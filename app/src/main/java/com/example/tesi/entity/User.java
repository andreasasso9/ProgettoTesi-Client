package com.example.tesi.entity;

import java.util.UUID;

public class User {
	private String email;
	private String username;
	private String password;
	private UUID id;
	private String indirizzo;

	public User(String email, String username, String password, UUID id, String indirizzo) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.id = id;
		this.indirizzo =indirizzo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
}
