package com.tesi.client.service;

import com.tesi.entity.chat.Chat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ChatServiceRetrofit {
	@POST("save")
	Call<ResponseBody> save(@Body Chat chat);

	@POST("findById")
	Call<ResponseBody> findById(@Body String id);

	@POST("delete")
	@FormUrlEncoded
	Call<Boolean> delete(@Field("id") String id, @Field("username") String username);

	@POST("findByUser")
	Call<ResponseBody> findByUser(@Body String user);
}
