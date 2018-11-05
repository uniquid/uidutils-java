/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

/**
 * A Message is a data structure that can be serialized/deserialized and represents a message between an User and a
 * Provider
 */
public interface UniquidMessage {

    /**
     * Return the {@link MessageType} of this message
     *
     * @return the {@link MessageType} of this message
     */
    MessageType getMessageType();

}
