/*
 * Copyright (c) 2016-2019. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.userclient;

public class UserClientTimeoutException extends UserClientException {

    public UserClientTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserClientTimeoutException(String message) {
        super(message);
    }
}
