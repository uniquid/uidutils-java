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
        String tx = blockChainDAOImpl.retrieveRawTx("3d09e1cb71d13cd14fc3958d6402f0e08afeb25e1dba7ac853cdbd9b9ec8abad ");

        Transaction originalTransaction = parameters.getDefaultSerializer().makeTransaction(Hex.decode(tx));

        BitSet bitSet = new BitSet();
        bitSet.set(34);
        bitSet.set(35);
        bitSet.set(36);

        Transaction contract;

        contract = ContractUtils.buildAccessContract(
                null,
                "mjF7jmRdMr5zNbUqGgDWEtNEk22RtHQLSc",
                "mkHdDJq2Veu9QvLijEAxcA46r2JZwj6pZb",
                originalTransaction.getOutput(3), bitSet, parameters);

        String contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001adabc89e9bbdcd53c87aba1d5eb2fe8ae0f002648d95c34fd13cd171cbe1093d030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff030000000000000000536a4c5000000000001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0930400000000001976a91428e1bb0aef0e6da3c8c7973051764ae94db049fe88ace0da8a00000000001976a91434534aef15c4a052a12c5d5ce21835635f7f033b88ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mjwodaJ9qNSgcbBRjHz6NnoTSSm4qYa1BZ",
                null,
                "mkHdDJq2Veu9QvLijEAxcA46r2JZwj6pZb",
                originalTransaction.getOutput(3), bitSet, parameters);

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001adabc89e9bbdcd53c87aba1d5eb2fe8ae0f002648d95c34fd13cd171cbe1093d030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff03a0860100000000001976a9143093ab996c2f4af5262eeddc755880445fcd667a88ac0000000000000000536a4c5000000000001c000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000020e88d00000000001976a91434534aef15c4a052a12c5d5ce21835635f7f033b88ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mjwodaJ9qNSgcbBRjHz6NnoTSSm4qYa1BZ",
                "mjF7jmRdMr5zNbUqGgDWEtNEk22RtHQLSc",
                "mkHdDJq2Veu9QvLijEAxcA46r2JZwj6pZb",
                originalTransaction.getOutput(3), null, parameters
        );

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001adabc89e9bbdcd53c87aba1d5eb2fe8ae0f002648d95c34fd13cd171cbe1093d030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff03a0860100000000001976a9143093ab996c2f4af5262eeddc755880445fcd667a88ace0930400000000001976a91428e1bb0aef0e6da3c8c7973051764ae94db049fe88ac40548900000000001976a91434534aef15c4a052a12c5d5ce21835635f7f033b88ac00000000",
                contractTx);

        contract = ContractUtils.buildAccessContract(
                "mjwodaJ9qNSgcbBRjHz6NnoTSSm4qYa1BZ",
                "mjF7jmRdMr5zNbUqGgDWEtNEk22RtHQLSc",
                "mkHdDJq2Veu9QvLijEAxcA46r2JZwj6pZb",
                originalTransaction.getOutput(3), bitSet, parameters
        );

        contractTx = new String(Hex.encode(contract.bitcoinSerialize()));
        Assert.assertEquals(
                "0100000001adabc89e9bbdcd53c87aba1d5eb2fe8ae0f002648d95c34fd13cd171cbe1093d030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff04a0860100000000001976a9143093ab996c2f4af5262eeddc755880445fcd667a88ac0000000000000000536a4c5000000000001c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0930400000000001976a91428e1bb0aef0e6da3c8c7973051764ae94db049fe88ac40548900000000001976a91434534aef15c4a052a12c5d5ce21835635f7f033b88ac00000000",
                contractTx);


        Transaction revoke;
        try {
            revoke = ContractUtils.buildRevokeContract(
                    null,
                    "mjwodaJ9qNSgcbBRjHz6NnoTSSm4qYa1BZ",
                    originalTransaction.getOutput(2), parameters);
            Assert.fail();
        } catch (Exception e) {
            //
        }

        try {
            revoke = ContractUtils.buildRevokeContract(
                    "mjF7jmRdMr5zNbUqGgDWEtNEk22RtHQLSc",
                    null,
                    originalTransaction.getOutput(2), parameters);
            Assert.fail();
        } catch (Exception e) {
            //
        }

        revoke = ContractUtils.buildRevokeContract(
                "mjF7jmRdMr5zNbUqGgDWEtNEk22RtHQLSc",
                "mjwodaJ9qNSgcbBRjHz6NnoTSSm4qYa1BZ",
                originalTransaction.getOutput(2), parameters);

        String revokeTx = new String(Hex.encode(revoke.bitcoinSerialize()));

        Assert.assertEquals(
                "0100000001adabc89e9bbdcd53c87aba1d5eb2fe8ae0f002648d95c34fd13cd171cbe1093d020000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff02a0860100000000001976a91428e1bb0aef0e6da3c8c7973051764ae94db049fe88aca0860100000000001976a9143093ab996c2f4af5262eeddc755880445fcd667a88ac00000000",
                revokeTx);

    }

}
