package com.example.tesi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tesi.entity.User;

public class Session {
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";

	private Session(Context context) {
		preferences=context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		editor=preferences.edit();
		editor.apply();
	}

	public static Session getInstance(Context context) {
		if (instance==null)
			instance=new Session(context);
		return instance;
	}

	public void setCurrentUser(User user, String password) {
		currentUser=user;
		editor.putString("username", user.getUsername());
		editor.putString("password", password);
		editor.apply();
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
