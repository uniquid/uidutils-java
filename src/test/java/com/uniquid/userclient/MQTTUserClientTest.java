/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.userclient;

import com.uniquid.messages.FunctionRequestMessage;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.serializers.JSONMessageSerializer;
import com.uniquid.userclient.impl.MQTTUserClient;
import org.fusesource.mqtt.client.*;
import org.junit.Assert;
import org.junit.Test;

public class MQTTUserClientTest {

    private String broker = "tcp://18.224.232.243:1883";

    @Test
    public void testConstructor() {
        String destination = "test";
        int timeout = 10;

        MQTTUserClient mqttUserClient = new MQTTUserClient(broker, destination, timeout, "test2");
        Assert.assertNotNull(mqttUserClient);
    }

    @Test
    public void testSendOutputMessage() {
        String sender = "sender";
        int method = 5;
        String params = "params";

        FunctionRequestMessage providerRequest = new FunctionRequestMessage();
        providerRequest.setUser(sender);
        providerRequest.setFunction(method);
        providerRequest.setParameters(params);

        String destination = "test";
        int timeout = 10;

        final MQTTUserClient mqttUserClient = new MQTTUserClient(broker, destination, timeout, sender);
        Assert.assertNotNull(mqttUserClient);

        new Thread(this::startMqttServerMock).start();

        try {
            FunctionResponseMessage providerResponse = (FunctionResponseMessage) mqttUserClient.execute(providerRequest);
            Assert.assertNotNull(providerResponse);

            Assert.assertEquals(providerResponse.getId(), providerRequest.getId());
        } catch (UserClientException e) {
            Assert.fail();
        }

    }

    private void startMqttServerMock() {

        String topic = "test";
        Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
        BlockingConnection connection;

        try{
            MQTT mqtt = new MQTT();
            mqtt.setHost(broker);
            connection = mqtt.blockingConnection();
            connection.connect();
            connection.subscribe(topics);
            // blocks!!!
            Message message = connection.receive();

            byte[] payload = message.getPayload();

            message.ack();

            Assert.assertNotNull(message);

            FunctionRequestMessage rpcProviderRequest = (FunctionRequestMessage) new JSONMessageSerializer().deserialize(payload);
            Assert.assertNotNull(rpcProviderRequest);

            FunctionResponseMessage rpcProviderResponse = new FunctionResponseMessage();
            rpcProviderResponse.setProvider("test");
            rpcProviderResponse.setError(0);
            rpcProviderResponse.setResult("result");
            rpcProviderResponse.setId(rpcProviderRequest.getId());

            connection.publish(rpcProviderRequest.getUser(), new JSONMessageSerializer().serialize(rpcProviderResponse), QoS.AT_LEAST_ONCE, false);

            connection.disconnect();

        } catch (Throwable t) {
            Assert.fail();
        }
    }

    @Test(expected = UserClientException.class)
    public void testSendOutputMessageException() throws UserClientException{
        String sender = "sender";
        int method = 5;
        String params = "params";

        FunctionRequestMessage providerRequest = new FunctionRequestMessage();
        providerRequest.setUser(sender);
        providerRequest.setFunction(method);
        providerRequest.setParameters(params);
        providerRequest.setId(1234);

        String destination = "test";
        int timeout = 5;

        final MQTTUserClient mqttUserClient = new MQTTUserClient(broker, destination, timeout, sender);
        Assert.assertNotNull(mqttUserClient);

        new Thread(this::startMqttServerMockException).start();

        FunctionRequestMessage providerResponse = (FunctionRequestMessage) mqttUserClient.execute(providerRequest);
        Assert.assertNotNull(providerResponse);

        Assert.assertEquals(providerRequest.getId(), providerResponse.getId());

    }

    private void startMqttServerMockException() {

        String topic = "test";
        Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
        BlockingConnection connection;

        try{
            MQTT mqtt = new MQTT();
            mqtt.setHost(broker);
            connection = mqtt.blockingConnection();
            connection.connect();
            connection.subscribe(topics);
            // blocks!!!
            Message message = connection.receive();

            byte[] payload = message.getPayload();

            message.ack();

            Assert.assertNotNull(message);

            FunctionRequestMessage functionRequestMessage = (FunctionRequestMessage) new JSONMessageSerializer().deserialize(payload);
            Assert.assertNotNull(functionRequestMessage);

            FunctionResponseMessage functionResponseMessage = new FunctionResponseMessage();
            functionResponseMessage.setProvider("sender");
            functionResponseMessage.setResult("result");
            functionResponseMessage.setError(0);

            connection.disconnect();

        } catch (Throwable t) {
            Assert.fail();
        }
    }

}
