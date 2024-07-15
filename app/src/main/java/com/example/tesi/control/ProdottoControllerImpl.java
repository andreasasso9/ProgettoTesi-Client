package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.entity.User;
import com.example.tesi.service.ProdottoServiceRetrofit;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
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

	@Override
	public boolean update(Prodotto prodotto) {
		Call<Boolean> call=prodottoServiceRetrofit.update(prodotto);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful()) {
					Log.println(Log.INFO, "UPDATE PRODOTTO", "SUCCESS");
					return response.body();
				}
				else {
					Log.println(Log.ERROR, "UPDATE PRODOTTO", "FAILED");
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
	public List<Prodotto> findByIdProprietario(UUID idProprietario) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.findByIdProprietario(idProprietario);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
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
			return null;
		}
	}

	@Override
	public List<Prodotto> findByTitoloODescrizione(UUID userId, String text) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.findByTitoloODescrizione(userId, text);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
				if (response.isSuccessful())
					return response.body();
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
