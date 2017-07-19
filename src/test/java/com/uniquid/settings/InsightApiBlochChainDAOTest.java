package com.uniquid.settings;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.uniquid.blockchain.InsightApiBlockChainDAOImpl;
import com.uniquid.blockchain.Utxo;

public class InsightApiBlochChainDAOTest {

	@Test
	public void test() throws Exception {

		InsightApiBlockChainDAOImpl blockChainDAOImpl = new InsightApiBlockChainDAOImpl("explorer.uniquid.co:3001");

		Collection<Utxo> utxo = blockChainDAOImpl.retrieveUtxo("moJ6LK1BZTvLPhA1XFefMmKCH3YGrdSegm");

		Assert.assertNotNull(utxo);
		Assert.assertEquals(1, utxo.size());
	}

}
