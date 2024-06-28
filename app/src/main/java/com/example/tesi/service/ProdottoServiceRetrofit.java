package com.example.tesi.service;

import com.example.tesi.entity.Prodotto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProdottoServiceRetrofit {
	@POST("addProdotto")
	Call<Prodotto> add(@Body Prodotto prodotto);

	@POST("getAll")
	Call<List<Prodotto>> getAll(@Body int limit);
}
