package com.tesi.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class User implements Serializable {
	private String email;
	private String username;
	private String password;
	private UUID id;
	private String indirizzo;

	public User(){}

	public User(String email, String username, String password, String indirizzo) {
		this.email = email;
		this.username = username;
		this.password = password;
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

	@NonNull
	@Override
	public String toString() {
		return String.format(Locale.ITALIAN, "%s %s", username, id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, username, password, id, indirizzo);
	}
}
