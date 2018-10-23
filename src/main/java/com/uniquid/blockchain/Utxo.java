package com.uniquid.blockchain;

/**
 * Represents an Unspent Transaction Output
 */
public class Utxo {

    private String address;
    private String txid;
    private long vout;
    private String scriptPubKey;
    private double amount;
    private long confirmation;

    /**
     * Returns the address that owns this utxo
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address that owns this utxo
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the transaction id that contains this utxo
     */
    public String getTxid() {
        return txid;
    }

    /**
     * Set the transaction id that contains this utxo
     */
    public void setTxid(String txid) {
        this.txid = txid;
    }

    /**
     * Returns the index of the output transaction linked to this utxo
     */
    public long getVout() {
        return vout;
    }

    /**
     * Set the index of the output transaction linked to this utxo
     */
    public void setVout(long vout) {
        this.vout = vout;
    }

    /**
     * Returns the script pubkey of this utxo
     */
    public String getScriptPubKey() {
        return scriptPubKey;
    }

    /**
     * Set the script pubkey of this utxo
     */
    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    /**
     * Returns the amount of this utxo in satoshi
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount of this utxo in satoshi
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the number of confirmations of this utxo
     */
    public long getConfirmation() {
        return confirmation;
    }

    /**
     * Set the number of confirmations of this utxo
     */
    public void setConfirmation(long confirmation) {
        this.confirmation = confirmation;
    }

}
