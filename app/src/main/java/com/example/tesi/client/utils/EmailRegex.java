package com.example.tesi.client.utils;

import java.util.regex.Pattern;

public class EmailRegex {
	private static final String regex="[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}";
	public static boolean match(String email) {
		return Pattern.matches(regex, email);
	}
}
