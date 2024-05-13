package com.example.tesi.entity;

import com.example.tesi.entity.entityoptions.Brand;
import com.example.tesi.entity.entityoptions.Categoria;
import com.example.tesi.entity.entityoptions.Condizioni;

import java.io.Serializable;
import java.util.List;

public class Prodotto implements Serializable {
	private String titolo;
	private String descrizione;
	private Categoria categoria;
	private Brand brand;
	private Condizioni condizioni;
	private double prezzo;
	private List<FotoByteArray> foto;
	private int nPreferiti;
	private User proprietario;
	private Long id;

	public Prodotto(User proprietario, String titolo, String descrizione, Categoria categoria, Brand brand, Condizioni condizioni, double prezzo, List<FotoByteArray> foto) {
		this.proprietario = proprietario;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.brand = brand;
		this.condizioni = condizioni;
		this.prezzo = prezzo;
		this.foto = foto;
		nPreferiti=0;
	}

	public Prodotto() {}

	public User getProprietario() {
		return proprietario;
	}

	public void setProprietario(User proprietario) {
		this.proprietario = proprietario;
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

	public List<FotoByteArray> getFoto() {
		return foto;
	}

	public void setFoto(List<FotoByteArray> foto) {
		this.foto = foto;
	}

	public int getPreferiti() {
		return nPreferiti;
	}

	public void setPreferiti(int nPreferiti) {
		this.nPreferiti = nPreferiti;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
