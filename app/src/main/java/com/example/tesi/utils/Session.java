package com.example.tesi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tesi.entity.User;

public class Session {
	private final SharedPreferences.Editor editor;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";
	static int screenWidth;
	static int screenHeight;

	private Session(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		editor= preferences.edit();
		editor.apply();
	}

	public static Session getInstance(Context context) {
		if (instance==null)
			instance=new Session(context);
		return instance;
	}

	public void setCurrentUser(User user, String password, boolean toSave) {
		currentUser=user;

		if (toSave)
			editor.putString("username", user.getUsername()).putString("password", password);
		else
			editor.clear();

		editor.apply();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		Session.screenWidth = screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		Session.screenHeight = screenHeight;
	}
}
