package com.uniquid.blockchain;

import java.util.Collection;

import org.junit.Test;

import com.uniquid.blockchain.AddressInfo;
import com.uniquid.blockchain.Utxo;
import com.uniquid.blockchain.exception.BlockChainException;
import com.uniquid.blockchain.impl.InsightApiDAOImpl;

import junit.framework.Assert;

public class InsightApiBlochChainDAOTest {

	@Test
	public void test() throws Exception {

		InsightApiDAOImpl blockChainDAOImpl = new InsightApiDAOImpl("http://explorer.uniquid.co:3001");

		AddressInfo addressInfo = blockChainDAOImpl.retrieveAddressInfo("moJ6LK1BZTvLPhA1XFefMmKCH3YGrdSegm");
		
		Assert.assertNotNull(addressInfo);
		Assert.assertEquals(addressInfo.getBalance(), 805000);
		
		Collection<Utxo> utxo = blockChainDAOImpl.retrieveUtxo("moJ6LK1BZTvLPhA1XFefMmKCH3YGrdSegm");

		Assert.assertNotNull(utxo);
		Assert.assertEquals(1, utxo.size());
		
		String rawTx = blockChainDAOImpl.retrieveRawTx(utxo.iterator().next().getTxid());
		
		Assert.assertEquals("0100000001b6c3475d919c676da5b6bd8016a8746c3b54fbd98a32379f724305e151abe6b5000000006b483045022100fd22b75a90d5cdb7d32e0cba9eb2b61f51caa13e83b51d1a7152815d18fd0f9e0220359993b09563ba57d9e1f7863bb7aeb581c500c1772de27acab2cfcb54811d55012102a71f7a7adee0ec0710f896e8b26e8db4ac602538dffaf0d7fd4b1e0068d035afffffffff0418730100000000001976a914d141f0e47bc3355260bf701fce59f67343b505fa88ac0000000000000000536a4c50000000004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000018730100000000001976a914d141f0e47bc3355260bf701fce59f67343b505fa88ac88480c00000000001976a91455524f2f6709abe0e640a77ada62ce0a3c80ff4b88ac00000000", rawTx);
		
		try {
			blockChainDAOImpl.sendTx("01000000018776b92aa383fd6035bf29491989c95804ba97c2407386981e6cc70241b7f95e000000006a47304402204266e7993a4e1a582571782cd55614be3acf7c4370778aa474fb6cb332aa9fe20220708e9c70e2999611c5d5683dd5132de7a841b0ec8a1f20bd7769dc39d1f0ac700121027b65d3df84fed18aebecd91b265b2eac7d9827b28858fd23164e2b6d3ace9f71ffffffff0418730100000000001976a91485bbd7f6f560a54ef89a66e513c3b1f55cfd86aa88ac0000000000000000536a4c50000000004000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000018730100000000001976a91485bbd7f6f560a54ef89a66e513c3b1f55cfd86aa88ac88480c00000000001976a9141abedcd67a1fe806cd9458c614538be97b90f0ea88ac00000000");
			Assert.fail();
			
		} catch (BlockChainException ex) {
			
			// OK
		}
	}

}
