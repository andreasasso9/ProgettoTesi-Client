package com.example.tesi.utils;

import java.util.Date;

public class Ricerca implements Comparable<Ricerca> {
	private String text;
	private Date date;

	public Ricerca(String text) {
		this.text = text;
		this.date = new Date();
	}

	public String getText() {
		return text;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(Ricerca o) {
		return date.compareTo(o.date);
	}
}
