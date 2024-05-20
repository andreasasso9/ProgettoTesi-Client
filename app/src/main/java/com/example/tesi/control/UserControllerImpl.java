package com.example.tesi.control;

import android.util.Log;

import com.example.tesi.entity.User;
import com.example.tesi.service.UserServiceRetrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserControllerImpl implements UserController{
	private final UserServiceRetrofit userServiceRetrofit;

	public UserControllerImpl() {
		userServiceRetrofit=new Retrofit.Builder()
				.baseUrl("http://10.0.2.2:8080/users/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(UserServiceRetrofit.class);
	}

	public boolean saveUser(String email, String username, String password, String indirizzo) {
		Call<User> saveCall=userServiceRetrofit.saveUser(email, username, password, indirizzo);
		final boolean[] result=new boolean[1];

		Thread saveThread= new Thread(() -> {
			try {
				if (saveCall.execute().body() != null) {
					result[0] = true;
					Log.println(Log.INFO, "SAVE USER", "Save successful");
				}
			} catch (IOException e) {
				Log.println(Log.ERROR, "SAVE USER", "Save failed");
			}
		});
		saveThread.start();
		try {
			saveThread.join();
		} catch (InterruptedException e) {
			return result[0];
		}

		return result[0];
	}

	@Override
	public User loginUser(String username, String password) {
		Call<User> call=userServiceRetrofit.loginUser(username, password);
		final User[] user = new User[1];
		Thread t= new Thread(() -> {
			try {
				user[0] =call.execute().body();
			} catch (IOException e) {
				Log.println(Log.ERROR, "LOGIN USER", "Login error");
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			return null;
		}

		return user[0];
	}
}
