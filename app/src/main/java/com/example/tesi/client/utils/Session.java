package com.example.tesi.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Text;
import com.example.tesi.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class Session {
	private final Context context;
	private final SharedPreferences.Editor editor;
	private final SharedPreferences preferences;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";
	private final Gson gson;
	private Set<String> fileChatsNames;
	private StompClient stompClient;

	private Session(Context context) {
		this.context=context;

		preferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		editor= preferences.edit();
		editor.commit();

		gson=new Gson();

		fileChatsNames=preferences.getStringSet("fileChatsNames", new HashSet<>());
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
		editor.putString("username", username).putString("password", password).commit();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public int getScreenWidth() {
		return preferences.getInt("screenWidth", 0);
	}

	public void setScreenWidth(int screenWidth) {
		editor.putInt("screenWidth", screenWidth).commit();
	}

	public int getScreenHeight() {
		return preferences.getInt("screenHeight", 0);
	}

	public void setScreenHeight(int screenHeight) {
		editor.putInt("screenHeight", screenHeight).commit();
	}

	public List<String> getSearchHistory() {
		String jsonHistory =preferences.getString("history " + currentUser.getId(), null);
		if (jsonHistory != null) {
			Type type = new TypeToken<List<String>>() {
			}.getType();
			return gson.fromJson(jsonHistory, type);
		} else
			return new ArrayList<>();
	}

	public void setSearchHistory(List<String> searchHistory) {
		String jsonHistory=gson.toJson(searchHistory);

		editor.putString("history "+currentUser.getId(), jsonHistory).commit();
	}

	public Set<String> getFileChatsNames() {
		return fileChatsNames;
	}

	public void setFileChatsNames(Set<String> fileChatsNames) {
		this.fileChatsNames = fileChatsNames;

		editor.putStringSet("fileChatsNames", this.fileChatsNames).commit();
	}

	public StompClient getStompClient() {
		return stompClient;
	}

	public void setStompClient(StompClient stompClient) {
		this.stompClient = stompClient;
	}

	@SuppressLint("CheckResult")
	public void createStompClient() {
		stompClient= Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/cloneVinted/websocket");
		stompClient.connect();
		stompClient.topic("/queue/user/"+currentUser.getUsername()).subscribe(message->{
			Text text=gson.fromJson(message.getPayload(), Text.class);
			String nameChat="chat-"+text.getReceiver()+"-"+text.getSender();
			Chat chat;
			if (fileChatsNames.contains(nameChat)) {
				chat = (Chat) File.readObjectFromFile(context, nameChat);
				if (chat!=null)
					chat.getTexts().add(text);
				File.deleteFile(context, nameChat);
			} else {
				fileChatsNames.add(nameChat);
				List<Text> texts=new ArrayList<>();
				texts.add(text);
				chat=new Chat(text.getSender(), texts, nameChat);
			}

			File.saveObjectToFile(context, nameChat, chat);
		});
	}
}
