package com.uniquid.messages;

/**
 * A Message is a data structure that can be serialized/deserialized and represents a message between an User and a
 * Provider
 */
public interface UniquidMessage {
	
	/**
	 * Return the {@link MessageType} of this message
	 * 
	 * @return the {@link MessageType} of this message
	 */
	public MessageType getMessageType();

}
