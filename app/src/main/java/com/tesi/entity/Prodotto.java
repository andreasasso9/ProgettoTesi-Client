package com.tesi.entity;


import java.io.Serializable;
import java.util.Objects;

public class Prodotto implements Serializable {
	public static final String[] brands = {
			"Zara", "H&M", "Uniqlo", "Nike",
			"Adidas", "Puma", "Levi's", "Supreme",
			"Under Armour", "Reebok", "Columbia Sportswear",
			"Gucci", "Prada", "Emporio Armani", "Altro"
	};

	public static final String[] categorie = {
			"Magliette", "Camicie", "Felpe", "Giubbotti", "Cappotti",
			"Pantaloni", "Jeans", "Shorts", "Gonne", "Abiti", "Costumi da Bagno",
			"Biancheria Intima", "Calze", "Scarpe", "Stivali", "Sandali",
			"Accessori", "Cappelli", "Guanti", "Sciarpe", "Borse",
			"Abbigliamento Sportivo", "Abbigliamento da Notte", "Abbigliamento da Casa",
			"Abbigliamento Maternità", "Abbigliamento da Lavoro", "Abbigliamento per Bambini",
			"Altro"
	};

	public static final String[] condizioni = {
			"Nuovo con cartellino", "Nuovo senza cartellino", "Ottime",
			"Buone", "Discrete"
	};

	private String titolo;
	private String descrizione;
	private String categoria, brand, condizione;
	private double prezzo;
	private final String proprietario;
	private Long id;
	private String compratore;
	private int likes;

	public Prodotto(String proprietario, String titolo, String descrizione, String categoria, String brand, String condizione, double prezzo) {
		this.proprietario = proprietario;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.categoria = categoria;
		this.brand = brand;
		this.condizione = condizione;
		this.prezzo = prezzo;
		likes=0;
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCondizione() {
		return condizione;
	}

	public void setCondizione(String condizioni) {
		this.condizione = condizioni;
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

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
}
