package com.tesi.client.control;

import android.util.Log;

import com.tesi.entity.Prodotto;
import com.tesi.client.service.ProdottoServiceRetrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdottoControllerImpl implements ProdottoController {
	private static ProdottoController instance;
	private final ProdottoServiceRetrofit prodottoServiceRetrofit;

	private ProdottoControllerImpl() {
		prodottoServiceRetrofit= new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/prodotto/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(ProdottoServiceRetrofit.class);
	}

	public static ProdottoController getInstance() {
		if (instance==null)
			instance=new ProdottoControllerImpl();
		return instance;
	}
	@Override
	public Prodotto add(Prodotto prodotto) {
		Call<Prodotto> call=prodottoServiceRetrofit.add(prodotto);
		CompletableFuture<Prodotto> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Prodotto> response=call.execute();
				if (response.isSuccessful())
					return response.body();

				Log.d("ADD PRODOTTO", "FAILED");
				return null;
			} catch (IOException e) {
				Log.d("ADD PRODOTTO", e.getMessage());
				return null;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return null;
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
				return null;
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
	public List<Prodotto> getAllNotOwnedBy(String user, int page) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.getAllNotOwnedBy(user, page);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
				if (response.isSuccessful())
					return response.body();

				return new ArrayList<>();
			} catch (IOException e) {
				return new ArrayList<>();
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean update(Long idProdotto) {
		Call<Boolean> call=prodottoServiceRetrofit.update(idProdotto);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful()) {
					Log.println(Log.INFO, "UPDATE PRODOTTO", "SUCCESS");
					return response.body();
				} else {
					Log.println(Log.ERROR, "UPDATE PRODOTTO", "FAILED 1");
					return false;
				}
			} catch (IOException e) {
				Log.println(Log.ERROR, "UPDATE PRODOTTO", "FAILED 2");
				return false;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			Log.println(Log.ERROR, "UPDATE PRODOTTO", Arrays.toString(e.getStackTrace()));
			return false;
		}
	}

	@Override
	public List<Prodotto> findByProprietario(String proprietario) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.findByProprietario(proprietario);
		CompletableFuture<List<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<List<Prodotto>> response=call.execute();
				if (response.isSuccessful())
					return response.body();

				return new LinkedList<>();
			} catch (IOException e) {
				return new LinkedList<>();
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return new LinkedList<>();
		}
	}

	@Override
	public List<Prodotto> findByRicerca(String user, String text) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.findByRicerca(user, text);
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

	@Override
	public List<Prodotto> findByCompratore(String compratore) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.findByCompratore(compratore);
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

	@Override
	public void deleteById(Long id) {
		Call<Void> call=prodottoServiceRetrofit.deleteById(id);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Void> response=call.execute();
				return response.isSuccessful();
			} catch (IOException e) {
				return null;
			}
		});

		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			return;
		}
	}

	@Override
	public Set<Prodotto> findByLikedBy(String username) {
		Call<Set<Prodotto>> call=prodottoServiceRetrofit.findByLikedBy(username);
		CompletableFuture<Set<Prodotto>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Set<Prodotto>> response=call.execute();
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

	@Override
	public boolean acquista(String username, Long idProdotto) {
		Call<Boolean> call=prodottoServiceRetrofit.acquista(username, idProdotto);
		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful()) {
					Log.println(Log.INFO, "ACQUISTA PRODOTTO", "SUCCESS");
					return response.body();
				} else {
					Log.println(Log.ERROR, "ACQUISTA PRODOTTO", "FAILED 1");
					return false;
				}
			} catch (IOException e) {
				Log.println(Log.ERROR, "ACQUISTA PRODOTTO", "FAILED 2");
				return false;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			Log.println(Log.ERROR, "ACQUISTA PRODOTTO", Arrays.toString(e.getStackTrace()));
			return false;
		}
	}
}
