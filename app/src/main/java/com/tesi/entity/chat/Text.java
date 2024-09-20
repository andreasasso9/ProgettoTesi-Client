package com.tesi.entity.chat;

import java.io.Serializable;

public class Text implements Serializable {
	private final String text;
	protected final String sender, receiver;
	private String chatId;

	public Text(String text, String sender, String receiver, String chatId) {
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		this.chatId = chatId;
	}

	public String getText() {
		return text;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChat(String chatId) {
		this.chatId = chatId;
	}
}
