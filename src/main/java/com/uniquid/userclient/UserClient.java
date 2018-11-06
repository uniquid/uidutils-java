/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.userclient;

        import com.uniquid.messages.UniquidMessage;

/**
 * Allow an User to send an {@link UniquidMessage} to a Provider and have the response back
 */
public interface UserClient {

    /**
     * Send the {@link UniquidMessage} to a Provider and return the {@link UniquidMessage} back.
     * @param userMessage the request to send to the Provider
     * @return the {@link UniquidMessage} from the Provider.
     * @throws UserClientException in case a problem occurs.
     */
    UniquidMessage execute(UniquidMessage userMessage) throws UserClientException;

}
