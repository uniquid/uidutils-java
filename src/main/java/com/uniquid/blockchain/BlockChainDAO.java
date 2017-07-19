package com.uniquid.blockchain;

import java.util.Collection;

import com.uniquid.blockchain.exception.BlockChainException;

/**
 * Interface to interact with BlockChain
 */
public interface BlockChainDAO {
	
	/**
	 * Retrieve address information
	 */
	public AddressInfo retrieveAddressInfo(String address) throws BlockChainException;
	
	/**
	 * Retrieve a collection of utxo from an address
	 */
	public Collection<Utxo> retrieveUtxo(String address) throws BlockChainException;
	
	/**
	 * 
	 * @param txid
	 * @return
	 * @throws Exception
	 */
	public String retrieveRawTx(String txid) throws BlockChainException;

}
