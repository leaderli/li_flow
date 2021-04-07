package com.leaderli.li.flow.util;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	
	public static String toJson(Object source) {
		return GSON.toJson(source);
	}
	
	public static <T> T fromJson(String json,Class<T> klass) {
		return GSON.fromJson(json, klass);
	}
	public static <T> T fromJson(InputStream json,Class<T> klass) {
		return GSON.fromJson(new InputStreamReader(json), klass);
	}


}

