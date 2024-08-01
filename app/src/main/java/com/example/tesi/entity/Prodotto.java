package com.example.tesi.entity;

import com.example.tesi.entity.entityoptions.Brand;
import com.example.tesi.entity.entityoptions.Categoria;
import com.example.tesi.entity.entityoptions.Condizioni;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Prodotto implements Serializable {
	private String titolo;
	private String descrizione;
	private Categoria categoria;
	private Brand brand;
	private Condizioni condizioni;
	private double prezzo;
	private int miPiace;
	private final UUID idProprietario;
	private Long id;
	private UUID idCompratore;

	public Prodotto(UUID idProprietario, String titolo, String descrizione, Categoria categoria, Brand brand, Condizioni condizioni, double prezzo) {
		this.idProprietario = idProprietario;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.brand = brand;
		this.condizioni = condizioni;
		this.prezzo = prezzo;
		this.miPiace = 0;
	}

	public UUID getIdProprietario() {
		return idProprietario;
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

	public int getMiPiace() {
		return miPiace;
	}

	public void setMiPiace(int miPiace) {
		this.miPiace = miPiace;
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

	public void setCompratore(UUID idCompratore) {
		this.idCompratore = idCompratore;
	}

	public UUID getCompratore() {
		return idCompratore;
	}

	public boolean isBought() {
		return idCompratore!=null;
	}
}
