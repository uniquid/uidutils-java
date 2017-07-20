package com.uniquid.contract;

import java.util.BitSet;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.ScriptBuilder;

import com.uniquid.params.UniquidRegTest;

public class ContractUtils {
	
	public static final Coin FEE = Coin.COIN.divide(10000); /* 0,0001 */
	public static final Coin IMPRINT_COIN_VALUE = Coin.CENT;
	
	/*
	 * 1 - call http://explorer.uniquid.co:3001/insight-api/addr/moJ6LK1BZTvLPhA1XFefMmKCH3YGrdSegm/utxo
	 * retrieve txid e vout
	 * 
	 * 2 - call http://explorer.uniquid.co:3001/insight-api/rawtx/f7e7abd5a1157fe07cf76faf0513a39dc1e17e7849b079091b941f50e1e911f3
	 *  decode hex and retieve output number(vout)
	 *  
	 *  call this with the transactionoutput
	 * 
	 */
	public static Transaction buildAccessContract(final String userAddress, final String revokeAddress, final String changeAddress, final TransactionOutput prevOut, BitSet bitSet) throws Exception {
		
		Coin coinValue = Coin.COIN.divide(100000).multiply(95); /* 0,00095 */
		
		Coin totalCoinOut = Coin.ZERO;
		
		final Coin FEE = Coin.COIN.divide(100000).multiply(5); /* 0,00005 */
		
		final NetworkParameters networkParameters = UniquidRegTest.get();

        final Transaction transaction = new Transaction(networkParameters);
        
        if (userAddress != null) {
        	
	        // user output
	        TransactionOutput outputToUser = new TransactionOutput(networkParameters, transaction, coinValue, Address.fromBase58(networkParameters, userAddress));
	        transaction.addOutput(outputToUser);
	        
	        // add value
	        totalCoinOut = totalCoinOut.add(coinValue);
        
        }
        
        if (bitSet != null) {
        
	        // allow function 30
	        byte[] opreturn = new byte[80];
			
			byte[] bitmask = bitSet.toByteArray();
			
			opreturn[0] = 0;
			
			// skip first byte!!!
			// This will create the opreturn of 80 bytes containing the orchestration bit set to 1
			System.arraycopy(bitmask, 0, opreturn, 1, bitmask.length);
			
	        // opreturn output
	        transaction.addOutput(Coin.ZERO, ScriptBuilder.createOpReturnScript(opreturn));
        
        }
        
        if (revokeAddress != null) {

        		// revoke addr output
	        TransactionOutput outputToRevoke = new TransactionOutput(networkParameters, transaction, coinValue, Address.fromBase58(networkParameters, revokeAddress));
	        transaction.addOutput(outputToRevoke);
	        
	        // add value
	        totalCoinOut = totalCoinOut.add(coinValue);
	        
        }
        
        // add fee
        totalCoinOut = totalCoinOut.add(FEE);
        
        TransactionOutput outputChange = null;
        try {

            final Coin change = IMPRINT_COIN_VALUE.subtract(totalCoinOut);
            
			outputChange = new TransactionOutput(networkParameters, transaction, change, Address.fromBase58(networkParameters, changeAddress));
            transaction.addOutput(outputChange);
			
			// Connect all input
        	transaction.addInput(prevOut).setScriptSig(ScriptBuilder.createInputScript(TransactionSignature.dummy()));
            	
        	return transaction;

        } catch (Exception ex) {

           throw new Exception("Exception while creating an access contract", ex);

        }

	}
	
	/*
	 * Revoke a contract sending back to provider the coin
	 */
	public static Transaction buildRevokeContract(final String providerAddress, final TransactionOutput prevOut) throws Exception {
		
		
		final Coin FEE = Coin.COIN.divide(100000).multiply(5); /* 0,00005 */

		Coin coinValue = prevOut.getValue().subtract(FEE);
		
		Coin totalCoinOut = Coin.ZERO;
		
		final NetworkParameters networkParameters = UniquidRegTest.get();

        final Transaction transaction = new Transaction(networkParameters);
        
        if (providerAddress == null) {
        	
        	 throw new Exception("Provider address is null");
        	
        }
        
        // provider output
        TransactionOutput outputToProvider = new TransactionOutput(networkParameters, transaction, coinValue, Address.fromBase58(networkParameters, providerAddress));
        transaction.addOutput(outputToProvider);
        
        // add value
        totalCoinOut = totalCoinOut.add(coinValue);
        
        try {
			
			// Connect all input
        	transaction.addInput(prevOut).setScriptSig(ScriptBuilder.createInputScript(TransactionSignature.dummy()));
            	
        	return transaction;

        } catch (Exception ex) {

           throw new Exception("Exception while creating an imprinting contract", ex);

        }

	}

}
