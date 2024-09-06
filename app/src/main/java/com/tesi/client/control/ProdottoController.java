package com.tesi.client.control;

import com.tesi.entity.Prodotto;

import java.util.List;
import java.util.Set;

public interface ProdottoController {
	Prodotto add(Prodotto prodotto);
	List<Prodotto> getAll(int limit);
	List<Prodotto> getAllNotOwnedBy(String user);
	boolean update(Prodotto prodotto);
	List<Prodotto> findByProprietario(String proprietario);
	List<Prodotto> findByRicerca(String user, String text);
	List<Prodotto> findByCompratore(String compratore);
	boolean deleteById(Long id);
	Set<Prodotto> findByLikedBy(String username);

}
