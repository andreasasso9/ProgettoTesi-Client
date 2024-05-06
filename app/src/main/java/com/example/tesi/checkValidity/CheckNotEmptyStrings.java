package com.example.tesi.checkValidity;

public class CheckNotEmptyStrings {
	public static boolean check(String... strings) {
		for (String s:strings)
			if (s.isEmpty())
				return false;
		return true;
	}
}
