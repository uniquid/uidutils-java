package com.uniquid.blockchain.exception;

public class BlockChainException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	/**
	 * Create new blockchain exception
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public BlockChainException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create new blockchain exception
	 * 
	 * @param message
	 *            the message
	 */
	public BlockChainException(String message) {
		super(message);
	}

}
