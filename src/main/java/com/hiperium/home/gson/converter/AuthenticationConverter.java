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

import com.hiperium.home.dto.HomeAuthResponseDTO;
import com.hiperium.home.gson.GsonConverterUtil;

/**
 * This is a utility class for serializing or deserializing java objects in a
 * JSON format.
 * 
 * @author Andres Solorzano
 */
public final class AuthenticationConverter extends GsonConverterUtil {

	/**
	 * Default constructor.
	 */
	public AuthenticationConverter() {
		super();
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public HomeAuthResponseDTO fromJsonToHomeAuthResponseDTO(String json) {
		return super.getGson().fromJson(json, HomeAuthResponseDTO.class);
	}
}
