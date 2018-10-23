package com.uniquid.connector.impl;

import com.uniquid.connector.ConnectorException;
import com.uniquid.connector.EndPoint;
import com.uniquid.messages.*;
import com.uniquid.messages.serializers.JSONMessageSerializer;
import com.uniquid.userclient.UserClientException;
import com.uniquid.userclient.impl.MQTTUserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a {@link EndPoint} used by {@link MQTTConnector}
 */
public class MQTTEndPoint implements EndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTEndPoint.class);
    private static final int DEFAULT_TIMEOUT = 60;

    private String broker;

    private final UniquidMessage receivedMessage;
    private FunctionResponseMessage providerResponse;

    private MessageSerializer messageSerializer = new JSONMessageSerializer();

    /**
     * Creates a new instance from the byte array message and broker
     * @param mqttMessageRequest the message received
     * @param broker the broker to use
     * @throws ConnectorException in case a problem occurs.
     */
    MQTTEndPoint(final byte[] mqttMessageRequest, final String broker) throws ConnectorException {

        this.broker = broker;

        try {

            UniquidMessage messageReceived = messageSerializer.deserialize(mqttMessageRequest);

            if (MessageType.FUNCTION_REQUEST.equals(messageReceived.getMessageType())) {

                // Retrieve message
                receivedMessage = messageReceived;

                providerResponse = new FunctionResponseMessage();
                providerResponse.setId(((FunctionRequestMessage) messageReceived).getId());

            } else if (MessageType.UNIQUID_CAPABILITY.equals(messageReceived.getMessageType())) {

                receivedMessage = messageReceived;
                providerResponse = new FunctionResponseMessage();
                providerResponse.setId(0);

            } else if (MessageType.ANNOUNCE.equals(messageReceived.getMessageType())) {

                receivedMessage = messageReceived;

            } else {

                throw new Exception("Received an invalid message type " + messageReceived.getMessageType());

            }

        } catch (Exception ex) {

            throw new ConnectorException("Exception during creation of endpoint", ex);

        }
    }

    @Override
    public UniquidMessage getRequest() {
        return receivedMessage;
    }

    @Override
    public void setResponse(FunctionResponseMessage message) {
        providerResponse = message;
    }

    @Override
    public void flush() throws ConnectorException {

        if (receivedMessage instanceof FunctionRequestMessage) {

            FunctionRequestMessage message = (FunctionRequestMessage) receivedMessage;

            MQTTUserClient mqttUserClient = new MQTTUserClient(broker, message.getUser(), DEFAULT_TIMEOUT, providerResponse.getProvider());

            try {

                mqttUserClient.send(providerResponse);

            } catch (UserClientException e) {

                throw new ConnectorException("Exception", e);

            }

        }

    }

}