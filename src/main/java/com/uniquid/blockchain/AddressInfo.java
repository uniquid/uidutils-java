package com.uniquid.blockchain;

/**
 * Represents information linked to an address
 */
public class AddressInfo {
	
	private double balance;
	private double totalReceived;
	private double totalSent;
	private double unconfirmedBalance;
	
	/**
	 * Returns the balance of the address
	 */
	public double getBalance() {
		return balance;
	}
	
	/**
	 * Set the balance of the address
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	/**
	 * Return the total received satoshi
	 */
	public double getTotalReceived() {
		return totalReceived;
	}
	
	/**
	 * Set the total received satoshi
	 */
	public void setTotalReceived(double totalReceived) {
		this.totalReceived = totalReceived;
	}
	
	/**
	 * Return the total sent satoshi
	 */
	public double getTotalSent() {
		return totalSent;
	}
	
	/**
	 * Set the total sent satoshi
	 */
	public void setTotalSent(double totalSent) {
		this.totalSent = totalSent;
	}
	
	/**
	 * Return the unconfirmed
	 */
	public double getUnconfirmedBalance() {
		return unconfirmedBalance;
	}
	
	/**
	 * Set the unconfirmed
	 */
	public void setUnconfirmedBalance(double unconfirmedBalance) {
		this.unconfirmedBalance = unconfirmedBalance;
	}

}
