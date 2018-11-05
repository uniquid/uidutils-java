/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

public class Function30ResponseMessage extends FunctionResponseMessage {

    private String txid;
    private int txidError;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getTxidError() {
        return txidError;
    }

    public void setTxidError(int txidError) {
        this.txidError = txidError;
    }
}
