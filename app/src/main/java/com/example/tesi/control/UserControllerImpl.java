package com.example.tesi.control;

import com.example.tesi.entity.User;
import com.example.tesi.service.UserServiceRetrofit;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
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
		CompletableFuture<User> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<User> response= saveCall.execute();
				if (response.isSuccessful())
					return response.body();

				throw new RuntimeException("SAVE USER FAILED");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		try {
			return future.get() != null;
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public User loginUser(String username, String password) {
		Call<User> call=userServiceRetrofit.loginUser(username, password);
		CompletableFuture<User> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<User> response=call.execute();
				if (response.isSuccessful())
					return response.body();

				return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}

	@Override
	public void miPiace(UUID idUser, Long idProdotto) {
		Call<Boolean> call=userServiceRetrofit.miPiace(idUser, idProdotto);
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
			future.get();
		} catch (InterruptedException | ExecutionException e) {
		}
	}

	@Override
	public boolean update(User user) {
		Call<Boolean> call=userServiceRetrofit.update(user);
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
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}

	@Override
	public User findById(UUID id) {
		Call<User> call=userServiceRetrofit.findById(id);
		CompletableFuture<User> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<User> response=call.execute();
				if (response.isSuccessful())
					return response.body();
				return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}

}
