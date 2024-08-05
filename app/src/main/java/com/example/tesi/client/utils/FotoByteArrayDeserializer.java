package com.example.tesi.client.utils;

import com.example.tesi.entity.FotoByteArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Base64;

public class FotoByteArrayDeserializer implements JsonDeserializer<FotoByteArray> {
	@Override
	public FotoByteArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();
		FotoByteArray foto=new FotoByteArray();

		String valueBase64=jsonObject.get("value").getAsString();
		byte[] value= Base64.getDecoder().decode(valueBase64);
		foto.setValue(value);

		return foto;
	}
}
