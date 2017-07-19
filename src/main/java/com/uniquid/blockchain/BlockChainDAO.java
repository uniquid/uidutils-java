package com.uniquid.blockchain;

import java.util.Collection;

/**
 * Interface to interact with BlockChain
 */
public interface BlockChainDAO {
	
	/**
	 * Retrieve address information
	 */
	public AddressInfo retrieveAddressInfo(String address) throws Exception;
	
	/**
	 * Retrieve a collection of utxo from an address
	 */
	public Collection<Utxo> retrieveUtxo(String address) throws Exception;
	
	/**
	 * 
	 * @param txid
	 * @return
	 * @throws Exception
	 */
	public String retrieveRawTx(String txid) throws Exception;

}
