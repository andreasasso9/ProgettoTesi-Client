package com.example.tesi.control;

import com.example.tesi.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServiceRetrofit {
	@POST("save")
	Call<User> saveUser(@Body User user);
}
