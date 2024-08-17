package com.example.tesi.service;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ProdottoServiceRetrofit {
	@POST("addProdotto")
	Call<Prodotto> add(@Body Prodotto prodotto);

	@POST("getAll")
	Call<List<Prodotto>> getAll(@Body int limit);

	@POST("getAllNotOwnedBy")
	Call<List<Prodotto>> getAllNotOwnedBy(@Body String user);

	@POST("update")
	Call<Boolean> update(@Body Prodotto prodotto);

	@POST("findByIdProprietario")
	Call<List<Prodotto>> findByProprietario(@Body String idProprietario);

	@POST("findByRicerca")
	@FormUrlEncoded
	Call<List<Prodotto>> findByRicerca(@Field("user") String userId, @Field("text") String text);

	@POST("findByCompratore")
	Call<List<Prodotto>> findByCompratore(@Body String compratore);
}
