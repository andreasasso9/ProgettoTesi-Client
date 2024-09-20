package com.tesi.entity.chat;

public class Image extends Text{
	private final String foto;

	public Image(String foto, String sender, String receiver, String chatId) {
		super(null, sender, receiver, chatId);
		this.foto=foto;
	}

	public String getFoto() {
		return foto;
	}
}
