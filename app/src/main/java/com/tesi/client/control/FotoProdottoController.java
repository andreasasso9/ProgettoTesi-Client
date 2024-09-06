package com.tesi.client.control;

import com.tesi.entity.FotoByteArray;

import java.util.List;

public interface FotoProdottoController {
	boolean add(List<FotoByteArray> foto);
	List<FotoByteArray> findByProdotto(Long idProdotto);
	FotoByteArray findFirst(Long idProdotto);
	boolean deleteByIdProdotto(Long id);
}
