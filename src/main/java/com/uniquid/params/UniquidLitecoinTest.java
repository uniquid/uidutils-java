/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.params;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestLitecoinNetParams;
import org.bitcoinj.params.TestNet3Params;

public class UniquidLitecoinTest extends TestLitecoinNetParams {

    public static final String ID_UNIQUIDLITETEST = "com.uniquid.litecointest";
    public static final String PAYMENT_PROTOCOL_ID_UNIQUIDLITETEST = "uniquidlitecointest";

    // 95.216.36.231 - 78.47.24.45 - 142.93.73.233 - 45.33.107.114 - 5.196.127.117 - 80.235.52.142 - 89.39.104.167
    private static int[] SEEDS = new int[] {(int) 3877951583L, (int) 756559694L, (int) 3913899406L, (int) 1919623469L,
            (int) 1971307525L, (int) 2385832784L, (int) 2808620889L};

    private UniquidLitecoinTest() {
        super();

        id = ID_UNIQUIDLITETEST;

        dnsSeeds = null;
        addrSeeds = SEEDS;

        majorityEnforceBlockUpgrade = TestNet3Params.TESTNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = TestNet3Params.TESTNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = TestNet3Params.TESTNET_MAJORITY_WINDOW;
    }

    private static UniquidLitecoinTest instance;

    public static synchronized UniquidLitecoinTest get() {

        if (instance == null) {

            instance = new UniquidLitecoinTest();

            NetworkParameters.addCustomNetworkParameter(ID_UNIQUIDLITETEST, instance);
            NetworkParameters.addCustomPaymentProtocol(PAYMENT_PROTOCOL_ID_UNIQUIDLITETEST, instance);
        }

        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_UNIQUIDLITETEST;
    }

}
