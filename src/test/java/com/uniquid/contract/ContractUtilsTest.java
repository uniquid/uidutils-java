package com.uniquid.contract;

import com.uniquid.blockchain.impl.InsightApiDAOImpl;
import com.uniquid.params.UniquidLitecoinRegTest;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.util.BitSet;

public class ContractUtilsTest {

    @Test
    public void test() throws Exception {

        NetworkParameters parameters = UniquidLitecoinRegTest.get();

        InsightApiDAOImpl blockChainDAOImpl = new InsightApiDAOImpl("http://40.115.103.9:3001/insight-lite-api");
        String tx = blockChainDAOImpl.retrieveRawTx("238658afbe6e61db2dd3f923f289a40f007d00cfc8672fadaac7fd4a9c01ade6 ");

        Transaction originalTransaction = parameters.getDefaultSerializer().makeTransaction(Hex.decode(tx));

        BitSet bitSet = new BitSet();
        bitSet.set(34);
        bitSet.set(35);
        bitSet.set(36);

        Transaction contract;

        contract = ContractUtils.buildAccessContract(
                null,
                "n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo",
                "musfm34J4hH2KH2SaEoiycnoDyR36d5wXT",
                originalTransaction.getOutput(3), bitSet, parameters);

        String contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001e6ad019c4afdc7aaad2f67c8cf007d000fa489f223f9d32ddb616ebeaf588623030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff030000000000000000536a4c5000000000001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0930400000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88ac80b88a00000000001976a9149d7cb191574e06f1ee5d225e90c0fb24383819c688ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB",
                null,
                "musfm34J4hH2KH2SaEoiycnoDyR36d5wXT",
                originalTransaction.getOutput(3), bitSet, parameters);

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001e6ad019c4afdc7aaad2f67c8cf007d000fa489f223f9d32ddb616ebeaf588623030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff03a0860100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ac0000000000000000536a4c5000000000001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c0c58d00000000001976a9149d7cb191574e06f1ee5d225e90c0fb24383819c688ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB",
                "n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo",
                "musfm34J4hH2KH2SaEoiycnoDyR36d5wXT",
                originalTransaction.getOutput(3), null, parameters
        );

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001e6ad019c4afdc7aaad2f67c8cf007d000fa489f223f9d32ddb616ebeaf588623030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff03a0860100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ace0930400000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88ace0318900000000001976a9149d7cb191574e06f1ee5d225e90c0fb24383819c688ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB",
                "n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo",
                "musfm34J4hH2KH2SaEoiycnoDyR36d5wXT",
                originalTransaction.getOutput(3), bitSet, parameters
        );

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));
        Assert.assertEquals(
                "0100000001e6ad019c4afdc7aaad2f67c8cf007d000fa489f223f9d32ddb616ebeaf588623030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff04a0860100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ac0000000000000000536a4c5000000000001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0930400000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88ace0318900000000001976a9149d7cb191574e06f1ee5d225e90c0fb24383819c688ac00000000",
                contractTx);


        Transaction revoke;
        try {
            revoke = ContractUtils.buildRevokeContract(
                    null,
                    "mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB",
                    originalTransaction.getOutput(2), parameters);
            Assert.fail();
        } catch (Exception e) {
            //
        }

        try {
            revoke = ContractUtils.buildRevokeContract(
                    "n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo",
                    null,
                    originalTransaction.getOutput(2), parameters);
            Assert.fail();
        } catch (Exception e) {
            //
        }

        revoke = ContractUtils.buildRevokeContract(
                "n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo",
                "mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB",
                originalTransaction.getOutput(2), parameters);

        String revokeTx = new String(Hex.encode(revoke.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001e6ad019c4afdc7aaad2f67c8cf007d000fa489f223f9d32ddb616ebeaf588623020000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff0284c50100000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88ac84c50100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ac00000000",
                revokeTx);

    }

}
