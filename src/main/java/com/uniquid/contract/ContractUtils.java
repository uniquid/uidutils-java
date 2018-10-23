package com.uniquid.contract;

import org.bitcoinj.core.*;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.ScriptBuilder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ContractUtils {

    public static final Coin FEE = Coin.COIN.divide(1000); /* 0,001 */
    public static final Coin FEE_PER_B = Coin.COIN.multiply(3).divide(1000000);	/* 0,000003 */
    public static final Coin COIN_OUTPUT = Coin.COIN.divide(1000); /* 0,001 */

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

        ArrayList<TransactionOutput> inputs = new ArrayList<>();

        inputs.add(prevOut);

        return buildAccessContract(userAddress, revokeAddress, changeAddress, inputs, bitSet, networkParameters);

    }

    /**
     * Builds an access contract
     *
     * @param userAddress the address of the user
     * @param revokeAddress the address of the revoker
     * @param changeAddress the change address
     * @param inputs a list of the {@link TransactionOutput} that will be used as inputs
     * @param bitSet the right of the contracts
     * @param networkParameters the {@link NetworkParameters} that identify the network
     * @return a {@link com.uniquid.blockchain.Transaction} that represents the Access Contract
     * @throws Exception in case a problem occurs
     */
    public static Transaction buildAccessContract(final String userAddress, final String revokeAddress, final String changeAddress, final List<TransactionOutput> inputs, BitSet bitSet, NetworkParameters networkParameters) throws Exception {

        Coin coinValue = COIN_OUTPUT;

        Coin totalCoinOut = Coin.ZERO;

        final Transaction transaction = new Transaction(networkParameters);

        if (userAddress != null) {

            // user output
            TransactionOutput outputToUser = new TransactionOutput(networkParameters, transaction, coinValue, LegacyAddress.fromBase58(networkParameters, userAddress));
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

            Coin revokeCoinOutput = coinValue.multiply(3);

            // revoke addr output
            TransactionOutput outputToRevoke = new TransactionOutput(networkParameters, transaction, revokeCoinOutput, LegacyAddress.fromBase58(networkParameters, revokeAddress));
            transaction.addOutput(outputToRevoke);

            // add value
            totalCoinOut = totalCoinOut.add(revokeCoinOutput);

        }

        // 34 Byte is the size of an output. For access we have 1 User, 1 Revoker, 1 Change
        // 150 Byte is the size of an input
        // 80 Byte is the size of the op_return
        long tSize = (34 * 3) + (150 * inputs.size()) + 92;

        Coin fee = FEE_PER_B.multiply(tSize);

        // add fee
        totalCoinOut = totalCoinOut.add(fee);

        TransactionOutput outputChange;
        try {

            final Coin change = sum(inputs).subtract(totalCoinOut);

            outputChange = new TransactionOutput(networkParameters, transaction, change, LegacyAddress.fromBase58(networkParameters, changeAddress));
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

    /**
     * Revoke a contract sending back to provider the coin
     *
     * @param providerAddress the address of the provider
     * @param userAddress the address of the user
     * @param prevOut the {@link TransactionOutput} that will be used as inputs
     * @param networkParameters the {@link NetworkParameters} that identify the network
     * @return a {@link com.uniquid.blockchain.Transaction} that represents the Revoke Contract
     * @throws Exception in case a problem occurs
     */
    public static Transaction buildRevokeContract(final String providerAddress, final String userAddress, final TransactionOutput prevOut, NetworkParameters networkParameters) throws Exception {

        if (providerAddress == null) {

            throw new Exception("Provider address is null");

        }

        if (userAddress == null) {

            throw new Exception("User address is null");

        }


        Coin coinValue = prevOut.getValue().subtract(FEE);

        Coin coinDivided = Coin.valueOf(coinValue.getValue() / 2);

        final Transaction transaction = new Transaction(networkParameters);

        // provider output
        TransactionOutput outputToProvider = new TransactionOutput(networkParameters, transaction, coinDivided, LegacyAddress.fromBase58(networkParameters, providerAddress));
        transaction.addOutput(outputToProvider);

        // user output
        TransactionOutput outputToUser = new TransactionOutput(networkParameters, transaction, coinDivided, LegacyAddress.fromBase58(networkParameters, userAddress));
        transaction.addOutput(outputToUser);

        try {

            // Connect all input
            transaction.addInput(prevOut).setScriptSig(ScriptBuilder.createInputScript(TransactionSignature.dummy()));

            return transaction;

        } catch (Exception ex) {

            throw new Exception("Exception while building revoke contract", ex);

        }

    }

}
