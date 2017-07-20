package com.uniquid.utils;

import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;

import com.google.common.collect.ImmutableList;

public class AddressUtils {
	
	public static Address getNextUserExternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final int userIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(1/*user*/, false),
					new ChildNumber(0/*external*/, false), new ChildNumber(userIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(1/*user*/, false),
					new ChildNumber(0/*external*/, false), new ChildNumber(userIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return imprintingKey.toAddress(networkParameters);
		
	}
	
	public static Address getNextUserInternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final int userIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(1/*user*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber(userIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(1/*user*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber(userIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return imprintingKey.toAddress(networkParameters);
		
	}
	
	public static Address getNextProviderExternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final int providerIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(0/*provider*/, false),
					new ChildNumber(/*external*/0, false), new ChildNumber(providerIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(0/*provider*/, false),
					new ChildNumber(/*external*/0, false), new ChildNumber(providerIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return imprintingKey.toAddress(networkParameters);
		
	}
	
	public static Address getNextProviderInternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final int providerIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(0/*provider*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber(providerIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(0/*provider*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber(providerIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return imprintingKey.toAddress(networkParameters);
		
	}

}
