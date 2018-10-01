package com.uniquid.params;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.RegTestLitecoinNetParams;
import org.bitcoinj.params.TestLitecoinNetParams;

import com.uniquid.utils.IpUtils;

public class UniquidLitecoinRegTest extends RegTestLitecoinNetParams {
	
	public static final String ID_UNIQUIDLITEREGTEST = "com.uniquid.params.UniquidLitecoinRegTest";
	public static final String PAYMENT_PROTOCOL_ID_UNIQUIDLITEREGTEST = "uniquidlitecoinregtest";
	
	// By default use test peers: /40.115.10.153, /40.115.103.9, /40.115.9.216
	private static int[] SEEDS = new int[] {(int) 2567598888L, (int) 157774632L, (int) 3624497960L};
	
	private UniquidLitecoinRegTest() {
		super();
		
		port = 19000;
        id = ID_UNIQUIDLITEREGTEST;
        
        addrSeeds = SEEDS;
        
	}
	
	private static UniquidLitecoinRegTest instance;
	
	public static synchronized UniquidLitecoinRegTest get() {
		
		if (instance == null) {
		
			instance = new UniquidLitecoinRegTest();

			NetworkParameters.addCustomNetworkParameter(ID_UNIQUIDLITEREGTEST, instance);
			NetworkParameters.addCustomPaymentProtocol(PAYMENT_PROTOCOL_ID_UNIQUIDLITEREGTEST, instance);
		}
		
		return instance;
	}

	@Override
	public String getPaymentProtocolId() {
		return PAYMENT_PROTOCOL_ID_UNIQUIDLITEREGTEST;
	}
	
	/**
	 * Allow to override default peers used to access to blockchain
	 * 
	 * @param peers a String representing peers separated by semicolon
	 * @throws Exception in case a problem during parsing occurs
	 */
	public void overridePeers(String peers) throws Exception {
		List<Integer> list = new ArrayList<Integer>();

		if (peers == null || peers.isEmpty())
			throw new Exception("Invalid seeds");
		
		StringTokenizer stringTokenizer = new StringTokenizer(peers, ";");
		
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
