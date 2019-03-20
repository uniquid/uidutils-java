/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

import org.spongycastle.util.encoders.Hex;

import java.util.Objects;
import java.util.Random;

/**
 * Represent a Function Request message: the User asks the provider to perform the specified method.
 */
public class FunctionRequestMessage implements UniquidMessage {

    private Random random = new Random();

    private long id;

    private String signature = "", parameters = "";

    private int method;

    private String sender;

    public FunctionRequestMessage() {

        // This will set a random id!
        do {
            id = random.nextInt(Integer.MAX_VALUE);
        } while (id == 0);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String prepareToSign() {

        StringBuilder serializedData = new StringBuilder();

        serializedData.append(method);
        serializedData.append(parameters);
        serializedData.append(id);

        return serializedData.toString();

    }

    @Override
    public MessageType getMessageType() {

        return MessageType.FUNCTION_REQUEST;

    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof FunctionRequestMessage))
            return false;

        if (this == object)
            return true;

        FunctionRequestMessage functionRequestMessage = (FunctionRequestMessage) object;

        return Objects.equals(id, functionRequestMessage.id) && Objects.equals(signature, functionRequestMessage.signature)
                && Objects.equals(parameters, functionRequestMessage.parameters)
                && Objects.equals(method, functionRequestMessage.method);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, signature, parameters, method);

    }

}
