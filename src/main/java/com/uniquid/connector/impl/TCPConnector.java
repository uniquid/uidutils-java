/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.connector.impl;

import com.uniquid.connector.Connector;
import com.uniquid.connector.ConnectorException;
import com.uniquid.connector.EndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Implementation of a {@link Connector} that uses the TCP plain protocol.
 */
public class TCPConnector implements Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPConnector.class);

    private int port;
    private final Queue<Socket> inputQueue = new LinkedList<>();

    private ScheduledExecutorService receiverExecutorService;

    /**
     * Creates a MQTTConnector that listen on the specified receiving topic and on the specified broker.
     * @param port the port where start the connector
     */
    public TCPConnector(int port) {

        this.port = port;

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

                Socket inputSocket = inputQueue.poll();

                LOGGER.trace("returning MQTTEndPoint");

                return new TCPEndPoint(inputSocket);

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
    public void connect() {

        receiverExecutorService = Executors.newSingleThreadScheduledExecutor();

        final Runnable receiver = () -> {

            try (ServerSocket serverSocket = new ServerSocket(port)) {

                while (!Thread.currentThread().isInterrupted()) {

                    Socket socket = serverSocket.accept();

                    // Create a JSON Message
                    synchronized (inputQueue) {

                        inputQueue.add(socket);
                        inputQueue.notifyAll();

                    }

                }

            } catch (Exception ex) {

                LOGGER.error("Catched Exception", ex);

            }

        };

        LOGGER.info("Starting receiving");

        // Start receiver
        receiverExecutorService.execute(receiver);

    }

    @Override
    public void close() {

        LOGGER.info("Stopping MQTTConnector");

        receiverExecutorService.shutdownNow();

    }

}
