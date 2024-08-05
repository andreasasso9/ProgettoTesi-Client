package com.example.tesi.client.chat;

import java.io.Serializable;
import java.util.UUID;

public class Text implements Serializable {
	private final String text;
	private final UUID idSender;

	public Text(String text, UUID idSender) {
		this.text = text;
		this.idSender = idSender;
	}

	public String getText() {
		return text;
	}

	public UUID getIdSender() {
		return idSender;
	}
}
