package com.example.tesi.entity;

public class Notifica {
	private Long id;
	private String sender, receiver;
	private String descrizione;
	private byte[] foto;

	public Notifica() {}

	public Notifica(String sender, String receiver, String descrizione, byte[] foto) {
		this.sender = sender;
		this.receiver = receiver;
		this.descrizione = descrizione;
		this.foto = foto;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
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
