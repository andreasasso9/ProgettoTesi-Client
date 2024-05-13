package com.example.tesi.entity;

import java.io.Serializable;

public class FotoByteArray implements Serializable {

	private Long id;
	private byte[] value;

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
}
