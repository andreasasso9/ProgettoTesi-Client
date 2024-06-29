package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.service.ProdottoServiceRetrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdottoControllerImpl implements ProdottoController {
	private final ProdottoServiceRetrofit prodottoServiceRetrofit;

	public ProdottoControllerImpl() {
		prodottoServiceRetrofit= new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/prodotto/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(ProdottoServiceRetrofit.class);
	}
	@Override
	public Prodotto add(Prodotto prodotto) {
		Call<Prodotto> call=prodottoServiceRetrofit.add(prodotto);
		CompletableFuture<Prodotto> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Prodotto> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				else
					throw new RuntimeException("ADD PRODOTTO FAILED");
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
	public List<Prodotto> getAll(int limit) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.getAll(limit);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				else
					Log.println(Log.ERROR, "GET ALL PRODOTTO", "FAILED");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return null;
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}

	@Override
	public List<Prodotto> getAllNotOwnedBy(User user) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.getAllNotOwnedBy(user);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
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
			return null;
		}
	}


}
