package com.uniquid.connector;

/**
 * Connector interface hides the specifics of a communication protocol and allows the library to works independently from
 * a particular implementation.
 */
public interface Connector {

    /**
     * Starts the connector
     */
    void start() throws ConnectorException;

    /**
     * Stop the connector
     */
    void stop() throws ConnectorException;

    /**
     * Listens for a connection to be made to this connector and accepts it. The method blocks until a
     * connection is made
     *
     * @return {@link EndPoint} the endpoint that wrap the communication with the User.
     * @throws ConnectorException in case a problem occurs.
     */
    EndPoint accept() throws ConnectorException, InterruptedException;

}
