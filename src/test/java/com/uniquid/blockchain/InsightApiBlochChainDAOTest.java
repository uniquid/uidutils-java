/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.blockchain;

import com.uniquid.blockchain.exception.BlockChainException;
import com.uniquid.blockchain.impl.InsightApiDAOImpl;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collection;

public class InsightApiBlochChainDAOTest {

    @Test
    public void test() throws Exception {

        InsightApiDAOImpl blockChainDAOImpl = new InsightApiDAOImpl("http://40.115.103.9:3001/insight-lite-api");

        AddressInfo addressInfo = blockChainDAOImpl.retrieveAddressInfo("mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB");

        Assert.assertNotNull(addressInfo);
        Assert.assertEquals(addressInfo.getBalance(), 0.002);

        try {
            AddressInfo addressInfo2 = blockChainDAOImpl.retrieveAddressInfo("1Nro9WkpaKm9axmcfPVp79dAJU1Gx7VmMZ");
            Assert.fail();
        } catch (BlockChainException ex) {
            //OK expected
        }

        Collection<Utxo> utxo = blockChainDAOImpl.retrieveUtxo("mxt3rL7PSZ22oaRGzVJV3voGwJhU1PFhdB");

        Assert.assertNotNull(utxo);
        Assert.assertEquals(2, utxo.size());

        Transaction tx = blockChainDAOImpl.retrieveTransaction(utxo.iterator().next().getTxid());

        Assert.assertNotNull(tx);

        try {
            Collection<Utxo> utxo2 = blockChainDAOImpl.retrieveUtxo("1Nro9WkpaKm9axmcfPVp79dAJU1Gx7VmMZ");
            Assert.fail();

        } catch (BlockChainException ex) {
            // OK expected
        }


        Collection<Utxo> utxo3 = blockChainDAOImpl.retrieveUtxo("msSf3JrPqbPA47vLSMenge2TcRXUWCBQcY");

        Assert.assertNotNull(utxo3);
        Assert.assertEquals(2, utxo3.size());

        utxo3 = blockChainDAOImpl.retrieveUtxo("msSf3JrPqbPA47vLSMenge2TcRXUWCBQcY", 1);

        Assert.assertNotNull(utxo3);
        Assert.assertEquals(1, utxo3.size());

        String rawTx = blockChainDAOImpl.retrieveRawTx(utxo.iterator().next().getTxid());

        Assert.assertEquals("01000000015a73b9bd48b5ff3786af7d2a06a1552e36fbb3795636df5e815356e3197cb8a0020000006b483045022100ebee2c20ecb6b7c66f377ad60ff512305c70fddd54766d50f252d12c1c1179d7022074c473f6db1373c49df7d478f1d23f6f18867b713d66966a97f37e9815aa4faa0121030e2a17650023a19a50b11f76fb623fe0e2fec69af2bd06a5c9da365805a73962ffffffff02a0860100000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88aca0860100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ac00000000", rawTx);

        try {
            String rawTx2 = blockChainDAOImpl.retrieveRawTx("12345");
            Assert.fail();
        } catch (BlockChainException ex) {
            // OK expected
        }

        try {
            blockChainDAOImpl.sendTx("01000000015a73b9bd48b5ff3786af7d2a06a1552e36fbb3795636df5e815356e3197cb8a0020000006b483045022100ebee2c20ecb6b7c66f377ad60ff512305c70fddd54766d50f252d12c1c1179d7022074c473f6db1373c49df7d478f1d23f6f18867b713d66966a97f37e9815aa4faa0121030e2a17650023a19a50b11f76fb623fe0e2fec69af2bd06a5c9da365805a73962ffffffff02a0860100000000001976a914fa1804e0b3d2049116ec25cfc3d30d47f6ccb8ca88aca0860100000000001976a914be778308363d598e21a2643e9b86fbf5c781865488ac00000000");
            Assert.fail();

        } catch (BlockChainException ex) {

            // OK
        }
    }

}
