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
package com.hiperium.home.listener;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import com.hiperium.home.common.EnumDeviceType;
import com.hiperium.home.dto.DeviceDTO;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.main.MainClass;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.api.wpan.TxRequest16;
import com.rapplogic.xbee.api.wpan.TxStatusResponse;


/**
 * This class represents a Message Driven Bean that gets a message from the cloud server for
 * any Internet user smart home administration.
 * 
 * @author Andres Solorzano
 * 
 */
public class DeviceDataListener implements PacketListener {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(DeviceDataListener.class);

	/** The property deviceMap. */
	private Map<Long, DeviceDTO> deviceMap;
	/** The property xbee. */
	private XBee xbee;
	/** The property messageListener. */
	private DeviceMessageListener messageListener;
	
	/** The property data. */
	private int[] data;
	/** The property payload. */
	private int[] payload;
	/** The property deviceId. */
	private Integer deviceId;
	
	/**
	 * 
	 * @param properties
	 */
	public DeviceDataListener() {
		this.xbee = new XBee();
		this.data = new int[3];
		this.payload = new int[2];
		this.deviceMap = new HashMap<Long, DeviceDTO>();
	}
	
	/**
	 * Start the listening.
	 * @throws XBeeException 
	 */
	public void start() throws XBeeException {
		LOGGER.debug("start() - START");
		this.xbee.open("/dev/ttyUSB0", 9600);
		this.xbee.addPacketListener(this);
		LOGGER.info("DeviceDataListener Ready To Receive XBee Messages...");
	}
	
	/*
	 * Listen for every XBee change and send it to the Hiperium Cloud via JMS. 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */ 
	@Override
	public void processResponse(XBeeResponse response) {
		LOGGER.debug("processResponse - START");
		if(this.messageListener == null) {
			LOGGER.error("DEVICE MESSAGE LISTENER IS NULL.");
			return;
		}
		if (response.getApiId() == ApiId.RX_16_RESPONSE) {
			RxResponse16 response16 = (RxResponse16) response;
			this.data = response16.getData();
			for(int i = 0; i < this.data.length; i++) {
				LOGGER.debug("Data["+i+"]: " + this.data[i]);
			}
			this.deviceId = this.data[0];
			DeviceDTO dto = this.deviceMap.get(this.deviceId.longValue());
			if(dto == null) {
				LOGGER.error("DEVICE NOT FOUND WITH ID: " + this.data[0]);
			} else {
				// ONLY THIS OBJECT MUST SET THE APPLICATION TOKEN
				dto.setTokenId(MainClass.TOKEN_ID);
				if(this.data[1] == 0) { // IF DEVICE HAS A VALUE 0-100
					dto.setStatus(this.data[2] == 0? false : true);
				} else { 				// IF DEVICE HAS A STATUS 1/0	
					dto.setValue(this.data[2]);
				}
				this.messageListener.sendMessage(dto);
				this.deviceMap.put(dto.getId(), dto);
			}
		}
		LOGGER.debug("processResponse - END");
	}
	
	/**
	 * 
	 * @param dto
	 */
	public void sendValueToXBee(DeviceDTO dto) {
		LOGGER.debug("sendValue - START: " + dto);
//		TODO: XBeeAddress16 destination = new XBeeAddress16(dto.getXbee16bitsMSB(), dto.getXbee16bitsLSB());
		XBeeAddress16 destination = new XBeeAddress16(0x56, 0x78);
		this.payload[0] = dto.getPinId();
		EnumDeviceType type = EnumDeviceType.decodeByEnumName(dto.getDevType());
		switch (type) {
		case ELECTRIC_LIGHT: // DEVICE OPERATION IN REVERSE SENSE
			this.payload[1] = dto.getStatus()? 0 : 1;
			break;
		default:
			this.payload[1] = dto.getStatus()? 1 : 0;
			break;
		}
		TxRequest16 tx = new TxRequest16(destination, this.payload);
		TxStatusResponse status;
		try {
			status = (TxStatusResponse) this.xbee.sendSynchronous(tx);
			if (status.isSuccess()) {
				LOGGER.debug("MESSAGE DELIVERED SUCCESSFUL");
				this.deviceMap.put(dto.getId(), dto);
			}
		} catch (XBeeException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.debug("sendValue - END");
	}
	
	/**
	 * Close the connection releasing the resources.
	 */
	@PreDestroy
	public void close() {
		if(this.xbee != null){
			this.xbee.close();
		}
	}

	/**
	 * @return the messageListener
	 */
	public DeviceMessageListener getMessageListener() {
		return messageListener;
	}

	/**
	 * @param messageListener
	 *            the messageListener to set
	 */
	public void setMessageListener(DeviceMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	/**
	 * @return the deviceMap
	 */
	public Map<Long, DeviceDTO> getDeviceMap() {
		return deviceMap;
	}

	/**
	 * @param deviceMap
	 *            the deviceMap to set
	 */
	public void setDeviceMap(Map<Long, DeviceDTO> deviceMap) {
		this.deviceMap = deviceMap;
	}

}
