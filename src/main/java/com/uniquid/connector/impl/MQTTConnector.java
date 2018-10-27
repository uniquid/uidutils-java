package com.uniquid.connector.impl;

import com.uniquid.connector.Connector;
import com.uniquid.connector.ConnectorException;
import com.uniquid.connector.EndPoint;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Implementation of a {@link Connector} that uses the MQTT protocol.
 */
public class MQTTConnector implements Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTConnector.class);

    private String providerTopic;
    private String broker;
    private Queue<byte[]> inputQueue;

    private ScheduledExecutorService receiverExecutorService;

    /**
     * Creates a MQTTConnector that listen on the specified receiving topic and on the specified broker.
     * @param topic the topic to listen to
     * @param broker the MQTT broker to use
     */
    private MQTTConnector(final String topic, final String broker) {

        this.providerTopic = topic;
        this.broker = broker;
        this.inputQueue = new LinkedList<>();

    }

    /**
     * Builder for {@link MQTTConnector}
     */
    public static class Builder {
        private String _topic;
        private String _broker;

        /**
         * Set the listening topic
         * @param _topic the topic to listen to
         * @return the Builder
         */
        public Builder set_topic(String _topic) {
            this._topic = _topic;
            return this;
        }

        /**
         * Set the broker to use
         * @param _broker the broker to use
         * @return the Builder
         */
        public Builder set_broker(String _broker) {
            this._broker = _broker;
            return this;
        }

        /**
         * Returns an instance of a {@link MQTTConnector}
         * @return an instance of a {@link MQTTConnector}
         */
        public MQTTConnector build() {

            return new MQTTConnector(_topic, _broker);
        }

    }

    @Override
    public EndPoint accept() throws ConnectorException, InterruptedException {

        try {

            synchronized (inputQueue) {

                while (inputQueue.isEmpty()) {

                    LOGGER.trace("inputQueue is empty. waiting");

                    inputQueue.wait();

                }

                LOGGER.trace("inputQueue not empty! fetching element");

                byte[] inputMessage = inputQueue.poll();

                LOGGER.trace("returning MQTTEndPoint");

                return new MQTTEndPoint(inputMessage, broker);

            }

        } catch (InterruptedException ex) {

            LOGGER.error("Catched InterruptedException", ex);

            throw ex;

        } catch (Exception ex) {

            LOGGER.error("Catched Exception", ex);

            throw new ConnectorException(ex);

        }

    }

    @Override
    public void start() throws ConnectorException {

        LOGGER.info("Starting MQTTConnector");

        MQTT mqtt = new MQTT();
        try {
            mqtt.setHost(broker);
        } catch (URISyntaxException e) {
            throw new ConnectorException(e);
        }

        receiverExecutorService = Executors.newSingleThreadScheduledExecutor();

        final Runnable receiver = () -> {

            while (!Thread.currentThread().isInterrupted()) {

                LOGGER.info("Connecting to MQTT");

                BlockingConnection connection = mqtt.blockingConnection();

                try {
                    connection.connect();

                    // subscribe
                    Topic[] topics = {new Topic(providerTopic, QoS.AT_LEAST_ONCE)};
                    connection.subscribe(topics);

                    LOGGER.info("Waiting for a message!");

                    while (!Thread.currentThread().isInterrupted()) {

                        // blocks until new message receive
                        Message message = connection.receive();

                        LOGGER.info("Message received!");

                        byte[] payload = message.getPayload();

                        // mark message as acknowledged
                        message.ack();

                        // Create a JSON Message
                        synchronized (inputQueue) {

                            inputQueue.add(payload);
                            inputQueue.notifyAll();

                        }

                    }
                }  catch (InterruptedException ex) {
                    LOGGER.info("Received interrupt request. Exiting");
                    return;

                } catch (Throwable t) {
                    LOGGER.error("Catched Exception", t);

                } finally {
                    // disconnect
                    try {

                        LOGGER.info("Disconnecting");

                        connection.disconnect();

                    } catch (Exception ex) {

                        LOGGER.error("Catched Exception", ex);

                    }
                }
            }
        };

        LOGGER.info("Starting receiving");

        // Start receiver
        receiverExecutorService.execute(receiver);

    }

    @Override
    public void stop() {

        LOGGER.info("Stopping MQTTConnector");

        receiverExecutorService.shutdownNow();

    }

}
