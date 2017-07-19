package com.uniquid.blockchain;

public class AddressInfo {
	
	private long balance;
	private long totalReceived;
	private long totalSent;
	private long unconfirmedBalance;
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getTotalReceived() {
		return totalReceived;
	}
	public void setTotalReceived(long totalReceived) {
		this.totalReceived = totalReceived;
	}
	public long getTotalSent() {
		return totalSent;
	}
	public void setTotalSent(long totalSent) {
		this.totalSent = totalSent;
	}
	public long getUnconfirmedBalance() {
		return unconfirmedBalance;
	}
	public void setUnconfirmedBalance(long unconfirmedBalance) {
		this.unconfirmedBalance = unconfirmedBalance;
	}

}
