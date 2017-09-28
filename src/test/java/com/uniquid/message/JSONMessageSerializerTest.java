package com.uniquid.message;

import org.junit.Test;

import com.uniquid.messages.AnnounceMessage;
import com.uniquid.messages.FunctionRequestMessage;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.MessageSerializerException;
import com.uniquid.messages.UniquidMessage;
import com.uniquid.messages.serializers.JSONMessageSerializer;

import junit.framework.Assert;

public class JSONMessageSerializerTest {

	@Test
	public void testSerialization() throws MessageSerializerException {

		String announce = "{ \"name\":\"ciccio\", \"xpub\":\"1234567\" }";
		
		AnnounceMessage announceMessage = new AnnounceMessage();
		announceMessage.setName("ciccio");
		announceMessage.setPubKey("1234567");
		
		String request = "{\"sender\":\"ciccio\", \"body\": { \"method\":33, \"params\": \"456\", \"id\":123467 } }";
		
		FunctionRequestMessage functionRequestMessage = new FunctionRequestMessage();
		functionRequestMessage.setUser("ciccio");
		functionRequestMessage.setFunction(33);
		functionRequestMessage.setParameters("456");
		functionRequestMessage.setId(123467);

		String response = "{\"sender\":\"ciccio\", \"body\": { \"result\":\"43f\", \"error\": 0, \"id\":123456 } }";
		
		FunctionResponseMessage functionResponseMessage = new FunctionResponseMessage();
		functionResponseMessage.setProvider("ciccio");
		functionResponseMessage.setResult("43f");
		functionResponseMessage.setError(0);
		functionResponseMessage.setId(123456);
		
		String invalid1 = "{\"sender\":\"ciccio\", \"body\": { \"invalid\":\"invalid\" } }";
		
		String invalid2 = "{\"invalid\":\"message\" }";

		UniquidMessage announceMessage2 = new JSONMessageSerializer().deserialize(announce.getBytes());
		
		UniquidMessage requestMessage = new JSONMessageSerializer().deserialize(request.getBytes());

		UniquidMessage responseMessage = new JSONMessageSerializer().deserialize(response.getBytes());
		
		try {
			new JSONMessageSerializer().deserialize(invalid1.getBytes());
			Assert.fail();
		} catch (MessageSerializerException ex) {
			// Expected
		}
		
		try {
			new JSONMessageSerializer().deserialize(invalid2.getBytes());
			Assert.fail();
		} catch (MessageSerializerException ex) {
			// Expected
		}

		String request1 = new String(new JSONMessageSerializer().serialize(requestMessage));
		String response1 = new String(new JSONMessageSerializer().serialize(responseMessage));
		String ann = new String(new JSONMessageSerializer().serialize(announceMessage2));
		
		Assert.assertEquals(announceMessage2, announceMessage);
		
		Assert.assertEquals(requestMessage, functionRequestMessage);
		
		Assert.assertEquals(responseMessage, functionResponseMessage);

	}

}
