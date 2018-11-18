/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

import com.google.common.base.MoreObjects;

public class Function30ResponseMessage extends FunctionResponseMessage {

    private String serializedTx;
    private int txError;

    public String getSerializedTx() {
        return serializedTx;
    }

    public void setSerializedTx(String txid) {
        this.serializedTx = txid;
    }

    public int getTxError() {
        return txError;
    }

    public void setTxError(int txError) {
        this.txError = txError;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serializedTx", serializedTx)
                .add("txError", txError)
                .toString();
    }
}
