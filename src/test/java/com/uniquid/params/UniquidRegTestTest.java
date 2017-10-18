package com.uniquid.params;

import org.junit.Assert;
import org.junit.Test;

import com.uniquid.utils.IpUtils;

public class UniquidRegTestTest {
	
	@Test
	public void test() throws Exception {
		
		int[] seeds = UniquidRegTest.get().getAddrSeeds();
		
		int[] expected = new int[] {(int) 2832851252L, (int) 2547230516L, (int) 2245714228L};
		
		Assert.assertArrayEquals(expected, seeds);
		
		UniquidRegTest.get().overrideSeeds("192.168.1.2");
		
		int[] newSeeds = UniquidRegTest.get().getAddrSeeds();
		
		int[] newExpected = new int[] {(int) IpUtils.convertStringIpToLong("192.168.1.2")};
		
		Assert.assertArrayEquals(newExpected, newSeeds);
		
	}

}
