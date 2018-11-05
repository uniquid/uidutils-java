/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages.serializers;

import com.uniquid.messages.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of Uniquid Messages with JSON format
 */
public class JSONMessageSerializer implements MessageSerializer {

    // Pattern: "error - txid"
    // Example: "0 - 913ae6d8d4f149e0286bd73d4b616b4c8478f256f7929373ffae11224066b4ee"
    private static final Pattern PATTERN = Pattern.compile("^(\\d+)+\\s+-\\s+(\\w+)$");

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

        } else if (MessageType.UNIQUID_CAPABILITY.equals(uniquidMessage.getMessageType())) {

            CapabilityMessage capabilityMessage = (CapabilityMessage) uniquidMessage;

            final JSONObject jsonResponse = new JSONObject();

            jsonResponse.put("assigner", capabilityMessage.getAssigner());
            jsonResponse.put("resourceID", capabilityMessage.getResourceID());
            jsonResponse.put("assignee", capabilityMessage.getAssignee());
            jsonResponse.put("rights", capabilityMessage.getRights());
            jsonResponse.put("since", capabilityMessage.getSince());
            jsonResponse.put("until", capabilityMessage.getUntil());
            jsonResponse.put("assignerSignature", capabilityMessage.getAssignerSignature());

            return jsonResponse.toString().getBytes();

        } else {

            throw new MessageSerializerException("Unknown message type " + uniquidMessage.getMessageType());

        }

    }

    @Override
    public UniquidMessage deserialize(byte[] payload) throws MessageSerializerException, JSONException {

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

                // IS function 30 ?
                Matcher matcher = PATTERN.matcher(result);
                if (matcher.find()) {
                    final int txidError = Integer.parseInt(matcher.group(1));
                    final String txid = matcher.group(2);

                    Function30ResponseMessage responseMessage = new Function30ResponseMessage();

                    responseMessage.setProvider(sender);
                    responseMessage.setResult(result);
                    responseMessage.setError(error);
                    responseMessage.setId(id);
                    responseMessage.setTxid(txid);
                    responseMessage.setTxidError(txidError);

                    return responseMessage;
                } else {
                    FunctionResponseMessage responseMessage = new FunctionResponseMessage();

                    responseMessage.setProvider(sender);
                    responseMessage.setResult(result);
                    responseMessage.setError(error);
                    responseMessage.setId(id);

                    return responseMessage;
                }

            } else {

                throw new MessageSerializerException("Invalid message to deserialize!");

            }

        } else if (jsonMessage.has("xpub")) {

            final String name = jsonMessage.getString("name");

            final String pubKey = jsonMessage.getString("xpub");

            AnnounceMessage announceMessage = new AnnounceMessage();

            announceMessage.setName(name);
            announceMessage.setPubKey(pubKey);

            return announceMessage;

        } else if (jsonMessage.has("assignerSignature")) {

            String assigner = jsonMessage.getString("assigner");

            String resourceID = jsonMessage.getString("resourceID");
            String assignee = jsonMessage.getString("assignee");
            String rights = jsonMessage.getString("rights");
            long since = jsonMessage.getLong("since");
            long until = jsonMessage.getLong("until");

            String assignerSignature = jsonMessage.getString("assignerSignature");

            CapabilityMessage capabilityMessage = new CapabilityMessage();
            capabilityMessage.setAssigner(assigner);
            capabilityMessage.setResourceID(resourceID);
            capabilityMessage.setAssignee(assignee);
            capabilityMessage.setRights(rights);
            capabilityMessage.setSince(since);
            capabilityMessage.setUntil(until);
            capabilityMessage.setAssignerSignature(assignerSignature);

            return capabilityMessage;

        } else {

            throw new MessageSerializerException("Invalid message to deserialize!");

        }
    }

}
