package com.uniquid.settings;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.uniquid.blockchain.Utxo;
import com.uniquid.blockchain.impl.InsightApiDAOImpl;

public class InsightApiBlochChainDAOTest {

	@Test
	public void test() throws Exception {

		InsightApiDAOImpl blockChainDAOImpl = new InsightApiDAOImpl("explorer.uniquid.co:3001");

		Collection<Utxo> utxo = blockChainDAOImpl.retrieveUtxo("moJ6LK1BZTvLPhA1XFefMmKCH3YGrdSegm");

		Assert.assertNotNull(utxo);
		Assert.assertEquals(1, utxo.size());
	}

}
