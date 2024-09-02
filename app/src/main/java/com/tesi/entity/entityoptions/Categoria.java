package com.tesi.entity.entityoptions;

public enum Categoria implements Option{
	VESTITI("Vestiti"),
	SCARPE("Scarpe"),
	ACCESSORI("Accessori"),
	CURA_DEL_CORPO("Cura del corpo"),
	BELLEZZA("Bellezza"),
	ANIMALI("Animali"),
	CASA("Casa"),
	INTRATTENIMENTO("Intrattenimento"),
	BAMBINI("Bambini"),
	ALTRO("Altro");

	private final String nome;
	Categoria(String nome) {
		this.nome=nome;
	}

	public String getNome() {
		return nome;
	}
}
