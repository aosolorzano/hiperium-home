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
package com.hiperium.home.test;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.ErrorResponse;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.RemoteAtResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.XBeeTimeoutException;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;

/**
 * This class contains the methods needed to communicate to a remote radio
 * device (XBee), and maintains a continuously listening for events with the
 * coordinator radio device.
 * 
 * @author Andres Solorzano
 * 
 */
public class XBeeTestService implements Runnable, Serializable {

	/** */
	private static final long serialVersionUID = -2492989401111585988L;
	/** The LOGGER property for logger messages. */
	private static final Logger LOGGER = Logger.getLogger(XBeeTestService.class.getName());
	/** */
	private XBee xbee = null;
	
	/**
	 * Default constructor.
	 */
	public XBeeTestService() {
		this.xbee = new XBee();	
	}
	
	/**
	 * 
	 * @param remoteDeviceAddress
	 * @param remoteDevicePinConnection
	 * @param state
	 * @param operationLevelEnabled
	 * @param operationLevel
	 * @return
	 */
	public Boolean sendValue(String remoteDeviceAddress, String remoteDevicePinConnection, 
			Boolean state, Boolean operationLevelEnabled, Integer operationLevel) {
		LOGGER.debug("sendValue - START: " + remoteDeviceAddress);
		Boolean isResponseOK = Boolean.FALSE;
		// replace with SH + SL of your end device
		XBeeAddress64 addr64 = new XBeeAddress64(remoteDeviceAddress);
		// activate or regulate the device
		RemoteAtRequest request = null;
		if(operationLevelEnabled){
			//TODO: PWM OUPUT CONFIGURATION NEEDED
			request = new RemoteAtRequest(addr64, remoteDevicePinConnection, new int[] {2});
		} else if(state){
			request = new RemoteAtRequest(addr64, remoteDevicePinConnection, new int[] {5});
		} else {
			request = new RemoteAtRequest(addr64, remoteDevicePinConnection, new int[] {4});
		}
		
		try {
			RemoteAtResponse remoteAtResponse = (RemoteAtResponse) xbee.sendSynchronous(request, 3000);
			if (remoteAtResponse.isOk()) {
				LOGGER.debug("successfully turned on ("+remoteDevicePinConnection+")");	
				isResponseOK = Boolean.TRUE;
			} else {
				throw new RuntimeException("failed to operate. Status is:" + remoteAtResponse.getStatus());
			}
		} catch (XBeeTimeoutException e) {
			LOGGER.error("Error: " + e);	
		} catch (XBeeException e) {
			LOGGER.error("Error: " + e);
		}
		LOGGER.debug("sendValue - END");
		return isResponseOK;
	}
	
	/**
	 * 
	 */
	public void run() {
		try {			
			this.xbee.open("/dev/ttyUSB0", 9600);
			LOGGER.info("********** run() - STARTED **********");
			while (true) {
				try {
					XBeeResponse response = this.xbee.getResponse(3000);
//					LOGGER.debug("Received i/o response: " + response);
//					LOGGER.debug("packet bytes is " + ByteUtils.toBase16(response.getPacketBytes()));
					if (response.isError()) {
						LOGGER.error("response contains errors", ((ErrorResponse)response).getException());
						continue;
					}
					if (response.getApiId() == ApiId.RX_16_IO_RESPONSE) {
						RxResponseIoSample ioSample = (RxResponseIoSample)response;
						LOGGER.debug("************ Source Address: " + ioSample.getSourceAddress()); 
						// loops just once since IT = 1
						for (IoSample sample: ioSample.getSamples()) {
							
							//TODO: ALL CHANGES DETECTED MUST BE SEND TO THE KRAKEN TOPIC
							if (ioSample.containsAnalog()) {
								LOGGER.debug("Analog pin 20 10-bit reading is: " + sample.getAnalog0());
							} else if (ioSample.containsDigital()){
								// we know it's change detect since analog was not sent
								LOGGER.debug("Digital D1 is: " + (sample.isD1On() ? "on" : "off"));
								LOGGER.debug("Digital D2 is: " + (sample.isD2On() ? "on" : "off"));
								LOGGER.debug("Digital D3 is: " + (sample.isD3On() ? "on" : "off"));
								LOGGER.debug("Digital D4 is: " + (sample.isD4On() ? "on" : "off"));
								LOGGER.debug("Digital D6 is: " + (sample.isD6On() ? "on" : "off"));
								LOGGER.debug("Digital D7 is: " + (sample.isD7On() ? "on" : "off"));
							}
						}
					} else {
						// not what we expected
						LOGGER.debug("Ignoring mystery packet " + response.toString());
					}
				} catch (Exception e) {
					LOGGER.error("Error: " + e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("************ Error: " + e);
		} finally {
			LOGGER.error("************ Error ************ XBEE.CLOSE() ");
			this.xbee.close();
		}
	}
	
	/**
	 * 
	 */
	public void close(){
		if(this.xbee != null){
			this.xbee.close();
		}
	}
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		XBeeTestService xBeeTestService = new XBeeTestService();
		Thread t2 = new Thread(xBeeTestService);
		t2.start();		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		xBeeTestService.sendValue("0 13 A2 0 40 61 9A AB", "D6", true, false, null);
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		xBeeTestService.sendValue("0 13 A2 0 40 61 9A AB", "D6", false, false, null);
	}
}
