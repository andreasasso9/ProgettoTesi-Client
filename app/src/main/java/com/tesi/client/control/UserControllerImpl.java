package com.tesi.client.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tesi.client.utils.ByteArrayDeserializer;
import com.tesi.client.utils.UserDeserializer;
import com.tesi.entity.User;
import com.tesi.client.service.UserServiceRetrofit;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserControllerImpl implements UserController{
	private static UserController instance;
	private final UserServiceRetrofit userServiceRetrofit;

	private UserControllerImpl() {
		userServiceRetrofit=new Retrofit.Builder()
				.baseUrl("http://10.0.2.2:8080/users/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(UserServiceRetrofit.class);
	}

	public static UserController getInstance() {
		if (instance==null)
			instance=new UserControllerImpl();
		return instance;
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
			return false;
		}
	}

	@Override
	public User loginUser(String username, String password) {
		Gson gson=new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();

		Call<ResponseBody> call=userServiceRetrofit.loginUser(username, password);
		CompletableFuture<User> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							return gson.fromJson(jsonResponse, User.class);
						}
					}
				}
				return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return null;
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
		Gson gson=new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();

		Call<ResponseBody> call=userServiceRetrofit.findById(id);
		CompletableFuture<User> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							return gson.fromJson(jsonResponse, User.class);
						}
					}
				}
				return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return null;
		}
	}

	@Override
	public boolean checkEmail(String email) {
		Call<Boolean> call=userServiceRetrofit.checkEmail(email);
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
	public byte[] findFoto(String username) {
		Gson gson=new GsonBuilder().registerTypeAdapter(User.class, new ByteArrayDeserializer()).create();

		Call<ResponseBody> call=userServiceRetrofit.findFoto(username);
		CompletableFuture<byte[]> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							return gson.fromJson(jsonResponse, byte[].class);
						}
					}
				}
				return null;
			} catch (IOException e) {
				return null;
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return null;
		}
	}


}
