/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

/**
 * A MessageSerializer is able to trasform an UniquidMessage to an array of bytes.
 * The format is completely defined by the implementor.
 */
public interface MessageSerializer {

    /**
     * Serialize an UniquidMessage to an array of bytes
     *
     * @param uniquidMessage the uniquid message to serialize
     * @return the byte array representing the uniquid message
     * @throws MessageSerializerException in case a problem occurs during serialization
     */
    byte[] serialize(UniquidMessage uniquidMessage) throws MessageSerializerException;

    /**
     * Deserialize an array of bytes into an Uniquid Message instance
     *
     * @param payload the array of bytes to deserialize
     * @return an Uniquid Message instance representing the byte array
     * @throws MessageSerializerException in case a problem occurs during deserialization
     */
    UniquidMessage deserialize(byte[] payload) throws MessageSerializerException;

}
