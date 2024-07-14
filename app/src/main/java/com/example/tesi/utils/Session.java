package com.example.tesi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tesi.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Session {
	private final SharedPreferences.Editor editor;
	private final SharedPreferences.Editor historyEditor;
	private final SharedPreferences historyPreferences;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";
	static int screenWidth;
	static int screenHeight;
	private final Gson gson;

	private Session(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		editor= preferences.edit();
		editor.apply();

		historyPreferences=context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		historyEditor=historyPreferences.edit();
		historyEditor.apply();

		gson=new Gson();
	}

	public static Session getInstance(Context context) {
		if (instance==null)
			instance=new Session(context);
		return instance;
	}

	public void setCurrentUser(@Nullable User user, String password) {
		currentUser=user;
		String username;
		if (user==null)
			username=null;
		else
			username=user.getUsername();
		editor.putString("username", username).putString("password", password);
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

	public List<Ricerca> getSearchHistory() {
		String jsonHistory = historyPreferences.getString("history " + currentUser.getId(), null);
		if (jsonHistory != null) {
			Type type = new TypeToken<List<Ricerca>>() {
			}.getType();
			return gson.fromJson(jsonHistory, type);
		} else
			return new ArrayList<>();

	}

	public void setSearchHistory(List<Ricerca> searchHistory) {
		String jsonHistory=gson.toJson(searchHistory);

		historyEditor.putString("history "+currentUser.getId(), jsonHistory).apply();
	}
}
