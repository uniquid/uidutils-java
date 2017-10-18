package com.uniquid.contract;

import java.util.BitSet;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.TestNet3Params;
import org.junit.Assert;
import org.junit.Test;

import com.subgraph.orchid.encoders.Hex;

public class ContractUtilsTest {

	@Test
	public void test() throws Exception {
		
		String tx = "0100000001b6c3475d919c676da5b6bd8016a8746c3b54fbd98a32379f724305e151abe6b5000000006b483045022100fd22b75a90d5cdb7d32e0cba9eb2b61f51caa13e83b51d1a7152815d18fd0f9e0220359993b09563ba57d9e1f7863bb7aeb581c500c1772de27acab2cfcb54811d55012102a71f7a7adee0ec0710f896e8b26e8db4ac602538dffaf0d7fd4b1e0068d035afffffffff0418730100000000001976a914d141f0e47bc3355260bf701fce59f67343b505fa88ac0000000000000000536a4c50000000004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000018730100000000001976a914d141f0e47bc3355260bf701fce59f67343b505fa88ac88480c00000000001976a91455524f2f6709abe0e640a77ada62ce0a3c80ff4b88ac00000000";

		Transaction originalTransaction = TestNet3Params.get().getDefaultSerializer().makeTransaction(Hex.decode(tx));

		BitSet bitSet = new BitSet();
		bitSet.set(30);
		bitSet.set(33);

		Transaction contract = null;
		
		contract = ContractUtils.buildAccessContract(
				null,
				"mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt",
				"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
				originalTransaction.getOutput(3), bitSet, TestNet3Params.get());
		
		String contractTx = new String(Hex.encode(contract.bitcoinSerialize()));
		
		Assert.assertEquals(
				"0100000001f311e9e1501f941b0979b049787ee1c19da31305af6ff77ce07f15a1d5abe7f7030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff030000000000000000536a4c50000000004002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000030750000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac48ac0b00000000001976a914e62d2a6f9eb5d27b862aa552cefafdcec2681c9588ac00000000",
				contractTx);
		
		contract = ContractUtils.buildAccessContract(
				"mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt",
				"mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt",
				"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
				originalTransaction.getOutput(3), null, TestNet3Params.get());
		
		contractTx = new String(Hex.encode(contract.bitcoinSerialize()));
		
		Assert.assertEquals(
				"0100000001f311e9e1501f941b0979b049787ee1c19da31305af6ff77ce07f15a1d5abe7f7030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff0310270000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac30750000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac38850b00000000001976a914e62d2a6f9eb5d27b862aa552cefafdcec2681c9588ac00000000",
				contractTx);
		
		contract = ContractUtils.buildAccessContract("mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt", null,
				"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
				originalTransaction.getOutput(3), bitSet, TestNet3Params.get());

		contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

		Assert.assertEquals(
				"0100000001f311e9e1501f941b0979b049787ee1c19da31305af6ff77ce07f15a1d5abe7f7030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff0310270000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac0000000000000000536a4c50000000004002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000068fa0b00000000001976a914e62d2a6f9eb5d27b862aa552cefafdcec2681c9588ac00000000",
				contractTx);
		
		contract = ContractUtils.buildAccessContract("mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt", "mnec4hDq98sd9dS1wXwjMLfoscuyV2hcWt",
				"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
				originalTransaction.getOutput(3), bitSet, TestNet3Params.get());

		contractTx = new String(Hex.encode(contract.bitcoinSerialize()));

		Assert.assertEquals(
				"0100000001f311e9e1501f941b0979b049787ee1c19da31305af6ff77ce07f15a1d5abe7f7030000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff0410270000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac0000000000000000536a4c50000000004002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000030750000000000001976a9144e3b521c391f94a914018dec3a20b1f5194beea888ac38850b00000000001976a914e62d2a6f9eb5d27b862aa552cefafdcec2681c9588ac00000000",
				contractTx);

		Transaction revoke = null;
		try {
			revoke = ContractUtils.buildRevokeContract(
					null,
					"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
					originalTransaction.getOutput(2), TestNet3Params.get());
			Assert.fail();
		} catch (Exception e) {
			//
		}
		
		try {
			revoke = ContractUtils.buildRevokeContract(
					"mggtDLwsHpWPB1Y7tKh9G665jJgLUbGiQQ",
					null,
					originalTransaction.getOutput(2), TestNet3Params.get());
			Assert.fail();
		} catch (Exception e) {
			//
		}
		
		revoke = ContractUtils.buildRevokeContract(
				"mggtDLwsHpWPB1Y7tKh9G665jJgLUbGiQQ",
				"n2W1pM7241TfvpCqgiT8TG4xS1VCb1yQum",
				originalTransaction.getOutput(2), TestNet3Params.get());

		String revokeTx = new String(Hex.encode(revoke.bitcoinSerialize()));

		Assert.assertEquals(
				"0100000001f311e9e1501f941b0979b049787ee1c19da31305af6ff77ce07f15a1d5abe7f7020000004847304402207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a002207fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a001ffffffff0204a60000000000001976a9140cd8e0eb6259627c4cdae4b03166bc4e9041947688ac04a60000000000001976a914e62d2a6f9eb5d27b862aa552cefafdcec2681c9588ac00000000",
				revokeTx);

	}

}
