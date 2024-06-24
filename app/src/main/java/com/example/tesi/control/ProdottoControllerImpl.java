package com.example.tesi.control;

import com.example.tesi.entity.Prodotto;
import com.example.tesi.service.ProdottoServiceRetrofit;

import java.io.IOException;
import java.util.List;

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
		final Boolean[] response=new Boolean[1];

		Thread addThread= new Thread(() -> {
			try {
				response[0]=Boolean.TRUE.equals(call.execute().body());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		addThread.start();
		try {
			addThread.join();
		} catch (InterruptedException e) {
			return response[0];
		}

		return response[0];
	}

	@Override
	public List<Prodotto> get(int limit) {
		Call<List<Prodotto>> call= prodottoServiceRetrofit.get(limit);
		//TODO implementare metodo che ottiene limit prodotti
	}


}
