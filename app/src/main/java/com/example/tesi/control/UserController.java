package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserController {
	private final UserServiceRetrofit userServiceRetrofit;

	public UserController() {
		Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/users/").addConverterFactory(GsonConverterFactory.create()).build();
		userServiceRetrofit=retrofit.create(UserServiceRetrofit.class);
	}

	public void saveUser(User user) {
		Call<User> call= userServiceRetrofit.saveUser(user);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.isSuccessful())
					Log.println(Log.DEBUG, "SAVE USER", "Success");
				else
					Log.println(Log.DEBUG, "SAVE USER", "Failure");
			}

			@Override
			public void onFailure(Call<User> call, Throwable throwable) {
				Log.println(Log.ERROR, "SAVE USER", "onFailure");
			}
		});
	}
}
