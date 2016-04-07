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
public class RestServicePath {

	/**
	 * The property APPLICATION_PATH with path /web/service/rest.
	 */
	public static final String APPLICATION_PATH = "/web/service/rest";
	
	/**
	 * The property SECURITY_CONTEXT_ROOT with path /hiperium-security.
	 */
	public static final String SECURITY_CONTEXT_ROOT = "/hiperium-security";
	
	/**
	 * The property SECURITY_CONTEXT_ROOT with path /hiperium-security.
	 */
	public static final String CONTROL_CONTEXT_ROOT = "/hiperium-control";

	
	// ***************************************************************** //
	// ***************************************************************** //
	// ***************** AUTHENTICATION FUNCTIONALITIES **************** //
	// ***************************************************************** //
	// ***************************************************************** //
	/**
	 * The property AUTHENTICATION with REST path /security/authentication.
	 */
	public static final String AUTHENTICATION = "/security/authentication";
	
	
	// ***************************************************************** //
	// ***************************************************************** //
	// ********************* CONTROL FUNCTIONALITIES ******************* //
	// ***************************************************************** //
	// ***************************************************************** //
	/**
	 * The property DEVICES with REST path /control/device.
	 */
	public static final String DEVICES = "/control/device";
	
}
