package com.uniquid.messages;

public interface MessageSerializer {

	/**
	 * Serialize an UniquidMessage
	 * 
	 * @param uniquidMessage
	 * @return
	 * @throws Exception 
	 */
	public byte[] serialize(UniquidMessage uniquidMessage) throws Exception;
	
	/**
	 * Deserialize ad Uniquid Message
	 * 
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	public UniquidMessage deserialize(byte[] payload) throws Exception;
	
}
