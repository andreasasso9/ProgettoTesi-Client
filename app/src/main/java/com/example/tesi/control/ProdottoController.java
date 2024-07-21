package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;
import java.util.UUID;

public interface ProdottoController {
	Prodotto add(Prodotto prodotto);
	List<Prodotto> getAll(int limit);
	List<Prodotto> getAllNotOwnedBy(User user);
	boolean update(Prodotto prodotto);
	List<Prodotto> findByIdProprietario(UUID idProprietario);
	List<Prodotto> findByRicerca(UUID idUser, String text);
	List<Prodotto> findByCompratore(User compratore);
}
