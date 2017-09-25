package com.uniquid.messages.serializers;

import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.uniquid.messages.AnnounceMessage;
import com.uniquid.messages.FunctionRequestMessage;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.MessageSerializer;
import com.uniquid.messages.MessageSerializerException;
import com.uniquid.messages.MessageType;
import com.uniquid.messages.UniquidMessage;

public class JSONMessageSerializer implements MessageSerializer {
	
	@Override
	public byte[] serialize(UniquidMessage uniquidMessage) throws MessageSerializerException {
		
		if (MessageType.FUNCTION_REQUEST.equals(uniquidMessage.getMessageType())) {
			
			FunctionRequestMessage functionRequestMessage = (FunctionRequestMessage) uniquidMessage;
			
			// Create empty json object
			JSONObject jsonObject = new JSONObject();

			// populate sender
			jsonObject.put("sender", functionRequestMessage.getUser());

			// Create empty json child
			JSONObject jsonbody = new JSONObject();

			// Put all keys inside body
			jsonbody.put("method", functionRequestMessage.getFunction());
			
			jsonbody.put("params", functionRequestMessage.getParameters());
			
			jsonbody.put("id", functionRequestMessage.getId());

			// Add body
			jsonObject.put("body", jsonbody);

			return jsonObject.toString().getBytes();
			
		} else if (MessageType.FUNCTION_RESPONSE.equals(uniquidMessage.getMessageType())) {
			
			FunctionResponseMessage functionResponseMessage = (FunctionResponseMessage) uniquidMessage;
			
			final JSONObject jsonBody = new JSONObject();
			
			jsonBody.put("result", functionResponseMessage.getResult());
			jsonBody.put("error", functionResponseMessage.getError());
			jsonBody.put("id", functionResponseMessage.getId());

			final JSONObject jsonResponse = new JSONObject();

			jsonResponse.put("sender", functionResponseMessage.getProvider());
			
			jsonResponse.put("body", jsonBody);
			
			return jsonResponse.toString().getBytes();
			
		} else if (MessageType.ANNOUNCE.equals(uniquidMessage.getMessageType())) {
			
			AnnounceMessage announceMessage = (AnnounceMessage) uniquidMessage;
			
			final JSONObject jsonResponse = new JSONObject();
			
			jsonResponse.put("name", announceMessage.getName());
			jsonResponse.put("xpub", announceMessage.getPubKey());

			return jsonResponse.toString().getBytes();
			
		}
		
		throw new MessageSerializerException("Unknown message type " + uniquidMessage.getMessageType());
		
	}

	@Override
	public UniquidMessage deserialize(byte[] payload) throws MessageSerializerException {
		
		String jsonString = new String(payload, StandardCharsets.UTF_8);
		
		final JSONObject jsonMessage = new JSONObject(jsonString);
		
		if (jsonMessage.has("sender")) {

			final String sender = jsonMessage.getString("sender");
	
			final JSONObject jsonBody = jsonMessage.getJSONObject("body");
	
			// IS Request?
			if (jsonBody.has("method")) {
				
				final int method = jsonBody.getInt("method");
				
				final String params = jsonBody.getString("params"); 
				
				final long id = jsonBody.getLong("id");
				
				FunctionRequestMessage requestMessage = new FunctionRequestMessage();
				
				requestMessage.setUser(sender);
				requestMessage.setFunction(method);
				requestMessage.setParameters(params);
				requestMessage.setId(id);
				
				return requestMessage;
				
			} else if (jsonBody.has("result")) {
				
				final String result = jsonBody.getString("result");
				
				final int error =jsonBody.getInt("error"); 
				
				final long id = jsonBody.getLong("id");
				
				FunctionResponseMessage responseMessage = new FunctionResponseMessage();
				
				responseMessage.setProvider(sender);
				responseMessage.setResult(result);
				responseMessage.setError(error);
				responseMessage.setId(id);
				
				return responseMessage;
				
			}
		
		} else if (jsonMessage.has("xpub")) {
			
			final String name = jsonMessage.getString("name");
			
			final String pubKey = jsonMessage.getString("xpub");
			
			AnnounceMessage announceMessage = new AnnounceMessage();
			
			announceMessage.setName(name);
			announceMessage.setPubKey(pubKey);
			
			return announceMessage;
			
		}
		
		throw new MessageSerializerException("Invalid message to deserialize!");
	}
	
}
