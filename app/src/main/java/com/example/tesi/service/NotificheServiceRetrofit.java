package com.example.tesi.service;

import com.example.tesi.entity.Notifica;

import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificheServiceRetrofit {
	@POST("save")
	Call<Boolean> save(@Body Notifica notifica);

	@POST("findByIdReceiver")
	Call<ResponseBody> findByReceiver(@Body UUID receiver);

	@POST("delete")
	Call<Boolean> delete(@Body String descrizione);
}
