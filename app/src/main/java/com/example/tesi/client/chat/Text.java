package com.example.tesi.client.chat;

import java.io.Serializable;

public class Text implements Serializable {
	private final String text;
	protected final String sender, receiver;
	protected boolean delivered;

	public Text(String text, String sender, String receiver) {
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
		delivered = false;
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

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
}
