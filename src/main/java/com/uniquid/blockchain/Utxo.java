package com.uniquid.blockchain;

public class Utxo {
	
	private String address;
	private String txid;
	private long vout;
	private String scriptPubKey;
	private long amount;
	private long confirmation;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	public long getVout() {
		return vout;
	}
	public void setVout(long vout) {
		this.vout = vout;
	}
	public String getScriptPubKey() {
		return scriptPubKey;
	}
	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getConfirmation() {
		return confirmation;
	}
	public void setConfirmation(long confirmation) {
		this.confirmation = confirmation;
	}
	
}
