package com.example.tesi.entity;

import android.graphics.Bitmap;

import com.example.tesi.entity.entityenum.Brand;
import com.example.tesi.entity.entityenum.Categoria;
import com.example.tesi.entity.entityenum.Condizioni;

import java.util.List;

public class Prodotto {
	private String titolo;
	private String descrizione;
	private Categoria categoria;
	private Brand brand;
	private Condizioni condizioni;
	private double prezzo;
	private List<Bitmap> foto;
	private int nPreferiti;

	public Prodotto(String titolo, String descrizione, Categoria categoria, Brand brand, Condizioni condizioni, double prezzo, List<Bitmap> foto) {
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.brand = brand;
		this.condizioni = condizioni;
		this.prezzo = prezzo;
		this.foto = foto;
		nPreferiti=0;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Condizioni getCondizioni() {
		return condizioni;
	}

	public void setCondizioni(Condizioni condizioni) {
		this.condizioni = condizioni;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public List<Bitmap> getFoto() {
		return foto;
	}

	public void setFoto(List<Bitmap> foto) {
		this.foto = foto;
	}

	public int getPreferiti() {
		return nPreferiti;
	}

	public void setPreferiti(int nPreferiti) {
		this.nPreferiti = nPreferiti;
	}
}