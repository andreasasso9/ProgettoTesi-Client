package com.example.tesi.client.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class File {
	public static void saveObjectToFile(Context context, String fileName, Object object) {
		try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object readObjectFromFile(Context context, String fileName) {
		Object object;
		try (FileInputStream fis = context.openFileInput(fileName);
		     ObjectInputStream ois = new ObjectInputStream(fis)) {
			object = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
		return object;
	}

	public static boolean deleteFile(Context context, String fileName) {
		return context.deleteFile(fileName);
	}
}
