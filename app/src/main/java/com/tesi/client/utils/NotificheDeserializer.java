package com.tesi.client.utils;

import com.tesi.entity.Notifica;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Base64;

public class NotificheDeserializer implements JsonDeserializer<Notifica> {
	@Override
	public Notifica deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject=json.getAsJsonObject();
		Notifica notifica=new Notifica();

		notifica.setId(jsonObject.get("id").getAsLong());
		notifica.setSender(jsonObject.get("sender").getAsString());
		notifica.setReceiver(jsonObject.get("receiver").getAsString());
		notifica.setDescrizione(jsonObject.get("descrizione").getAsString());

		String valueBase64=jsonObject.get("foto").getAsString();
		byte[] foto= Base64.getDecoder().decode(valueBase64);
		notifica.setFoto(foto);

		return notifica;
	}
}
