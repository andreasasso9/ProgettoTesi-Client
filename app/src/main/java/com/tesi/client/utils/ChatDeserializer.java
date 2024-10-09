package com.tesi.client.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tesi.entity.chat.Chat;
import com.tesi.entity.chat.Image;
import com.tesi.entity.chat.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatDeserializer implements JsonDeserializer<Chat> {
	@Override
	public Chat deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();

		String id=jsonObject.get("id").getAsString();

		String user1=jsonObject.get("user1").getAsString();
		String user2=jsonObject.get("user2").getAsString();

		JsonArray textsArray = jsonObject.getAsJsonArray("texts");
		List<Text> texts = new ArrayList<>();
		Gson gson=new Gson();

		for (JsonElement element : textsArray) {
			Text text = gson.fromJson(element, Text.class);
			if (text.getText()==null) {
				Image image = gson.fromJson(element, Image.class);
				texts.add(image);
			}else
				texts.add(text);
		}

		return new Chat(texts, id, user1, user2);

	}
}
