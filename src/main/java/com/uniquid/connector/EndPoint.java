/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
