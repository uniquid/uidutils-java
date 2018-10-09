package com.uniquid.utils;

import com.google.common.collect.ImmutableList;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public class AddressUtils {
	
	public static Address getUserExternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final long userIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(1/*user*/, false),
					new ChildNumber(0/*external*/, false), new ChildNumber((int)userIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(1/*user*/, false),
					new ChildNumber(0/*external*/, false), new ChildNumber((int)userIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return LegacyAddress.fromKey(networkParameters, imprintingKey);
		
	}
	
	public static Address getUserInternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final long userIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(1/*user*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber((int)userIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(1/*user*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber((int)userIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return LegacyAddress.fromKey(networkParameters, imprintingKey);
		
	}
	
	public static Address getProviderExternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final long providerIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(0/*provider*/, false),
					new ChildNumber(/*external*/0, false), new ChildNumber((int)providerIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(0/*provider*/, false),
					new ChildNumber(/*external*/0, false), new ChildNumber((int)providerIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return LegacyAddress.fromKey(networkParameters, imprintingKey);
		
	}
	
	public static Address getProviderInternalAddress(final String pubKey, final NetworkParameters networkParameters,
			final long providerIndex) {
		
		DeterministicKey deterministicKey = DeterministicKey.deserializeB58(pubKey, networkParameters);
		DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(deterministicKey);
		
		List<ChildNumber> child = null;
		if (deterministicKey.getDepth() == 2) {
			
			/* M/44'/0' node tpub */
			child = ImmutableList.of(new ChildNumber(0, false), new ChildNumber(0/*provider*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber((int)providerIndex, false));
		
		} else if (deterministicKey.getDepth() == 3) {
			
			/* M/44'/0'/X context tpub */
			child = ImmutableList.of(new ChildNumber(0/*provider*/, false),
					new ChildNumber(1/*internal*/, false), new ChildNumber((int)providerIndex, false));
		}

		DeterministicKey imprintingKey = deterministicHierarchy.get(child, true, true);
		return LegacyAddress.fromKey(networkParameters, imprintingKey);
		
	}

}
