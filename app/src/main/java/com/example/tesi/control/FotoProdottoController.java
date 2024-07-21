package com.example.tesi.control;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;

import java.util.List;

public interface FotoProdottoController {
	boolean add(List<FotoByteArray> foto);
	List<FotoByteArray> findByProdotto(Prodotto prodotto);
	FotoByteArray findFirst(Prodotto prodotto);
}
