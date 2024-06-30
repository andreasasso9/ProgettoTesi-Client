package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;

public interface ProdottoController {
	public Prodotto add(Prodotto prodotto);
	public List<Prodotto> getAll(int limit);
	public List<Prodotto> getAllNotOwnedBy(User user);
	public boolean miPiace(Prodotto prodotto);
}
