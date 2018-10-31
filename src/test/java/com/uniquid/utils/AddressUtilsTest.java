package com.uniquid.utils;

import com.uniquid.params.UniquidLitecoinRegTest;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.junit.Test;

import static com.uniquid.utils.AddressUtils.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Beatrice Formai
 */
public class AddressUtilsTest {

    @Test
    public void testAddress() {

        String publicKey = "tpubDASL2owci3wRQ9n2inv3UhSoyep91aNvdEbRUezppZc8Zq1hLHQg8S5Gfd4nm7TtJu5iv7XFTt4cnnZAvwYyi8q5uWnf2MDLJdyNrfu54Ad";
        NetworkParameters parameters = UniquidLitecoinRegTest.get();

        String address = ((LegacyAddress) getUserExternalAddress(publicKey, parameters, 1)).toBase58();
        assertEquals("n4KKztGVmQ6sQ462AxPPfVU9Gk4HLY6bdo", address);

        address = ((LegacyAddress) getUserInternalAddress(publicKey, parameters, 1)).toBase58();
        assertEquals("msxodmnickkuT4VvAZn8vZ7viCpbrDnHAL", address);

        address = ((LegacyAddress) getProviderExternalAddress(publicKey, parameters, 1)).toBase58();
        assertEquals("mkRK2eYYEJTTwwYm53mhNtyvqZi69fH6zN", address);

        address = ((LegacyAddress) getProviderInternalAddress(publicKey, parameters, 1)).toBase58();
        assertEquals("musfm34J4hH2KH2SaEoiycnoDyR36d5wXT", address);

    }

}