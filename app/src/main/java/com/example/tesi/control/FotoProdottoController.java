package com.example.tesi.control;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

public interface FotoProdottoController {
	public boolean add(List<FotoByteArray> foto);

	public List<FotoByteArray> findByProdotto(Prodotto prodotto);

	public FotoByteArray findFirst(Prodotto prodotto);
}
