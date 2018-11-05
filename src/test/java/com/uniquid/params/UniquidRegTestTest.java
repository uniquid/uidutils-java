/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.params;

import com.uniquid.utils.IpUtils;
import org.junit.Assert;
import org.junit.Test;

public class UniquidRegTestTest {

    @Test
    public void test() throws Exception {

        int[] seeds = UniquidRegTest.get().getAddrSeeds();

        int[] expected = new int[] {(int) 2832851252L, (int) 2547230516L, (int) 2245714228L};

        Assert.assertArrayEquals(expected, seeds);

        UniquidRegTest.get().overridePeers("192.168.1.2");

        int[] newSeeds = UniquidRegTest.get().getAddrSeeds();

        int[] newExpected = new int[] {(int) IpUtils.convertStringIpToLong("192.168.1.2")};

        Assert.assertArrayEquals(newExpected, newSeeds);

    }

}
