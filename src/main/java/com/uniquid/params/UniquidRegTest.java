package com.uniquid.params;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.RegTestParams;

import com.uniquid.utils.IpUtils;

/**
 * Network parameters for Uniquid Internal RegTestNet
 */
public class UniquidRegTest extends RegTestParams {
	
	// By default use test peers
	private static int[] SEEDS = new int[] {(int) 2832851252L, (int) 2547230516L, (int) 2245714228L};
	
	public static final String ID_UNIQUIDREGTEST = "com.uniquid.regtest";
	public static final String PAYMENT_PROTOCOL_ID_UNIQUIDREGTEST = "uniquidregtest";

	private UniquidRegTest() {

		port = 19000;
		id = ID_UNIQUIDREGTEST;

		addrSeeds = SEEDS;
		
	}
	
	private static UniquidRegTest instance;
	
	public static synchronized UniquidRegTest get() {
		
		if (instance == null) {
		
			instance = new UniquidRegTest();

			NetworkParameters.addCustomNetworkParameter(ID_UNIQUIDREGTEST, instance);
			NetworkParameters.addCustomPaymentProtocol(ID_UNIQUIDREGTEST, instance);
		}
		
		return instance;
	}
	
	@Override
	public String getPaymentProtocolId() {
		return PAYMENT_PROTOCOL_ID_UNIQUIDREGTEST;
	}
	
	/**
	 * Allow to override default peers used to access to blockchain
	 * 
	 * @param seeds a String representing peers separated by semicolon
	 * @throws Exception in case a problem during parsing occurs
	 */
	public void overrideSeeds(String seeds) throws Exception {
		List<Integer> list = new ArrayList<Integer>();

		if (seeds == null || seeds.isEmpty())
			throw new Exception("Invalid seeds");
		
		StringTokenizer stringTokenizer = new StringTokenizer(seeds, ";");
		
		if (!(stringTokenizer.countTokens() > 0)) {
			throw new Exception("Invalid seeds count");
		}
		
		while (stringTokenizer.hasMoreTokens()) {
			
			String token = stringTokenizer.nextToken();
			
			token = token.trim();
			
			int ip = (int) IpUtils.convertStringIpToLong(token);

			list.add(ip);
			
		}
		
		int[] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = (int) list.get(i);
		}
		
		SEEDS = arr;
		
		addrSeeds = SEEDS;

	}
	
}