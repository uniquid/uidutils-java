package com.uniquid.blockchain;

import com.uniquid.blockchain.exception.BlockChainException;

import java.util.Collection;

/**
 * Interface to interact with BlockChain
 */
public interface BlockChainDAO {

	/**
	 * Retrieve address information
	 */
	AddressInfo retrieveAddressInfo(String address) throws BlockChainException;

	/**
	 * Retrieve a collection of utxo from an address
	 */
	Collection<Utxo> retrieveUtxo(String address) throws BlockChainException;

	/**
	 * Retrieve a collection of utxo from an address
	 */
	Collection<Utxo> retrieveUtxo(String address, int maxUtxo) throws BlockChainException;

	/**
	 * Retrieve Transanction from blockchain
	 */
	Transaction retrieveTransaction(String txid) throws BlockChainException;

	/**
	 * Retrieve the raw transaction from a transaction id
	 */
	String retrieveRawTx(String txid) throws BlockChainException;

	/**
	 * Send a raw tx to the network
	 */
	String sendTx(String rawtx) throws BlockChainException;

}
