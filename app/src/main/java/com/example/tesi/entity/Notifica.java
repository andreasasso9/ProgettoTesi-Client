package com.example.tesi.entity;

import java.util.UUID;

public class Notifica {
	private Long id;
	private UUID sender, receiver;
	private String descrizione;
	private byte[] foto;

	public Notifica() {}

	public Notifica(UUID sender, UUID receiver, String descrizione, byte[] foto) {
		this.sender = sender;
		this.receiver = receiver;
		this.descrizione = descrizione;
		this.foto = foto;
	}

	public UUID getSender() {
		return sender;
	}

	public void setSender(UUID sender) {
		this.sender = sender;
	}

	public UUID getReceiver() {
		return receiver;
	}

	public void setReceiver(UUID receiver) {
		this.receiver = receiver;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
