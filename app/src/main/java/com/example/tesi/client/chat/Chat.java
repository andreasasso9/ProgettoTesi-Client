package com.example.tesi.client.chat;

import com.example.tesi.entity.User;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {
	private User receiver;
	private List<Text> texts;

	public Chat(User receiver, List<Text> texts) {
		this.receiver = receiver;
		this.texts = texts;
	}

	public User getReceiver() {
		return receiver;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
}
