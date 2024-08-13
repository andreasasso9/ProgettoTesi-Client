package com.example.tesi.client.chat;

import com.example.tesi.entity.FotoByteArray;

public class Image extends Text{
	private final FotoByteArray foto;

	public Image(FotoByteArray foto, String sender, String receiver) {
		super(null, sender, receiver);
		this.foto=foto;
	}

	public FotoByteArray getFoto() {
		return foto;
	}
}
