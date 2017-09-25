package com.uniquid.messages;

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