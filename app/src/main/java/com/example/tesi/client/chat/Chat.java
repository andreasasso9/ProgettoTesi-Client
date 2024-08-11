package com.example.tesi.client.chat;

import com.example.tesi.entity.User;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {
	private String id;
	private final String receiver;
	private List<Text> texts;

	public Chat(String receiver, List<Text> texts, String id) {
		this.receiver = receiver;
		this.texts = texts;
		this.id = id;
	}

	public String getReceiver() {
		return receiver;
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
}
