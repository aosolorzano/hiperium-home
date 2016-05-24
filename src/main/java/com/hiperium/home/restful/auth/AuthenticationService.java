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
package com.hiperium.home.restful.auth;

import com.hiperium.commons.client.dto.HomeCredentialDTO;
import com.hiperium.commons.client.exception.InformationException;
import com.hiperium.commons.client.gson.GsonConverterUtil;
import com.hiperium.commons.client.http.HttpClient;
import com.hiperium.home.bean.ConfigurationBean;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.restful.RestIdentityPath;

/**
 * This service class send calls to the REST Service to operate with different
 * actions originated from the system.
 * 
 * @author Andres Solorzano
 * 
 */
public final class AuthenticationService extends HttpClient {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(AuthenticationService.class);
	
	/** The property converter. */
	private GsonConverterUtil converter;
	
	/** The property service. */
	private static AuthenticationService service = null;
	
	/**
	 * Class constructor.
	 */
	private AuthenticationService() {
		this.converter = new GsonConverterUtil();
	}
	
	/**
	 * Return the singleton instance.
	 * 
	 * @return
	 */
	public static AuthenticationService getInstance() {
		if(service == null) {
			service = new AuthenticationService();
		}
		return service;
	}
	
	/**
	 * 
	 * @param credentialsDTO
	 * @return
	 * @throws InformationException
	 */
	public String homeAuthentication(HomeCredentialDTO credentialsDTO) throws InformationException {
		LOGGER.debug("homeAuthentication - START");
		String response = null;
		try {
			response = super.postToService(
					ConfigurationBean.PROPERTIES.getProperty("hiperium.identity.service.url").concat(RestIdentityPath.homeAuthentication()), 
					this.converter.toJSON(credentialsDTO), "application/json", "");
		} catch (InformationException e) {
			throw e;
		}
		return response;
	}
}
