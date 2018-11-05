/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.params;

import com.uniquid.utils.IpUtils;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.RegTestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Network parameters for Uniquid Internal RegTestNet
 */
public class UniquidRegTest extends RegTestParams {

    // By default use test peers: /52.225.217.168, /52.167.211.151, /52.225.218.133
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
            NetworkParameters.addCustomPaymentProtocol(PAYMENT_PROTOCOL_ID_UNIQUIDREGTEST, instance);
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
     * @param peers a String representing peers separated by semicolon
     * @throws Exception in case a problem during parsing occurs
     */
    public void overridePeers(String peers) throws Exception {
        List<Integer> list = new ArrayList<>();

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
            arr[i] = list.get(i);
        }

        SEEDS = arr;

        addrSeeds = SEEDS;

    }

}