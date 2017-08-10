package com.uniquid.contract;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.ScriptBuilder;

public class ContractUtils {
	
	public static final Coin FEE = Coin.COIN.divide(10000); /* 0,0001 */
	public static final Coin IMPRINT_COIN_VALUE = Coin.CENT; /*0,01 */
	public static final Coin COIN_OUTPUT = Coin.COIN.divide(10000); /* 0,0001 */
	
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
	public static Transaction buildAccessContract(final String userAddress, final String revokeAddress, final String changeAddress, final TransactionOutput prevOut, BitSet bitSet, NetworkParameters networkParameters) throws Exception {
		
		ArrayList<TransactionOutput> inputs = new ArrayList<TransactionOutput>();
		
		inputs.add(prevOut);
		
		return buildAccessContract(userAddress, revokeAddress, changeAddress, inputs, bitSet, networkParameters);

	}
	
	public static Transaction buildAccessContract(final String userAddress, final String revokeAddress, final String changeAddress, final List<TransactionOutput> inputs, BitSet bitSet, NetworkParameters networkParameters) throws Exception {
		
		Coin coinValue = COIN_OUTPUT;
		
		Coin totalCoinOut = Coin.ZERO;
		
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
        	
    		final Coin change = sum(inputs).subtract(totalCoinOut);
            
			outputChange = new TransactionOutput(networkParameters, transaction, change, Address.fromBase58(networkParameters, changeAddress));
            transaction.addOutput(outputChange);
			
			// Connect all input
            for (TransactionOutput input : inputs) {
            	
            	transaction.addInput(input).setScriptSig(ScriptBuilder.createInputScript(TransactionSignature.dummy()));
            	
            }
            	
        	return transaction;

        } catch (Exception ex) {

           throw new Exception("Exception while creating an access contract", ex);

        }

	}
	
	private static Coin sum(List<TransactionOutput> outputs) {
		
		Coin sum = Coin.ZERO;
		
		for (TransactionOutput out : outputs) {
			
			sum = sum.add(out.getValue());
			
		}
		
		return sum;
		
	}
	
	/*
	 * Revoke a contract sending back to provider the coin
	 */
	public static Transaction buildRevokeContract(final String providerAddress, final String userAddress, final TransactionOutput prevOut, NetworkParameters networkParameters) throws Exception {
		
		if (providerAddress == null) {
			
			throw new Exception("Provider address is null");
			
		}
		
		if (userAddress == null) {
			
			throw new Exception("User address is null");
			
		}
		
		Coin coinValue = prevOut.getValue().subtract(FEE);
		
		Coin totalCoinOut = Coin.ZERO;
		
		Coin coinDivided = Coin.valueOf(coinValue.getValue() / 2);
		
        final Transaction transaction = new Transaction(networkParameters);
        
        // provider output
        TransactionOutput outputToProvider = new TransactionOutput(networkParameters, transaction, coinDivided, Address.fromBase58(networkParameters, providerAddress));
        transaction.addOutput(outputToProvider);
        
        // user output
        TransactionOutput outputToUser = new TransactionOutput(networkParameters, transaction, coinDivided, Address.fromBase58(networkParameters, userAddress));
        transaction.addOutput(outputToUser);
        
        // add value
        totalCoinOut = totalCoinOut.add(coinValue);
        
        try {
			
			// Connect all input
        	transaction.addInput(prevOut).setScriptSig(ScriptBuilder.createInputScript(TransactionSignature.dummy()));
            	
        	return transaction;

        } catch (Exception ex) {

           throw new Exception("Exception while building revoke contract", ex);

        }

	}

}
