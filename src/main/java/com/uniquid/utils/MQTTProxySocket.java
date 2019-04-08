/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import com.uniquid.connector.Connector;
import com.uniquid.connector.EndPoint;
import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.MessageType;
import com.uniquid.messages.UniquidMessage;
import com.uniquid.userclient.UserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class open a connector as a Provider and when a message is received then forward the request via the
 * specified userclient
 */
public class MQTTProxySocket implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTProxySocket.class);

    private Connector connector;
    private UserClient userClient;

    public MQTTProxySocket(Connector connector, UserClient userClient) {
        this.connector = connector;
        this.userClient = userClient;
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            try {

                EndPoint endPoint = connector.accept();

                UniquidMessage inputMessage = endPoint.getRequest();

                if (MessageType.FUNCTION_REQUEST.equals(inputMessage.getMessageType())) {

                    LOGGER.info("Received input message {}", inputMessage.getMessageType());

                } else {

                    LOGGER.info("Unknown message type {} received", inputMessage.getMessageType());

                    throw new Exception("Unknown message type");

                }

                LOGGER.info("Forwarding request via TCP");
                FunctionResponseMessage response = (FunctionResponseMessage) userClient.execute(inputMessage);

                LOGGER.info("Received response!");
                FunctionResponseMessage outputMessage = new FunctionResponseMessage();
                outputMessage.setError(response.getError());
                outputMessage.setResult(response.getResult());
                endPoint.setResponse(outputMessage);

                LOGGER.info("Flushing response via MQTT");

                endPoint.flush();

            } catch (Throwable t) {

                LOGGER.error("Throwable catched", t);

            }

        }

    }

}
