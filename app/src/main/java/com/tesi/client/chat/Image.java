package com.tesi.client.chat;

public class Image extends Text{
	private final String foto;

	public Image(String foto, String sender, String receiver) {
		super(null, sender, receiver);
		this.foto=foto;
		delivered=false;
	}

	public String getFoto() {
		return foto;
	}
}
