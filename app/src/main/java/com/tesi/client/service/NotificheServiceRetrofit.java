package com.tesi.client.service;

import com.tesi.entity.Notifica;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificheServiceRetrofit {
	@POST("save")
	Call<Boolean> save(@Body Notifica notifica);

	@POST("findByReceiver")
	Call<ResponseBody> findByReceiver(@Body String receiver);

	@POST("delete")
	@FormUrlEncoded
	Call<Boolean> delete(@Field("descrizione") String descrizione, @Field("jsonLikeId") String likeId);

	@POST("miPiace")
	@FormUrlEncoded
	Call<Boolean> miPiace(@Field("sender") String sender, @Field("idProdotto") Long idProdotto);
}
