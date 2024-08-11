package com.example.tesi.client.chat;

import java.io.Serializable;

public class Text implements Serializable {
	private final String text;
	private final String sender, receiver;

	public Text(String text, String idSender, String idReceiver) {
		this.text = text;
		this.sender = idSender;
		this.receiver = idReceiver;
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
}
