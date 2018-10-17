package com.uniquid.connector.impl;

import com.uniquid.connector.ConnectorException;
import com.uniquid.connector.EndPoint;
import com.uniquid.messages.*;
import com.uniquid.messages.serializers.JSONMessageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.Arrays;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Implementation of a {@link EndPoint} used by {@link TCPConnector}
 */
public class TCPEndPoint implements EndPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(TCPEndPoint.class);
	private static final int PREFIX_LEN = 4;

	private Socket socket;

	private final FunctionRequestMessage providerRequest;
	private FunctionResponseMessage providerResponse;

	private MessageSerializer messageSerializer = new JSONMessageSerializer();

	/**
	 * Creates a new instance from the byte array message and broker
	 * @param socket the socket to use
	 * @throws ConnectorException in case a problem occurs.
	 */
	TCPEndPoint(Socket socket) throws ConnectorException {

		this.socket = socket;

		try {

			DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			byte[] length = new byte[PREFIX_LEN];
			int size = inputStream.read(length, 0, PREFIX_LEN);

			// TODO WE SHOULD PUT AN UPPER LIMIT HERE TO AVOID LEAKS/MALICIOUS REQUESTS...

			ByteBuffer buffer = ByteBuffer.wrap(length);
			int num = buffer.getInt();

			int len = 0;
			byte[] msg = new byte[num];

			int r = inputStream.read(msg, 0, msg.length);

			UniquidMessage messageReceived = messageSerializer.deserialize(msg);

			if (MessageType.FUNCTION_REQUEST.equals(messageReceived.getMessageType())) {

				// Retrieve message
				providerRequest = (FunctionRequestMessage) messageReceived;

				providerResponse = new FunctionResponseMessage();
				providerResponse.setId(providerRequest.getId());

			} else {

				throw new Exception("Received an invalid message type " + messageReceived.getMessageType());

			}

		} catch (Exception ex) {

			throw new ConnectorException("Exception during creation of endpoint", ex);

		}
	}

	@Override
	public FunctionRequestMessage getRequest() {
		return providerRequest;
	}

	@Override
	public void setResponse(FunctionResponseMessage message) {
		providerResponse = message;
	}

	@Override
	public void flush() throws ConnectorException {

		try {

			byte[] payload = messageSerializer.serialize(providerResponse);

			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			int messageSize = payload.length;

			byte[] prefix = ByteBuffer.allocate(PREFIX_LEN).order(ByteOrder.BIG_ENDIAN).putInt(messageSize).array();
			byte[] result = Arrays.concatenate(prefix, payload);

			dataOutputStream.write(result);
			dataOutputStream.flush();

		} catch (Exception ex) {

			LOGGER.error("Exception " + ex.getMessage(), ex);

			throw new ConnectorException(ex);

		} finally {

			try {

				socket.close();

			} catch (IOException ex) {

				LOGGER.error("Exception " + ex.getMessage(), ex);
			}

		}

	}

}