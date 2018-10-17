package com.uniquid.connector;

import com.uniquid.messages.FunctionResponseMessage;
import com.uniquid.messages.UniquidMessage;

/**
 * Represents a communication between a Provider and a User
 */
public interface EndPoint {

    /**
     * Returns the {@link UniquidMessage} performed by the User
     * @return the {@link UniquidMessage} performed by the User
     */
    UniquidMessage getRequest();

    /**
     * Set the {@link FunctionResponseMessage} to be returned to the User
     */
    void setResponse(FunctionResponseMessage message);

    /**
     * Closes this instance sending all the communication to the User.
     */
    void flush() throws ConnectorException;

}
