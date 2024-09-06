package com.tesi.entity;

import java.io.Serializable;

public class FotoByteArray implements Serializable {

	private Long id;
	private byte[] value;
	private Long idProdotto;

	public FotoByteArray() {}

	public FotoByteArray(byte[] value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Long idProdotto) {
		this.idProdotto = idProdotto;
	}
}
