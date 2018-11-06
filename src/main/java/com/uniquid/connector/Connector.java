/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.connector;

/**
 * Connector interface hides the specifics of a communication protocol and allows the library to works independently from
 * a particular implementation.
 */
public interface Connector extends AutoCloseable {

    /**
     * Establish the connection
     */
    void connect() throws ConnectorException;

    /**
     * Close the connection
     */
    void close() throws ConnectorException;

    /**
     * Listens for a connection to be made to this connector and accepts it. The method blocks until a
     * connection is made
     *
     * @return {@link EndPoint} the endpoint that wrap the communication with the User.
     * @throws ConnectorException in case a problem occurs.
     */
    EndPoint accept() throws ConnectorException, InterruptedException;

}
