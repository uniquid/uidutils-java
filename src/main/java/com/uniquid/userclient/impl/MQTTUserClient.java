package com.uniquid.userclient.impl;

import com.uniquid.messages.UniquidMessage;
import com.uniquid.messages.serializers.JSONMessageSerializer;
import com.uniquid.userclient.UserClient;
import com.uniquid.userclient.UserClientException;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Implementation of {@link UserClient} that uses MQTT protocol
 */
public class MQTTUserClient implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTUserClient.class);

    private String broker;
    private int timeoutInSeconds;
    private String destinationTopic;
    private String senderAddress;
    private JSONMessageSerializer messageSerializer;

    /**
     * Creates an instance from broker, destination topic and timeout
     * @param broker the broker to use
     * @param destinationTopic the topic that will receive the message
     * @param timeoutInSeconds the timeout in seconds to wait for a response
     */
    public MQTTUserClient(final String broker, final String destinationTopic, final int timeoutInSeconds,
                          String senderAddress) {
        this.broker = broker;
        this.destinationTopic = destinationTopic;
        this.timeoutInSeconds = timeoutInSeconds;
        this.senderAddress = senderAddress;
        this.messageSerializer = new JSONMessageSerializer();

    }

    @Override
    public UniquidMessage execute(final UniquidMessage userRequest) throws UserClientException {

        return sendRecv(userRequest);

    }

    public UniquidMessage sendRecv(final UniquidMessage userRequest) throws UserClientException {

        LOGGER.info("Sending output message to {}", destinationTopic);

        BlockingConnection connection = null;

        try {
            final MQTT mqtt = new MQTT();

            mqtt.setHost(broker);

            connection = mqtt.blockingConnection();
            connection.connect();

            // to subscribe
            final Topic[] topics = { new Topic(senderAddress, QoS.AT_LEAST_ONCE) };
            /*byte[] qoses = */connection.subscribe(topics);

            byte[] payload = messageSerializer.serialize(userRequest);

            // consume
            connection.publish(destinationTopic, payload, QoS.AT_LEAST_ONCE, false);

            final Message message = connection.receive(timeoutInSeconds, TimeUnit.SECONDS);

            if (message == null) {

                throw new TimeoutException();

            }

            payload = message.getPayload();

            message.ack();

            // Create a JSON Message
            return messageSerializer.deserialize(payload);

        } catch (TimeoutException ex) {

            LOGGER.error("Timeout while waiting response!", ex);

            throw new UserClientException("Timeout while waiting response!", ex);

        } catch (Throwable t) {

            LOGGER.error("Catched Exception: " + t.getMessage(), t);

            throw new UserClientException("Catched Exception: " + t.getMessage(), t);

        } finally {

            // disconnect
            try {

                if (connection != null) {

                    LOGGER.info("Disconnecting");

                    connection.disconnect();

                }

            } catch (Exception ex) {

                LOGGER.error("Catched Exception: " + ex.getMessage(), ex);

            }

        }

    }

    public void send(final UniquidMessage userRequest) throws UserClientException {

        LOGGER.info("Sending output message to {}", destinationTopic);

        BlockingConnection connection = null;

        try {
            final MQTT mqtt = new MQTT();

            mqtt.setHost(broker);

            connection = mqtt.blockingConnection();
            connection.connect();

            byte[] payload = messageSerializer.serialize(userRequest);

            // consume
            connection.publish(destinationTopic, payload, QoS.AT_LEAST_ONCE, false);

        } catch (TimeoutException ex) {

            LOGGER.error("Timeout while waiting response!", ex);

            throw new UserClientException("Timeout while waiting response!", ex);

        } catch (Throwable t) {

            LOGGER.error("Catched Exception: " + t.getMessage(), t);

            throw new UserClientException("Catched Exception: " + t.getMessage(), t);

        } finally {

            // disconnect
            try {

                if (connection != null) {

                    LOGGER.info("Disconnecting");

                    connection.disconnect();

                }

            } catch (Exception ex) {

                LOGGER.error("Catched Exception: " + ex.getMessage(), ex);

            }

        }

    }

}
