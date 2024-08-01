package com.example.tesi.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class User implements Serializable {
	private String email;
	private String username;
	private String password;
	private UUID id;
	private String indirizzo;
	private Set<Prodotto> prodottiPreferiti;

	public User(){}

	public User(String email, String username, String password, String indirizzo) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.indirizzo =indirizzo;
		prodottiPreferiti=new HashSet<>();
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

	public Set<Prodotto> getProdottiPreferiti() {
		return prodottiPreferiti;
	}

	public void setProdottiPreferiti(Set<Prodotto> prodottiPreferiti) {
		this.prodottiPreferiti = prodottiPreferiti;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(Locale.ITALIAN, "%s %s", username, id);
	}
}
