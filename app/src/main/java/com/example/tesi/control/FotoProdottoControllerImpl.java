package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.FotoByteArray;
import com.example.tesi.entity.Prodotto;
import com.example.tesi.service.FotoProdottoServiceRetrofit;
import com.example.tesi.utils.FotoByteArrayDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FotoProdottoControllerImpl implements FotoProdottoController{
	private final FotoProdottoServiceRetrofit fotoProdottoServiceRetrofit;

	public FotoProdottoControllerImpl() {
		fotoProdottoServiceRetrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/prodotto/foto/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(FotoProdottoServiceRetrofit.class);
	}

	@Override
	public boolean add(List<FotoByteArray> foto) {
		Call<Boolean> call=fotoProdottoServiceRetrofit.addFoto(foto);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				else
					return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}

	@Override
	public List<FotoByteArray> findByProdotto(Prodotto prodotto) {
		Gson gson=new GsonBuilder().registerTypeAdapter(FotoByteArray.class, new FotoByteArrayDeserializer()).create();

		Call<ResponseBody> call=fotoProdottoServiceRetrofit.findByProdotto(prodotto);
		CompletableFuture<List<FotoByteArray>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					String jsonResponse=response.body().string();
					Type listType=new TypeToken<List<FotoByteArray>>() {}.getType();

					return gson.fromJson(jsonResponse, listType);
				}
				else
					throw new RuntimeException("Failed to fetch data");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public FotoByteArray findFirst(Prodotto prodotto) {
		Gson gson=new GsonBuilder().registerTypeAdapter(FotoByteArray.class, new FotoByteArrayDeserializer()).create();

		Call<ResponseBody> call=fotoProdottoServiceRetrofit.findFirst(prodotto);
		CompletableFuture<FotoByteArray> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					String jsonResponse=response.body().string();
					Type fotoType=new TypeToken<FotoByteArray>() {}.getType();

					return gson.fromJson(jsonResponse, fotoType);
				}
				else
					throw new RuntimeException("Failed to fetch data");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
