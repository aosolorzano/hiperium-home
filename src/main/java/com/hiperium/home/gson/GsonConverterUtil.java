/**
 * Product  : Hiperium Project
 * Architect: Andres Solorzano.
 * Created  : 08-05-2009 - 23:30:00
 * 
 * The contents of this file are copyrighted by Andres Solorzano 
 * and it is protected by the license: "GPL V3." You can find a copy of this 
 * license at: http://www.hiperium.com/about/licence.html
 * 
 * Copyright 2014 Andres Solorzano. All rights reserved.
 * 
 */
package com.hiperium.home.gson;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This is a utility class for serializing or java objects in a
 * JSON format.
 * 
 * @author Andres Solorzano
 * 
 */
public class GsonConverterUtil {

	/** */
	private Gson gson;

	/**
	 * Default constructor.
	 */
	public GsonConverterUtil() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			public JsonElement serialize(Date date, Type typeOfT,
					JsonSerializationContext context) {
				return new JsonPrimitive(date.getTime());
			}
		});
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			public Date deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		this.gson = builder.serializeNulls().setPrettyPrinting().create();
	}

	/**
	 * 
	 * @param <T>
	 * @param object
	 * @return
	 */
	public <T> String toJSON(T object) {
		return this.gson.toJson(object, object.getClass());
	}

	/**
	 * 
	 * @param <T>
	 * @param object
	 * @return
	 */
	public <T> String toJSON(List<T> object) {
		return this.gson.toJson(object, object.getClass());
	}

	/**
	 * Return the GSON object instance.
	 * 
	 * @return the GSON object instance.
	 */
	public Gson getGson() {
		return gson;
	}
}
