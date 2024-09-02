package com.tesi.client.service;

import com.tesi.entity.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenService {
	@POST("save")
	Call<Boolean> save(@Body Token token);

	@POST("delete")
	Call<Boolean> delete(@Body Token token);

	@POST("findByToken")
	Call<Token> findByToken(@Body String token);
}
