package com.example.tesi.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.tesi.client.chat.Chat;
import com.example.tesi.client.chat.Image;
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
	private final SharedPreferences.Editor editor;
	private final SharedPreferences preferences;
	private User currentUser;
	private static Session instance;
	public static final String SESSION_PREFERENCES="session_preferences";
	private final Gson gson;
	private Set<String> fileChatsNames;
	private StompClient stompClient;


	private Session(Context context) {
		preferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
		editor= preferences.edit();
		editor.apply();

		gson=new Gson();

		fileChatsNames=preferences.getStringSet("fileChatsNames", new HashSet<>());
	}

	public static Session getInstance(Context context) {
		if (instance==null)
			instance=new Session(context);
		return instance;
	}

	public void setCurrentUser(@Nullable User user, Context context) {
		currentUser=user;
		File.saveObjectToFile(context, "loggedUser", user);
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
		editor.putInt("screenHeight", screenHeight).apply();
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

		editor.putStringSet("fileChatsNames", fileChatsNames).apply();
	}

	public StompClient getStompClient() {
		return stompClient;
	}

	public void setStompClient(StompClient stompClient) {
		this.stompClient = stompClient;
	}

	@SuppressLint("CheckResult")
	public void createStompClient(Context context) {
		stompClient= Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/cloneVinted/websocket");
		stompClient.connect();

		stompClient.topic("/queue/user/"+currentUser.getUsername()).retry(5).subscribe(message->{
			Text text=gson.fromJson(message.getPayload(), Text.class);
			Image image=null;

			String nameChat="chat-"+text.getReceiver()+"-"+text.getSender();
			Chat chat;

			if (text.getText()==null) {
				image = gson.fromJson(message.getPayload(), Image.class);
				text=null;
			}

			if (fileChatsNames.contains(nameChat)) {
				chat = (Chat) File.readObjectFromFile(context, nameChat);
				if (chat!=null)
					if (text!=null)
						chat.getTexts().add(text);
					else
						chat.getTexts().add(image);
			} else {
				fileChatsNames.add(nameChat);
				List<Text> texts=new ArrayList<>();
				String sender;
				if (text!=null) {
					texts.add(text);
					sender=text.getSender();
				} else {
					texts.add(image);
					sender=image.getSender();
				}
				chat=new Chat(sender, texts, nameChat);
			}

			setFileChatsNames(fileChatsNames);

			File.deleteFile(context, nameChat);
			File.saveObjectToFile(context, nameChat, chat);
		});


		stompClient.send("/app/synchronize", currentUser.getUsername()).retry(0).subscribe();
	}

	public String getToken() {
		return preferences.getString("token", null);
	}

	public void setToken(String token) {
		editor.putString("token", token).apply();
	}
}
