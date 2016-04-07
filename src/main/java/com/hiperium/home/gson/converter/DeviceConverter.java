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
package com.hiperium.home.gson.converter;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hiperium.home.dto.DeviceDTO;
import com.hiperium.home.gson.GsonConverterUtil;
import com.hiperium.home.logger.HiperiumLogger;

/**
 * This is a utility class for serializing or deserializing java objects in a
 * JSON format.
 * 
 * @author Andres Solorzano
 */
public final class DeviceConverter extends GsonConverterUtil {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(DeviceConverter.class);

	/**
	 * Default constructor.
	 */
	public DeviceConverter() {
		super();
	}
	
	/**
	 * Returns a specific object implementation from a JSON.
	 * 
	 * @param jsonData
	 *            Single object to be parsed.
	 * @return a specific object implementation from a JSON.
	 */
	public DeviceDTO fromJsonToDeviceDTO(String jsonData) {
		DeviceDTO register = null;
		try {
			register = super.getGson().fromJson(jsonData, DeviceDTO.class);
		} catch (JsonSyntaxException e) {
			LOGGER.error("fromJsonToDeviceActivationDTO() - ERROR: " + e.getMessage());
		}
		return register;
	}
	
	/**
	 * Returns a specific object implementation from a JSON.
	 * 
	 * @param jsonData
	 *            Single object to be parsed.
	 * @return a specific object implementation from a JSON.
	 */
	public List<DeviceDTO> fromJsonToDeviceList(String jsonData) {
		Type type = new TypeToken<List<DeviceDTO>>() {}.getType();
		List<DeviceDTO> list = null;
		try {
			list = super.getGson().fromJson(jsonData, type);
		} catch (JsonSyntaxException e) {
			LOGGER.error("fromJsonToDeviceList() - ERROR: " + e.getMessage());
		}
		return list;
	}
}
