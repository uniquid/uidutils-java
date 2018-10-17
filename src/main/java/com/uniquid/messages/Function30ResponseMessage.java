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
