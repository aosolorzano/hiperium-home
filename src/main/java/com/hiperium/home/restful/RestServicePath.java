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
	 * The property IDENTITY_CONTEXT_ROOT with path /hiperium-identity.
	 */
	public static final String IDENTITY_CONTEXT_ROOT = "/hiperium-identity";
	
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
	 * The property APPLICATION_PATH with path /api/identity.
	 */
	public static final String IDENTITY_APPLICATION_PATH = "/api/identity";
	
	/**
	 * The property AUTHENTICATION with REST path /authentication.
	 */
	public static final String AUTHENTICATION = "/authentication";
	
	
	// ***************************************************************** //
	// ***************************************************************** //
	// ********************* CONTROL FUNCTIONALITIES ******************* //
	// ***************************************************************** //
	// ***************************************************************** //
	/**
	 * The property APPLICATION_PATH with path /api/identity.
	 */
	public static final String CONTROL_APPLICATION_PATH = "/api/control";
	
	/**
	 * The property GATEWAY with REST path /gateway.
	 */
	public static final String GATEWAY = "/gateway";
	
}
