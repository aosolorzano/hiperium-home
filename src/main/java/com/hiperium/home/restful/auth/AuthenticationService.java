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

import org.apache.commons.lang.StringUtils;

import com.hiperium.commons.client.dto.HomeCredentialDTO;
import com.hiperium.commons.client.dto.HomeResponseDTO;
import com.hiperium.commons.client.exception.InformationException;
import com.hiperium.commons.client.http.HttpClient;
import com.hiperium.home.common.bean.ConfigurationBean;
import com.hiperium.home.common.converter.HomeResponseConverter;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.restful.RestSecurityPath;

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
	private HomeResponseConverter converter;
	
	/** The property service. */
	private static AuthenticationService service = null;
	
	/**
	 * Class constructor.
	 */
	private AuthenticationService() {
		this.converter = new HomeResponseConverter();
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
	 * @param token
	 * @return
	 */
	public HomeResponseDTO homeAuthentication(HomeCredentialDTO credentialsDTO, String token) {
		LOGGER.debug("homeAuthentication - START");
		String response;
		try {
			response = super.postToService(ConfigurationBean.PROPERTIES.getProperty("hiperium.security.service.url")
					.concat(RestSecurityPath.homeAuthentication()), this.converter.toJSON(credentialsDTO), 
							"application/json", token);
			if(StringUtils.isNotBlank(response)) {
				HomeResponseDTO obj = this.converter.fromJsonToHomeResponseDTO(response);
				return obj;
			}
		} catch (InformationException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
}
