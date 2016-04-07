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
package com.hiperium.home.common;

import java.util.Properties;

import com.hiperium.home.logger.HiperiumLogger;

/**
 * 
 * @author Andres Solorzano
 * @version 1.0
 */
public class Resources {
	
	/** The MESSAGE_SERVICE_HOST property path. */
	public static final String MESSAGE_SERVICE_HOST = "hiperium.messaging.service.host";
	/** The DEVICE_CLOUD_TOPIC property path. */
	public static final String CLIENT_DEVICE_TOPIC = "deviceTopic";
	/** The CLIENT_DEVICE_QUEUE property path. */
	public static final String CLIENT_DEVICE_QUEUE = "deviceQueue";
	
	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(Resources.class);
	
	/** The property PROPERTIES. */
    public static final Properties PROPERTIES = new Properties();
    
	/**
	 * Class initialization
	 */
	static {
		// Set up the namingContext for the JNDI lookup
		try {
			PROPERTIES.load(Resources.class.getClassLoader().getResourceAsStream("home.properties"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
