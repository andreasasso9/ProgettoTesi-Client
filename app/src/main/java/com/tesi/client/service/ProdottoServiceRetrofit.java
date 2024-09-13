package com.tesi.client.service;

import com.tesi.entity.Prodotto;

import java.util.List;
import java.util.Set;

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
	Call<Boolean> update(@Body Long idProdotto);

	@POST("findByIdProprietario")
	Call<List<Prodotto>> findByProprietario(@Body String idProprietario);

	@POST("findByRicerca")
	@FormUrlEncoded
	Call<List<Prodotto>> findByRicerca(@Field("user") String userId, @Field("text") String text);

	@POST("findByCompratore")
	Call<List<Prodotto>> findByCompratore(@Body String compratore);

	@POST("delete")
	Call<Void> deleteById(@Body Long id);

	@POST("findByLikedBy")
	Call<Set<Prodotto>> findByLikedBy(@Body String username);

	@POST("acquista")
	@FormUrlEncoded
	Call<Boolean> acquista(@Field("username") String username, @Field("idProdotto") Long idProdotto);
}
