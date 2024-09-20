package com.tesi.entity.chat;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {
	private String id;
	private List<Text> texts;
	private String user1, user2;

	public Chat(List<Text> texts, String id, String user1, String user2) {
		this.texts = texts;
		this.id = id;
		this.user1 = user1;
		this.user2 = user2;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}
}
