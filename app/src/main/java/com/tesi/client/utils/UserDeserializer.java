package com.tesi.client.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tesi.entity.User;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.UUID;

public class UserDeserializer implements JsonDeserializer<User> {
	@Override
	public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();

		UUID id=UUID.fromString(jsonObject.get("id").getAsString());
		String username=jsonObject.get("username").getAsString();
		String email=jsonObject.get("email").getAsString();
		String password=jsonObject.get("password").getAsString();
		String indirizzo=jsonObject.get("indirizzo").getAsString();
		//byte[] foto=jsonObject.get("foto").getAsBigInteger().toByteArray();

		User user=new User(email, username, password, indirizzo);
		user.setId(id);
		//user.setFoto(foto);

		return user;
	}
}
