package com.tesi.client.control;

import com.tesi.entity.FotoByteArray;
import com.tesi.entity.Prodotto;
import com.tesi.client.service.FotoProdottoServiceRetrofit;
import com.tesi.client.utils.FotoByteArrayDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FotoProdottoControllerImpl implements FotoProdottoController{
	private final FotoProdottoServiceRetrofit fotoProdottoServiceRetrofit;
	private static FotoProdottoController instance;

	public static FotoProdottoController getInstance() {
		if (instance==null)
			instance=new FotoProdottoControllerImpl();
		return instance;
	}

	private FotoProdottoControllerImpl() {
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
					try (ResponseBody body=response.body()) {
						assert body != null;
						String jsonResponse = body.string();
						Type listType = new TypeToken<List<FotoByteArray>>() {}.getType();

						return gson.fromJson(jsonResponse, listType);
					}
				}

				return new LinkedList<>();
			} catch (IOException e) {
				return new LinkedList<>();
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return new LinkedList<>();
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
					try (ResponseBody body=response.body()) {
						assert body != null;
						String jsonResponse = body.string();
						Type fotoType = new TypeToken<FotoByteArray>() {}.getType();

						return gson.fromJson(jsonResponse, fotoType);
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
			return null;
		}
	}
}
