package com.example.tesi.entity.entityenum;

public enum Condizioni {
	NUOVO_CON_CARTELLINO("Nuovo con cartellino"),
	NUOVO_SENZA_CARTELLINO("Nuovo senza cartellino"),
	OTTIME("Ottime"),
	BUONE("Buone"),
	DISCRETE("Discrete");

	private final String nome;

	Condizioni(String nome) {
		this.nome=nome;
	}

	public String getNome() {
		return nome;
	}
}
