/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.registry.exception;

public class RegistryException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Create new blockchain exception
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new blockchain exception
     *
     * @param message
     *            the message
     */
    public RegistryException(String message) {
        super(message);
    }

}
