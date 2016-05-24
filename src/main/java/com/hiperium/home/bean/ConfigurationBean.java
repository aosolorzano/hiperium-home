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
package com.hiperium.home.bean;

import java.util.Properties;

import com.hiperium.home.logger.HiperiumLogger;

/**
 * 
 * @author Andres Solorzano
 * @version 1.0
 */
public class ConfigurationBean {
	
	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(ConfigurationBean.class);
	
	/** The MESSAGE_SERVICE_URL property path. */
	public static final String MESSAGE_SERVICE_URI = "hiperium.messaging.service.uri";
	/** The IDENTITY_SERVICE_URL property path. */
	public static final String IDENTITY_SERVICE_URL = "hiperium.identity.service.url";
	/** The DEVICE_SERVICE_URL property path. */
	public static final String DEVICE_SERVICE_URL = "hiperium.device.service.url";
	
	/** The property PROPERTIES. */
    public static final Properties PROPERTIES = new Properties();
    
    /** The HOME_ID property. */
	public static Long homeId = null;
	/** The SERIAL_ID property. */
	public static String serial = null;
	/** The TOKEN_ID property for messages. */
	public static String tokenId = null;
	
	/**
	 * Class initialization
	 */
	static {
		// Set up the namingContext for the JNDI lookup
		try {
			PROPERTIES.load(ConfigurationBean.class.getClassLoader().getResourceAsStream("common.properties"));
			String home = PROPERTIES.getProperty("homeId");
			homeId = Long.valueOf(home);
			serial = PROPERTIES.getProperty("serial");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public static String getWebSocketURI() {
		return PROPERTIES.getProperty(MESSAGE_SERVICE_URI);
	}

	/**
	 * @return the tokenId
	 */
	public static String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public static void setTokenId(String tokenId) {
		ConfigurationBean.tokenId = tokenId;
	}

	/**
	 * @return the homeId
	 */
	public static Long getHomeId() {
		return homeId;
	}

	/**
	 * @return the serial
	 */
	public static String getSerial() {
		return serial;
	}
}
