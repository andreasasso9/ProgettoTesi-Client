package com.tesi.client.control;

import com.tesi.client.service.TokenService;
import com.tesi.entity.Token;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenControllerImpl implements TokenController {
	private static TokenControllerImpl instance;
	private final TokenService tokenService;

	private TokenControllerImpl() {
		tokenService=new Retrofit.Builder()
				.baseUrl("http://10.0.2.2:8080/token/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(TokenService.class);
	}

	public static TokenControllerImpl getInstance() {
		if (instance==null)
			instance=new TokenControllerImpl();
		return instance;
	}

	@Override
	public boolean save(Token token) {
		Call<Boolean> call=tokenService.save(token);

		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				return false;
			} catch (IOException e) {
				return false;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean delete(Token token) {
		Call<Boolean> call=tokenService.delete(token);

		CompletableFuture<Boolean> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Boolean> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				return false;
			} catch (IOException e) {
				return false;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Token findByToken(String token) {
		Call<Token> call=tokenService.findByToken(token);

		CompletableFuture<Token> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<Token> response=call.execute();
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
			throw new RuntimeException(e);
		}
	}
}
