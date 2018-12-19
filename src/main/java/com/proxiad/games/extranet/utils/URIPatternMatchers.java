package com.proxiad.games.extranet.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIPatternMatchers {


	public static boolean matches(String uri, String uriPattern) {
		String regexPattern = "^"
				+ uriPattern.replaceAll("\\{[a-zA-Z0-9]*\\}", "[a-zA-Z0-9]*")
							.replaceAll("\\/", "\\\\/")
				+ "$";
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(uri);
		return matcher.find();
	}

}
