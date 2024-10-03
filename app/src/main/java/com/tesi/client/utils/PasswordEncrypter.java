package com.tesi.client.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordEncrypter {
	public static String encrypt(String password) {
		StringBuilder encryptedPassword = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] message=md.digest(password.getBytes());

			for (byte b : message)
				encryptedPassword.append(String.format("%02x", b));

			return encryptedPassword.toString();
		} catch (NoSuchAlgorithmException e) {
			Logger.getLogger("global").log(Level.INFO, e.getMessage());
			return null;
		}
	}
}
