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
public final class RestIdentityPath extends RestServicePath {

	/** The HOME_AUTH property path. */
	public static final String HOME_AUTH = "/homeAuthentication";
	
	/**
	 * 
	 * @return
	 */
	public static String homeAuthentication() {
		return RestServicePath.IDENTITY_CONTEXT_ROOT.concat(RestServicePath.IDENTITY_APPLICATION_PATH).concat(RestServicePath.AUTHENTICATION).concat(HOME_AUTH);
	}
	
}
