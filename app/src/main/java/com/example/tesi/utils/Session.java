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
import java.util.Stack;

public class Session {
	private final SharedPreferences.Editor editor;
	private final SharedPreferences preferences;
	private final SharedPreferences.Editor historyEditor;
	private final SharedPreferences historyPreferences;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";
	private final Gson gson;

	private Session(Context context) {
		preferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
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

	public int getScreenWidth() {
		return preferences.getInt("screenWidth", 0);
	}

	public void setScreenWidth(int screenWidth) {
		editor.putInt("screenWidth", screenWidth);
		editor.apply();
	}

	public int getScreenHeight() {
		return preferences.getInt("screenHeight", 0);
	}

	public void setScreenHeight(int screenHeight) {
		editor.putInt("screenHeight", screenHeight);
		editor.apply();
	}

	public List<String> getSearchHistory() {
		String jsonHistory = historyPreferences.getString("history " + currentUser.getId(), null);
		if (jsonHistory != null) {
			Type type = new TypeToken<Stack<String>>() {
			}.getType();
			return gson.fromJson(jsonHistory, type);
		} else
			return new Stack<>();
	}

	public void setSearchHistory(List<String> searchHistory) {
		String jsonHistory=gson.toJson(searchHistory);

		historyEditor.putString("history "+currentUser.getId(), jsonHistory).apply();
	}
}
