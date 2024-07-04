package com.example.tesi.service;

import com.example.tesi.entity.Notifica;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificheServiceRetrofit {
	@POST("save")
	Call<Boolean> save(@Body Notifica notifica);

	@POST("findByReceiver")
	Call<List<Notifica>> findByReceiver(@Body UUID receiver);
}
