package com.uniquid.utils;

import org.spongycastle.util.Arrays;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtils {

	/**
	 * Convert a string representing an IP address to an integer
	 * @param ip the IP address to convert
	 * @return the {@link Long} representation of the given IP address
	 * @throws Exception if the IP is not a valid IP address
	 */
	public static long convertStringIpToLong(String ip) throws Exception {

		Pattern pattern = Pattern.compile(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher matcher = pattern.matcher(ip);

		// if it's an ip@
		if (matcher.matches()) {

			StringTokenizer ipTokenizer = new StringTokenizer(ip, ".");

			byte byte0 = (byte) Integer.parseInt(ipTokenizer.nextToken());
			byte byte1 = (byte) Integer.parseInt(ipTokenizer.nextToken());
			byte byte2 = (byte) Integer.parseInt(ipTokenizer.nextToken());
			byte byte3 = (byte) Integer.parseInt(ipTokenizer.nextToken());

			return covertByteAddressToLong(new byte[] {byte0, byte1, byte2, byte3});

		} else {

			throw new Exception("String is not a valid ip address!");

		}

	}

	/**
	 * Convert an integer to an Inetaddress
	 * @param seed the {@link Integer} representation of the IP address
	 * @return the {@link InetAddress} representation of the given IP address
	 * @throws UnknownHostException if IP address is of illegal length
	 */
	protected static InetAddress convertIntToInetAddress(int seed) throws UnknownHostException {
		byte[] v4addr = new byte[4];
		v4addr[0] = (byte) (0xFF & (seed));
		v4addr[1] = (byte) (0xFF & (seed >> 8));
		v4addr[2] = (byte) (0xFF & (seed >> 16));
		v4addr[3] = (byte) (0xFF & (seed >> 24));
		return InetAddress.getByAddress(v4addr);
	}

	/**
	 * Convert an array of bytes to an integer
	 * @param byteAddress the IP address to convert
	 * @return the IP address in {@link Long} format
	 * @throws Exception in case a problem occurs
	 */
	protected static long covertByteAddressToLong(byte[] byteAddress) throws Exception {

		byte[] addressAsBytesArray = Arrays.reverse(byteAddress);

		long result = 0;
		for (byte b : addressAsBytesArray) {
			result = result << 8 | (b & 0xFF);
		}

		return result;

	}

	/**
	 * Convert an Inetaddress to an integer
	 * @param inetAddress the {@link InetAddress} to convert
	 * @return the address in a {@link Long} format
	 * @throws Exception in case a problem occurs
	 */
	protected static long covertInetAddressToLong(InetAddress inetAddress) throws Exception {

		byte[] addressAsBytesArray = inetAddress.getAddress();

		return covertByteAddressToLong(addressAsBytesArray);

	}

}
