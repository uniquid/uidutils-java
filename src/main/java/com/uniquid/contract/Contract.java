/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.contract;

import com.uniquid.blockchain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Contract {

    private static final Logger LOGGER = LoggerFactory.getLogger(Contract.class);

    private static final int MEMPOOL_WAIT_TIME = 60 * 7;     // How much time wait for mempool pick transaction in seconds
    private static final int MINIMUM_CONFIRMATIONS = 6;

    public enum ContractStatus {
        CREATED,    // Contract was created and sent to blockchain but wasn't picked by mempool
        PENDING,    // Contract was picked by mempool but has 0 confirmation
        ACTIVE,     // Contract has at least 1 confirmation
        CONFIRMED,  // Contract has more then 6 confirmation
        REVOKED,    // Contract was revoked
        FAILED      // Contract was failed
    }

    public Contract() {
        contractStatus = ContractStatus.CREATED;
    }

    protected String txid;

    protected long confirmations;

    protected ContractStatus contractStatus;

    protected String error;

    protected long creationTime;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus state) {
        this.contractStatus = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return life time of contract starting from creation moment in seconds
     */
    public long calcContractLifeTime() {
        return (System.currentTimeMillis() - creationTime) / 1000;
    }

    public void updateByTransaction(Transaction transaction) {


        switch (contractStatus) {
            case CREATED:
                if (transaction == null) {

                    // transaction not found in blockchain - check how long time ago was created transaction
                    long contractLifeTime = calcContractLifeTime();
                    if (contractLifeTime > MEMPOOL_WAIT_TIME) {

                        // contract wasn't picked by mempool during MEMPOOL_WAIT_TIME - transaction disappeared
                        LOGGER.info("TX {} wasn't picked by mempool during {} sec, then disappeared from blockchain.",
                                txid, contractLifeTime);
                        contractStatus = ContractStatus.FAILED;
                        error = "TX wan't picked by mempool during " + contractLifeTime +
                                " sec, then disappeared from blockchain.";
                    } else {

                        // wait few more minutes, until mempool pick transaction
                        LOGGER.info("TX {} wasn't picked by mempool yet. Waiting few more minutes.", txid);
                    }
                } else {

                    // transaction found update status
                    long txConfirmations = transaction.getConfirmations();

                    if (txConfirmations >= MINIMUM_CONFIRMATIONS) {

                        // if TX already has enough confirmations then set status to ACTIVE
                        LOGGER.info("TX {} included in blockchain with {} confirmation(s).", txid, confirmations);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.CONFIRMED;
                    } else if (txConfirmations > 0) {

                        // if at least on confirmation then confirmation then set status to ACTIVE
                        LOGGER.info("TX {} was found in blockchain with {} confirmation(s).", txid, confirmations);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.ACTIVE;
                    } else {

                        // if there is no confirmations, then set status to PENDING
                        LOGGER.info("TX {} was found in blockchain but unconfirmed yet.", txid);
                        contractStatus = ContractStatus.PENDING;
                    }
                }
                break;

            case PENDING:
                if (transaction == null) {

                    // transaction was picked by mempool, but then disappeared from blockchain
                    LOGGER.info("TX {} was picked by mempool, but then disappeared from blockchain.", txid);
                    contractStatus = ContractStatus.FAILED;
                    error = "TX was picked by mempool, but then disappeared from blockchain.";
                } else {

                    // check how much confirmations has transaction
                    long txConfirmations = transaction.getConfirmations();
                    if (txConfirmations >= MINIMUM_CONFIRMATIONS) {

                        // if TX already has enough confirmations then set status to ACTIVE
                        LOGGER.info("TX {} included in blockchain with {} confirmation(s).", txid, confirmations);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.CONFIRMED;
                    } else if (txConfirmations > 0) {

                        // found at least 1 confirmation - set status to ACTIVE
                        LOGGER.info("TX {} was found in blockchain with {} confirmation(s).", txid, confirmations);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.ACTIVE;
                    }
                }
                break;

            case ACTIVE:
                if (transaction == null) {

                    // transaction was included and confirmed in fork chain, then disappeared
                    LOGGER.info("TX {} was included in fork chain, then disappeared from blockchain.", txid);
                    contractStatus = ContractStatus.FAILED;
                    error = "TX was included in form chain, than disappeared from blockchain.";
                } else {

                    // check how much confirmations has transaction
                    long txConfirmations = transaction.getConfirmations();
                    if (txConfirmations == 0) {

                        // was included and confirmed in fork chain, but then it was canceled
                        LOGGER.info("TX {} was included in fork chain, but then it was canceled.", txid);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.PENDING;
                    } else if (txConfirmations >= MINIMUM_CONFIRMATIONS) {

                        // transaction got minimum required number of confirmations, set status to CONFIRMED
                        LOGGER.info("TX {} got {} confirmation(s) and can't be canceled anymore.", txid,
                                confirmations);
                        confirmations = txConfirmations;
                        contractStatus = ContractStatus.CONFIRMED;
                    } else {

                        // number of confirmations from 1 to MINIMUM_CONFIRMATIONS - update number of confirmations
                        confirmations = txConfirmations;
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("No suitable handler for given contract status " + contractStatus);
        }
    }
}
