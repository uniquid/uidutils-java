package com.uniquid.blockchain;

/**
 * Represents information linked to an address
 */
public class AddressInfo {
	
	private long balance;
	private long totalReceived;
	private long totalSent;
	private long unconfirmedBalance;
	
	/**
	 * Returns the balance of the address
	 */
	public long getBalance() {
		return balance;
	}
	
	/**
	 * Set the balance of the address
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	/**
	 * Return the total received satoshi
	 */
	public long getTotalReceived() {
		return totalReceived;
	}
	
	/**
	 * Set the total received satoshi
	 */
	public void setTotalReceived(long totalReceived) {
		this.totalReceived = totalReceived;
	}
	
	/**
	 * Return the total sent satoshi
	 */
	public long getTotalSent() {
		return totalSent;
	}
	
	/**
	 * Set the total sent satoshi
	 */
	public void setTotalSent(long totalSent) {
		this.totalSent = totalSent;
	}
	
	/**
	 * Return the unconfirmed
	 */
	public long getUnconfirmedBalance() {
		return unconfirmedBalance;
	}
	
	/**
	 * Set the unconfirmed
	 */
	public void setUnconfirmedBalance(long unconfirmedBalance) {
		this.unconfirmedBalance = unconfirmedBalance;
	}

}
