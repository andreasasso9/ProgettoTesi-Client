package com.example.tesi.service;

import com.example.tesi.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserServiceRetrofit {
	@POST("save")
	Call<User> saveUser(@Body User user);
	@POST("login")
	@FormUrlEncoded
	Call<User> loginUser(@Field("username") String username, @Field("password") String password);
}