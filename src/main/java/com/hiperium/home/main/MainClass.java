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
package com.hiperium.home.main;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hiperium.commons.client.dto.DeviceDTO;
import com.hiperium.commons.client.dto.HomeCredentialDTO;
import com.hiperium.commons.client.exception.InformationException;
import com.hiperium.home.bean.ConfigurationBean;
import com.hiperium.home.common.EnumDeviceClass;
import com.hiperium.home.listener.DeviceMessageListener;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.restful.auth.AuthenticationService;
import com.hiperium.home.restful.device.DeviceService;
import com.hiperium.home.websocket.WebSocketClient;

/**
 * @author Andres Solorzano
 *
 */
public class MainClass {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(MainClass.class);
	
    /**
	 * @param args
	 */
	public static void main(String[] args) throws InformationException {
		LOGGER.info("Home Identifier: " + ConfigurationBean.getHomeId());
		MainClass mainClass = new MainClass();
		mainClass.init();
		synchronized (mainClass) {
			try {
				mainClass.wait();
			} catch (InterruptedException ie) {
				LOGGER.error(ie.getMessage(), ie);
			}
		}
	}
	
	/**
	 * 
	 * @throws InformationException
	 */
	private void init() throws InformationException {
		// **************** CLOUD PLATFORM AUTHENTICATION ****************
		LOGGER.debug("Cloud platform Web Service Authentication - BEGIN");
		HomeCredentialDTO credentialsDTO = new HomeCredentialDTO(ConfigurationBean.getHomeId(), ConfigurationBean.getSerial());
		AuthenticationService authService = AuthenticationService.getInstance();
		ConfigurationBean.setTokenId(authService.homeAuthentication(credentialsDTO));
		LOGGER.debug("Cloud platform Web Service Authentication - END");
		
		// **************** FIND DEVICES BY HOME ID AND STORE THEM LOCALLY ****************
		LOGGER.debug("Getting home devices form Cloud - BEGIN");
		DeviceService deviceService = DeviceService.getInstance();
		List<DeviceDTO> devices = deviceService.findByHomeId(ConfigurationBean.getHomeId(), ConfigurationBean.getTokenId());
		if(devices == null || devices.isEmpty()) {
			throw new InformationException("NOT FOUND DEVICES FOR THIS HOME.");
		}
		Map<Long, DeviceDTO> deviceMap = new HashMap<Long, DeviceDTO>();
		for(DeviceDTO dto : devices) {
			deviceMap.put(dto.getId(), dto);
		}
		LOGGER.debug("Getting home devices form Cloud - END");
		
		// **************** DEVICE LISTENER CONFIGURATION ****************
		LOGGER.debug("Device Listener Configuration - BEGIN");
		DeviceMessageListener deviceMessageListener = new DeviceMessageListener();
		deviceMessageListener.setDeviceMap(deviceMap);
		try {
			WebSocketClient deviceEndPoint = new WebSocketClient(new URI(ConfigurationBean.getWebSocketURI() + ConfigurationBean.getHomeId()));
			deviceEndPoint.addMessageHandler(new WebSocketClient.MessageHandler() {
				public void handleMessage(String message) {
					deviceMessageListener.onMessage(message);
				}
			});
			deviceMessageListener.setSocketClient(deviceEndPoint);
			deviceMessageListener.startXbeePackageListener();
		} catch (Exception e) {
			throw new InformationException(e.getMessage());
		}
		LOGGER.debug("Device Listener Configuration - END");
				
		// INITIALIZES ACTUATOR DEVICES TO STATUS OBTAINED FORM CLOUD PLATFORM 
		EnumDeviceClass deviceClass = null;
		for(DeviceDTO dto : devices) {
			deviceClass = EnumDeviceClass.decodeByEnumName(dto.getDevClass());
			if(EnumDeviceClass.ACTUATOR.equals(deviceClass)) {
				deviceMessageListener.sendMessageToDevice(dto);
			}
		}
	}
	
}
