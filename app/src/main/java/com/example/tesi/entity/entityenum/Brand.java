package com.example.tesi.entity.entityenum;

public enum Brand {
	GUCCI("Gucci"),
	PRADA("Prada"),
	VERSACE("Versace"),
	ARMANI("Armani"),
	DIOR("Dior"),
	LOUIS_VUITTON("Louis Vuitton"),
	CHANEL("Chanel"),
	HERMES("Hermes"),
	BURBERRY("Burberry"),
	DOLCE_GABBANA("Dolce & Gabbana"),
	FENDI("Fendi"),
	RALPH_LAUREN("Ralph Lauren"),
	TOMMY_HILFIGER("Tommy Hilfiger"),
	CALVIN_KLEIN("Calvin Klein"),
	HUGO_BOSS("Hugo Boss"),
	ZARA("Zara"),
	H_AND_M("H&M"),
	UNIQLO("Uniqlo"),
	ADIDAS("Adidas"),
	NIKE("Nike"),
	PUMA("Puma"),
	LEVI_S("Levi's"),
	LACOSTE("Lacoste"),
	CONVERSE("Converse"),
	NEW_BALANCE("New Balance"),
	ALTRO("Altro");

	private final String nome;

	Brand(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}
