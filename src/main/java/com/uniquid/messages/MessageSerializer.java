package com.uniquid.messages;

public interface MessageSerializer {

	/**
	 * Serialize an UniquidMessage
	 * 
	 * @param uniquidMessage
	 * @return
	 * @throws MessageSerializerException 
	 */
	public byte[] serialize(UniquidMessage uniquidMessage) throws MessageSerializerException;
	
	/**
	 * Deserialize ad Uniquid Message
	 * 
	 * @param payload
	 * @return
	 * @throws MessageSerializerException 
	 */
	public UniquidMessage deserialize(byte[] payload) throws MessageSerializerException;
	
}
