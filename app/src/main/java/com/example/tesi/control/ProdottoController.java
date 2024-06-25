package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;

import java.util.List;

public interface ProdottoController {
	public boolean add(Prodotto prodotto);
	public List<Prodotto> getAll(int limit);
}
