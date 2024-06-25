package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.service.ProdottoServiceRetrofit;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
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
	public boolean add(Prodotto prodotto) {
		Call<Boolean> call=prodottoServiceRetrofit.add(prodotto);
		AtomicBoolean response=new AtomicBoolean();

		Thread addThread= new Thread(() -> {
			try {
				boolean result=Boolean.TRUE.equals(call.execute().body());
				response.set(result);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		addThread.start();
		try {
			addThread.join();
		} catch (InterruptedException e) {
			return response.get();
		}

		return response.get();
	}

	@Override
	public List<Prodotto> getAll(int limit) {
		Call<List<Prodotto>> call=prodottoServiceRetrofit.getAll(limit);
		List<Prodotto> prodotti=new CopyOnWriteArrayList<>();

		Thread getAllThread=new Thread(() -> {
			try {
				List<Prodotto> response=call.execute().body();
				if (response!=null)
					prodotti.addAll(response);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		getAllThread.start();
		try {
			getAllThread.join();
		} catch (InterruptedException e) {
			return prodotti;
		}

		return prodotti;
	}


}
