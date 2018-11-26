/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.userclient;

public class UserClientException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Create new UserClientException exception
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public UserClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new UserClientException exception
     *
     * @param message
     *            the message
     */
    public UserClientException(String message) {
        super(message);
    }

}
