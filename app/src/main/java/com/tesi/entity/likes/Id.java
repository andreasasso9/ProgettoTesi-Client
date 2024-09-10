package com.tesi.entity.likes;

import java.io.Serializable;
import java.util.Objects;

public class Id implements Serializable {
	private Long idProdotto;
	private String username;

	public Id() {}

	public Id(Long idProdotto, String username) {
		this.idProdotto = idProdotto;
		this.username = username;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Long idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Id id = (Id) o;
		return Objects.equals(idProdotto, id.idProdotto) && Objects.equals(username, id.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idProdotto, username);
	}
}
