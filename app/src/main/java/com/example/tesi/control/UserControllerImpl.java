package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.User;
import com.example.tesi.service.UserServiceRetrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserControllerImpl implements UserController{
	private final UserServiceRetrofit userServiceRetrofit;

	public UserControllerImpl() {
		Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/users/").addConverterFactory(GsonConverterFactory.create()).build();
		userServiceRetrofit=retrofit.create(UserServiceRetrofit.class);
	}

	public boolean saveUser(User user) {
		Call<User> call= userServiceRetrofit.saveUser(user);
		final boolean[] isSuccessful=new boolean[1];
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				isSuccessful[0]=response.isSuccessful();
				if (isSuccessful[0])
					Log.println(Log.DEBUG, "SAVE USER", "Save successful");
				else
					Log.println(Log.DEBUG, "SAVE USER", "Save error");
			}

			@Override
			public void onFailure(Call<User> call, Throwable throwable) {
				Log.println(Log.ERROR, "SAVE USER", "Save failed");
			}
		});
		return isSuccessful[0];
	}

	@Override
	public User loginUser(String username, String password) {
		Call<User> call=userServiceRetrofit.loginUser(username, password);
		final User[] user = new User[1];
		Thread t=new Thread() {
			@Override
			public void run() {
				try {
					user[0] =call.execute().body();
				} catch (IOException e) {
					Log.println(Log.ERROR, "LOGIN USER", "Login error");
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			return null;
		}

		return user[0];
	}
}
