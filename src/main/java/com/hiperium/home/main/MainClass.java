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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.Context;

import com.hiperium.home.common.EnumDeviceClass;
import com.hiperium.home.dto.DeviceDTO;
import com.hiperium.home.dto.HomeAuthResponseDTO;
import com.hiperium.home.dto.HomeCredentialDTO;
import com.hiperium.home.exception.InformationException;
import com.hiperium.home.gson.GsonConverterUtil;
import com.hiperium.home.listener.DeviceDataListener;
import com.hiperium.home.listener.DeviceMessageListener;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.restful.auth.AuthenticationService;
import com.hiperium.home.restful.control.DeviceService;
import com.rapplogic.xbee.api.XBeeException;

/**
 * @author Andres Solorzano
 *
 */
public class MainClass {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(MainClass.class);
	
	/** The GSON_CONVERTER_UTIL object for JSON conversion. */
	public static final GsonConverterUtil GSON_CONVERTER_UTIL = new GsonConverterUtil();
	
	/** The property DEFAULT_CONNECTION_FACTORY. */
    public static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
   
    /** The property PROPERTIES. */
    public static final Properties PROPERTIES = new Properties();

	/** The HOME_ID property. */
	public static Long HOME_ID = null;
	/** The TOKEN_ID property for messages. */
	public static String TOKEN_ID = null;
	
	/**
	 * Class initialization
	 */
	static {
		// Set up the namingContext for the JNDI lookup
		try {
			PROPERTIES.load(MainClass.class.getClassLoader().getResourceAsStream("home.properties"));
			String home = PROPERTIES.getProperty("homeId");
			HOME_ID = Long.valueOf(home);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @throws InformationException 
	 * 
	 */
	public MainClass() throws InformationException {
		// **************** CLOUD PLATFORM CONNECTION ****************
		LOGGER.debug("Cloud platform Web Service connection - BEGIN");
		HomeCredentialDTO credentialsDTO = new HomeCredentialDTO(HOME_ID, PROPERTIES.getProperty("serial"));
		AuthenticationService authService = AuthenticationService.getInstance();
		HomeAuthResponseDTO credentialDTO = authService.homeAuthentication(credentialsDTO, "");
		PROPERTIES.put(Context.SECURITY_PRINCIPAL, credentialDTO.getParam1());
		PROPERTIES.put(Context.SECURITY_CREDENTIALS, credentialDTO.getParam2());
		TOKEN_ID = credentialDTO.getParam3();
		LOGGER.debug("Cloud platform Web Service connection - END");
		
		// **************** FIND DEVICES BY HOME ID AND STORE THEM LOCALLY ****************
		LOGGER.debug("Getting home devices form Cloud - BEGIN");
		DeviceService deviceService = DeviceService.getInstance();
		List<DeviceDTO> devices = deviceService.findByHomeId(HOME_ID, TOKEN_ID);
		Map<Long, DeviceDTO> deviceMap = new HashMap<Long, DeviceDTO>();
		if(devices == null || devices.isEmpty()) {
			throw new InformationException("DID NOT FOUND ANY DEVICE FOR THIS HOME.");
		} else {
			for(DeviceDTO dto : devices) {
				deviceMap.put(dto.getId(), dto);
			}
		}
		LOGGER.debug("Getting home devices form Cloud - END");
		
		// **************** DEVICE CLOUD CONNECTION ****************
		LOGGER.debug("Cloud device JMS connection - BEGIN");
		DeviceMessageListener deviceMessageListener = null;
		try {
			deviceMessageListener = new DeviceMessageListener(PROPERTIES);
			deviceMessageListener.start();
		} catch (JMSException e) {
			LOGGER.error(e.getMessage(), e);
			throw new InformationException(e.getMessage());
		}
		LOGGER.debug("Cloud device JMS connection - END");
				
		// **************** DEVICE XBEE CONNECTION ****************
		LOGGER.debug("Device XBee Coordinator connection - BEGIN");
		DeviceDataListener dataListener = null;
		try {
			dataListener = new DeviceDataListener();
			dataListener.setMessageListener(deviceMessageListener);
			dataListener.setDeviceMap(deviceMap);
			dataListener.start();
			deviceMessageListener.setDataListener(dataListener);
		} catch (XBeeException e) {
			LOGGER.error(e.getMessage(), e);
			throw new InformationException(e.getMessage());
		}
		LOGGER.debug("Device XBee Coordinator connection - END");
		
		// INITIALIZES ACTUATOR DEVICES TO THE ACTUAL STATUS
		EnumDeviceClass deviceClass = null;
		for(DeviceDTO dto : devices) {
			deviceClass = EnumDeviceClass.decodeByEnumName(dto.getDevClass());
			if(EnumDeviceClass.ACTUATOR.equals(deviceClass)) {
				dataListener.sendValueToXBee(dto);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws InformationException {
		LOGGER.info("Home Identifier: " + HOME_ID);
		MainClass mainClass = new MainClass();
		synchronized (mainClass) {
			try {
				mainClass.wait();
			} catch (InterruptedException ie) {
				LOGGER.error(ie.getMessage(), ie);
			}
		}
	}
}
