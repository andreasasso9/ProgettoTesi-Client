package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.Notifica;
import com.example.tesi.service.NotificheServiceRetrofit;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.tesi.utils.FotoByteArrayDeserializer;
import com.example.tesi.utils.NotificheDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class NotificheControllerImpl implements NotificheController {
	private NotificheServiceRetrofit notificheServiceRetrofit;

	public NotificheControllerImpl() {
		this.notificheServiceRetrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/notifiche/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(NotificheServiceRetrofit.class);
	}

	@Override
	public boolean save(Notifica notifica) {
		Call<Boolean> call= notificheServiceRetrofit.save(notifica);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				try (ResponseBody errrorBody=response.errorBody()) {
					assert errrorBody != null;
					Log.println(Log.ERROR, "SAVE NOTIFICA", errrorBody.string());
					return false;
				}
			} catch (IOException e) {
				return false;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}

	@Override
	public List<Notifica> findByReceiver(UUID receiver) {
		Gson gson=new GsonBuilder().registerTypeAdapter(Notifica.class, new NotificheDeserializer()).create();

		Call<ResponseBody> call=notificheServiceRetrofit.findByReceiver(receiver);
		CompletableFuture<List<Notifica>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						assert body != null;
						String jsonResponse= body.string();

						Type type=new TypeToken<List<Notifica>>() {}.getType();

						return gson.fromJson(jsonResponse, type);
					}
				}
				return null;
			} catch (IOException e) {

				return null;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
