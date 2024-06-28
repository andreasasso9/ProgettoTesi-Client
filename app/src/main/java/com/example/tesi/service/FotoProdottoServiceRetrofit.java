package com.example.tesi.service;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FotoProdottoServiceRetrofit {
	@POST("addFoto")
	Call<Boolean> addFoto(@Body List<FotoByteArray> foto);

	@POST("findByProdotto")
	Call<ResponseBody> findByProdotto(@Body Prodotto prodotto);

	@POST("findFirst")
	Call<ResponseBody> findFirst(@Body Prodotto prodotto);
}
