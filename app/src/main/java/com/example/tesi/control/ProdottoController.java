package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;

import java.util.List;

public interface ProdottoController {
	Prodotto add(Prodotto prodotto);
	List<Prodotto> getAll(int limit);
	List<Prodotto> getAllNotOwnedBy(String user);
	boolean update(Prodotto prodotto);
	List<Prodotto> findByProprietario(String proprietario);
	List<Prodotto> findByRicerca(String user, String text);
	List<Prodotto> findByCompratore(String compratore);
}
