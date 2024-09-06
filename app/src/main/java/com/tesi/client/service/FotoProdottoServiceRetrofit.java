package com.tesi.client.service;

import com.tesi.entity.FotoByteArray;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FotoProdottoServiceRetrofit {
	@POST("addFoto")
	Call<Boolean> addFoto(@Body List<FotoByteArray> foto);

	@POST("findByIdProdotto")
	Call<ResponseBody> findByProdotto(@Body Long idProdotto);

	@POST("findFirst")
	Call<ResponseBody> findFirst(@Body Long idProdotto);

	@POST("deleteByIdProdotto")
	Call<Void> deleteByIdProdotto(@Body Long idProdotto);
}
