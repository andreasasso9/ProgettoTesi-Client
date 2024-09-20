package com.tesi.client.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tesi.client.service.ChatServiceRetrofit;
import com.tesi.client.utils.ChatDeserializer;
import com.tesi.entity.chat.Chat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatControllerImpl implements ChatController {
	private final ChatServiceRetrofit chatServiceRetrofit;
	private static ChatController instance;//todo completare implementazione salvataggio chat su database

	private ChatControllerImpl() {
		chatServiceRetrofit=new Retrofit.Builder()
				.baseUrl("http://10.0.2.2:8080/chat/")
				.addConverterFactory(GsonConverterFactory.create())
				.build()
				.create(ChatServiceRetrofit.class);
	}

	public static ChatController getInstance() {
		if (instance==null)
			instance=new ChatControllerImpl();
		return instance;
	}

	@Override
	public Chat save(Chat chat) {
		Gson gson=new GsonBuilder().registerTypeAdapter(Chat.class, new ChatDeserializer()).create();

		Call<ResponseBody> call=chatServiceRetrofit.save(chat);
		CompletableFuture<Chat> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							return gson.fromJson(jsonResponse, Chat.class);
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
	public Chat findById(String id) {
		Gson gson=new GsonBuilder().registerTypeAdapter(Chat.class, new ChatDeserializer()).create();

		Call<ResponseBody> call=chatServiceRetrofit.findById(id);
		CompletableFuture<Chat> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							return gson.fromJson(jsonResponse, Chat.class);
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
	public boolean delete(String id, String username) {
		Call<Boolean> call=chatServiceRetrofit.delete(id, username);
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
			return false;
		}
	}

	@Override
	public List<Chat> findByUser(String user) {
		Gson gson=new GsonBuilder().registerTypeAdapter(Chat.class, new ChatDeserializer()).create();

		Call<ResponseBody> call=chatServiceRetrofit.findByUser(user);
		CompletableFuture<List<Chat>> future=CompletableFuture.supplyAsync(()->{
			try {
				Response<ResponseBody> response=call.execute();
				if (response.isSuccessful()) {
					try (ResponseBody body=response.body()) {
						if (body != null) {
							String jsonResponse=body.string();

							Type chatListType=new TypeToken<List<Chat>>() {}.getType();
							return gson.fromJson(jsonResponse, chatListType);
						}
					}
				}
				return new LinkedList<>();
			} catch (IOException e) {
				return new LinkedList<>();
			}
		});

		try {
			return future.get();
		} catch (ExecutionException | InterruptedException e) {
			return new LinkedList<>();
		}
	}
}
