package com.uniquid.messages;

/**
 * Thrown when a problem during message serialization/deserialization occurs
 */
public class MessageSerializerException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Create new message serializer exception
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public MessageSerializerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create new message serializer exception
	 *
	 * @param message
	 *            the message
	 */
	public MessageSerializerException(String message) {
		super(message);
	}

}