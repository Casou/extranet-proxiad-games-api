package com.proxiad.games.extranet.utils;

import java.security.SecureRandom;

public class SecurityUtils {

	public static String generateToken() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return bytes.toString();
	}

}
