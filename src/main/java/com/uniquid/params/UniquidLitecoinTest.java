package com.uniquid.params;

import java.math.BigInteger;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestLitecoinNetParams;
import org.bitcoinj.params.TestNet2Params;

public class UniquidLitecoinTest extends TestLitecoinNetParams {
	
//	private static final BigInteger MAX_TARGET = new BigInteger("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16);
	
	public static final String ID_UNIQUIDLITEREGTEST = "com.uniquid.litecointest";
	public static final String PAYMENT_PROTOCOL_ID_UNIQUIDLITEREGTEST = "uniquidlitecointest";
	
	private UniquidLitecoinTest() {
		super();
		
        id = ID_UNIQUIDLITEREGTEST;

        majorityEnforceBlockUpgrade = TestNet2Params.TESTNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = TestNet2Params.TESTNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = TestNet2Params.TESTNET_MAJORITY_WINDOW;
	}
	
	private static UniquidLitecoinTest instance;
	
	public static synchronized UniquidLitecoinTest get() {
		
		if (instance == null) {
		
			instance = new UniquidLitecoinTest();

			NetworkParameters.addCustomNetworkParameter(ID_UNIQUIDLITEREGTEST, instance);
			NetworkParameters.addCustomPaymentProtocol(ID_UNIQUIDLITEREGTEST, instance);
		}
		
		return instance;
	}

	@Override
	public String getPaymentProtocolId() {
		return PAYMENT_PROTOCOL_ID_UNIQUIDLITEREGTEST;
	}

}
