package com.uniquid.utils;

import java.security.SecureRandom;

public class StringUtils {

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String getRandomName(int len) {

		SecureRandom random = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(random.nextInt(AB.length())));
		}

		return sb.toString();

	}

}
