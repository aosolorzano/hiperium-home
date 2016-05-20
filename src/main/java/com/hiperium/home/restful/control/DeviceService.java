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
package com.hiperium.home.restful.control;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hiperium.commons.client.dto.DeviceDTO;
import com.hiperium.commons.client.exception.InformationException;
import com.hiperium.commons.client.http.HttpClient;
import com.hiperium.home.common.bean.ConfigurationBean;
import com.hiperium.home.common.converter.DeviceConverter;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.restful.RestControlPath;

/**
 * This service class send calls to the REST Service to operate with different
 * actions originated from the system.
 * 
 * @author Andres Solorzano
 * 
 */
public final class DeviceService extends HttpClient {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(DeviceService.class);
	
	/** The property converter. */
	private DeviceConverter converter;
	
	/** The property service. */
	private static DeviceService service = null;
	
	/**
	 * Class con
	 */
	private DeviceService() {
		this.converter = new DeviceConverter();
	}
	
	/**
	 * Return the singleton instance.
	 * 
	 * @return
	 */
	public static DeviceService getInstance() {
		if(service == null) {
			service = new DeviceService();
		}
		return service;
	}
	
	/**
	 * 
	 * @param homeId
	 * @param token
	 * @return
	 */
	public List<DeviceDTO> findByHomeId(Long homeId, String token) {
		LOGGER.debug("findByHomeId - START");
		String response;
		try {
			String URL = ConfigurationBean.PROPERTIES.getProperty("hiperium.control.service.url").concat(RestControlPath.findDeviceByHomeId());
			response = super.getFromService(URL.concat("?homeId=").concat(homeId.toString()), "application/json", token);
			if(StringUtils.isNotBlank(response)) {
				List<DeviceDTO> devices = this.converter.fromJsonToDeviceList(response);
				return devices;
			}
		} catch (InformationException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
}
