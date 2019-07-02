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

    private static int[] SEEDS = new int[] {
            (int) 3877951583L,    // 95.216.36.231           /LitecoinCore:0.16.3/
            (int) 3896695184L,    // 144.217.66.232:19335    /LitecoinCore:0.16.3/
            (int) 1898204263L,    // 103.76.36.113:19335     /LitecoinCore:0.17.1(litecoin-testnet)/
            (int) 2174535050L,    // 138.197.156.129:19335   /LitecoinCore:0.16.3/
            (int) 473386846L,     // 94.79.55.28:19335       /LitecoinCore:0.16.3/
            //(int) 1522814096L,    // 144.76.196.90:38940     /LitecoinCore:0.16.3/
            (int) 732187792L,     // 144.76.164.43:19335     /LitecoinCore:0.13.2(bitcore)/
            //(int) 340450082L,     // 34.219.74.20:53494      /LitecoinCore:0.15.1/
            //(int) 3291727061L,    // 213.196.51.196:33562    /LitecoinCore:0.15.1/
            //(int) 4190390836L,    // 52.74.196.249:54922     /LitecoinCore:0.16.2/
            //(int) 1168091446L,    // 54.169.159.69:44294     /LitecoinCore:0.16.3/
            //(int) 795849426L,     // 210.178.111.47:47566    /LitecoinCore:0.13.2(bitcore)/
            (int) 1479887667L,    // 51.75.53.88:19335       /LitecoinCore:0.17.1/
            //(int) 4067637517L,    // 13.57.115.242:58814     /LitecoinCore:0.16.3/
            //(int) 557480973L,     // 13.124.58.33:5722       /LitecoinCore:0.16.3/
            (int) 1699783371L,    // 203.162.80.101:19335    /LitecoinCore:0.16.3/
            //(int) 1702469155L,    // 35.158.121.101:58770    /LitecoinCore:0.15.1/
            //(int) 1020206126L,    // 46.28.207.60:33243      /LitecoinCore:0.16.0/
            //(int) 3851785270L,    // 54.148.149.229:54244    /bcoin:v1.0.0-beta.15/
            //(int) 4238491985L,    // 81.65.162.252:51997     /LitecoinCore:0.14.2/
            //(int) 450140883L,     // 211.154.212.26:37260    /LitecoinCore:0.17.1/
            //(int) 4050631867L,    // 187.188.111.241:56552   /LitecoinCore:0.14.2/
            //(int) 2634965607L,    // 103.98.14.157:59106     /LitecoinCore:0.16.3/
            //(int) 61599829L,      // 85.240.171.3:54236      /LitecoinCore:0.16.3/
            (int) 264367818L,     // 202.238.193.15:19335    /LitecoinCore:0.16.3/
            //(int) 2999863774L,    // 222.73.206.178:24496    /LitecoinCore:0.16.3/
            //(int) 2068977003L,    // 107.21.82.123:57184     /LitecoinCore:0.17.1/
            //(int) 1960648889L,    // 185.32.221.116:37516    /LitecoinCore:0.17.1/
            //(int) 3904005031L,    // 167.99.178.232:49242    /LitecoinCore:0.16.3/
            //(int) 2103626564L,    // 68.203.98.125:52121     /LitecoinCore:0.16.3/
            //(int) 521099377L,     // 113.88.15.31:48078      /LitecoinCore:0.16.3/
            //(int) 4023652565L,    // 213.16.212.239:49439    /LitecoinCore:0.15.1/
            //(int) 1143260082L,    // 178.195.36.68:54260     /LitecoinCore:0.16.3/
            //(int) 1125509411L,    // 35.233.21.67:1102       /LitecoinCore:0.16.3/
            //(int) 2943918966L,    // 118.163.120.175:4718    /LitecoinCore:0.16.3/
            //(int) 3416441181L,    // 93.193.162.203:3627     /LitecoinCore:0.15.1/
            //(int) 3971232096L,    // 96.49.180.236:60464     /LitecoinCore:0.16.3/
            //(int) 3674751391L,    // 159.65.8.219:44408      /LitecoinCore:0.13.2(bitcore)/
            //(int) 3920708790L,    // 182.68.177.233:54187    /LitecoinCore:0.16.3/
            //(int) 1951887127L,    // 23.111.87.116:49462     /LitecoinCore:0.17.1/
    };


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
