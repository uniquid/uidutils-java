package com.uniquid.connector;

import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.UniquidMessage;

/**
 * Represents a communication between a Provider and a User
 */
public interface EndPoint {
	
	/**
	 * Returns the {@link ProviderRequest} performed by the User
	 * @return the {@link ProviderRequest} performed by the User
	 */
	public UniquidMessage getRequest();

	/**
	 * Returns the {@link ProviderResponse} to be returned to the User
	 * @return the {@link ProviderResponse} to be returned to the User
	 */
	public void setResponse(FunctionResponseMessage message);
	
	/**
	 * Closes this instance sending all the communication to the User.
	 */
	public void flush() throws ConnectorException;

}
