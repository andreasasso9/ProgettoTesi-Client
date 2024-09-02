package com.tesi.client.control;

import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;

import java.util.List;

public interface FotoProdottoController {
	boolean add(List<FotoByteArray> foto);
	List<FotoByteArray> findByProdotto(Prodotto prodotto);
	FotoByteArray findFirst(Prodotto prodotto);
}
