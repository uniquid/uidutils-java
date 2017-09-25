package com.uniquid.message;

import org.junit.Test;

import com.uniquid.messages.FunctionRequestMessage;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.UniquidMessage;
import com.uniquid.messages.serializers.JSONMessageSerializer;

import junit.framework.Assert;

public class JSONMessageSerializerTest {

	@Test
	public void testSerialization() throws Exception {

		String request = "{\"sender\":\"ciccio\", \"body\": { \"method\":33, \"params\": \"{456}\", \"id\":123467 } }";
		
		FunctionRequestMessage functionRequestMessage = new FunctionRequestMessage();
		functionRequestMessage.setUser("ciccio");
		functionRequestMessage.setFunction(33);
		functionRequestMessage.setParameters("{456}");
		functionRequestMessage.setId(123467);

		String response = "{\"sender\":\"ciccio\", \"body\": { \"result\":\"43f\", \"error\": 0, \"id\":123456 } }";
		
		FunctionResponseMessage functionResponseMessage = new FunctionResponseMessage();
		functionResponseMessage.setProvider("ciccio");
		functionResponseMessage.setResult("43f");
		functionResponseMessage.setError(0);
		functionResponseMessage.setId(123456);

		UniquidMessage requestMessage = new JSONMessageSerializer().deserialize(request.getBytes());

		UniquidMessage responseMessage = new JSONMessageSerializer().deserialize(response.getBytes());

		String request1 = new String(new JSONMessageSerializer().serialize(requestMessage));
		String response1 = new String(new JSONMessageSerializer().serialize(responseMessage));
		
		Assert.assertEquals(requestMessage, functionRequestMessage);
		
		Assert.assertEquals(responseMessage, functionResponseMessage);

	}

}
