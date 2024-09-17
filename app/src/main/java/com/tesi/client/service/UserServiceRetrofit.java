package com.tesi.client.service;

import com.tesi.entity.User;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserServiceRetrofit {
	@POST("save")
	@FormUrlEncoded
	Call<User> saveUser(
			@Field("email") String email,
			@Field("username") String username,
			@Field("password") String password,
			@Field("indirizzo") String indirizzo
	);

	@POST("login")
	@FormUrlEncoded
	Call<User> loginUser(@Field("username") String username, @Field("password") String password);

	@POST("update")
	Call<Boolean> update(@Body User user);

	@POST("findUserById")
	Call<User> findById(@Body UUID id);

	@POST("checkEmail")
	Call<Boolean> checkEmail(@Body String Email);
}
