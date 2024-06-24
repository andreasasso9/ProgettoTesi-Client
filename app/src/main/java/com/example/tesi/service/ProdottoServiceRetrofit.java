package com.example.tesi.service;

import com.example.tesi.entity.Prodotto;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProdottoServiceRetrofit {
	@POST("add")
	Call<Boolean> add(@Body Prodotto prodotto);

	@POST("get")
	Call<List<Prodotto>> get(@Body int limit);
}
