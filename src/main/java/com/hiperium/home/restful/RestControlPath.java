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
package com.hiperium.home.restful;



/**
 * @author Andres Solorzano
 *
 */
public final class RestControlPath extends RestServicePath {

	/** The FIND_DEVICE_BY_HOME_ID property path. */
	public static final String FIND_DEVICE_BY_HOME_ID = "/findByHomeId";
	
	/**
	 * Get the device by zone identifier path.
	 * 
	 * @return the device by zone identifier path.
	 */
	public static String findDeviceByHomeId() {
		return RestServicePath.CONTROL_CONTEXT_ROOT.concat(RestServicePath.CONTROL_APPLICATION_PATH).concat(RestServicePath.GATEWAY).concat(FIND_DEVICE_BY_HOME_ID);
	}
}
