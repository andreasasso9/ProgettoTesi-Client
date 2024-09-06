package com.tesi.entity;

import com.tesi.entity.entityoptions.Brand;
import com.tesi.entity.entityoptions.Categoria;
import com.tesi.entity.entityoptions.Condizioni;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Prodotto implements Serializable {
	private String titolo;
	private String descrizione;
	private Categoria categoria;
	private Brand brand;
	private Condizioni condizioni;
	private double prezzo;
	private final String proprietario;
	private Long id;
	private String compratore;
	private Set<User> likedBy;

	public Prodotto(String proprietario, String titolo, String descrizione, Categoria categoria, Brand brand, Condizioni condizioni, double prezzo) {
		this.proprietario = proprietario;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.brand = brand;
		this.condizioni = condizioni;
		this.prezzo = prezzo;
		likedBy=new HashSet<>();
	}

	public String getProprietario() {
		return proprietario;
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Prodotto prodotto = (Prodotto) o;
		return Objects.equals(id, prodotto.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void setCompratore(String compratore) {
		this.compratore = compratore;
	}

	public String getCompratore() {
		return compratore;
	}

	public boolean isBought() {
		return compratore!=null;
	}

	public Set<User> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Set<User> likedBy) {
		this.likedBy = likedBy;
	}

	public int getMiPiace() {
		return likedBy.size();
	}
}
