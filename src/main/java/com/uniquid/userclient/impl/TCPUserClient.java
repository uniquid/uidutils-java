/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.userclient.impl;

import com.uniquid.messages.UniquidMessage;
import com.uniquid.messages.serializers.JSONMessageSerializer;
import com.uniquid.userclient.UserClient;
import com.uniquid.userclient.UserClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.Arrays;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Implementation of {@link UserClient} that uses TCP protocol
 */
public class TCPUserClient implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPUserClient.class);

    private static final int PREFIX_LEN = 4;

    private String host;
    private int port;
    private int timeoutInSeconds;
    private JSONMessageSerializer messageSerializer;

    /**
     * Creates an instance from broker, destination topic and timeout
     * @param host the host to use
     * @param port the port where to listen
     * @param timeoutInSeconds the timeout in seconds to wait for a response
     */
    public TCPUserClient(final String host, final int port, final int timeoutInSeconds) {
        this.host = host;
        this.port = port;
        this.timeoutInSeconds = timeoutInSeconds;
        this.messageSerializer = new JSONMessageSerializer();

    }

    @Override
    public UniquidMessage execute(final UniquidMessage userRequest) throws UserClientException {

        return sendRecv(userRequest);

    }

    public UniquidMessage sendRecv(final UniquidMessage userRequest) throws UserClientException {

        LOGGER.info("Sending output message to {}", host);

        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeoutInSeconds * 1000);
            socket.setSoTimeout(timeoutInSeconds * 1000);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            byte[] payload = messageSerializer.serialize(userRequest);

            String msg = new String(payload);
            String msgfinal = msg + "\r\n";
            byte[] message = msgfinal.getBytes();

            int messageSize = message.length;

            byte[] prefix = ByteBuffer.allocate(PREFIX_LEN).order(ByteOrder.BIG_ENDIAN).putInt(messageSize).array();
            byte[] request = Arrays.concatenate(prefix, message);
            dataOutputStream.write(request);
            dataOutputStream.flush();

            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            byte[] length = new byte[PREFIX_LEN];
            dataInputStream.read(length, 0, PREFIX_LEN);
            ByteBuffer byteBuffer = ByteBuffer.wrap(length);
            int resultLen = byteBuffer.getInt();
            byte[] received = new byte[resultLen];
            dataInputStream.read(received, 0, received.length);

            // Create a JSON Message
            return messageSerializer.deserialize(received);

        } catch (SocketTimeoutException ex) {

            LOGGER.error("Timeout while conneting!", ex);

            throw new UserClientException("Timeout while waiting response!", ex);

        } catch (Throwable t) {

            LOGGER.error("Catched Exception: " + t.getMessage(), t);

            throw new UserClientException("Catched Exception: " + t.getMessage(), t);

        } finally {

            // disconnect
            try {

                if (socket != null && !socket.isClosed()) {

                    LOGGER.info("Disconnecting");

                    socket.close();

                }

            } catch (Exception ex) {

                LOGGER.error("Catched Exception: " + ex.getMessage(), ex);

            }

        }

    }

    public void send(final UniquidMessage userRequest) throws UserClientException {

        LOGGER.info("Sending output message to {}", host);

        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeoutInSeconds * 1000);
            socket.setSoTimeout(timeoutInSeconds * 1000);
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            byte[] payload = messageSerializer.serialize(userRequest);

            String msg = new String(payload);
            String msgfinal = msg + "\r\n";
            byte[] message = msgfinal.getBytes();

            int messageSize = message.length;
            byte[] prefix = ByteBuffer.allocate(PREFIX_LEN).order(ByteOrder.BIG_ENDIAN).putInt(messageSize).array();
            byte[] request = Arrays.concatenate(prefix, message);
            dataOutputStream.write(request);
            dataOutputStream.flush();

        } catch (SocketTimeoutException ex) {

            LOGGER.error("Timeout while waiting response!", ex);

            throw new UserClientException("Timeout while waiting response!", ex);

        } catch (Throwable t) {

            LOGGER.error("Catched Exception: " + t.getMessage(), t);

            throw new UserClientException("Catched Exception: " + t.getMessage(), t);

        } finally {

            // disconnect
            try {

                if (socket != null && !socket.isClosed()) {

                    LOGGER.info("Disconnecting");

                    socket.close();

                }

            } catch (Exception ex) {

                LOGGER.error("Catched Exception: " + ex.getMessage(), ex);

            }

        }

    }

}
