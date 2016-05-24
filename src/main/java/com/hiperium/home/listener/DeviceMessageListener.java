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

import com.hiperium.commons.client.dto.DeviceDTO;
import com.hiperium.home.bean.ConfigurationBean;
import com.hiperium.home.converter.DeviceConverter;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.websocket.WebSocketClient;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.RxResponse16;


/**
 * This class represents a Message Driven Bean that gets a message from the cloud server for
 * any Internet user smart home administration.
 * 
 * @author Andres Solorzano
 * 
 */
public class DeviceMessageListener implements PacketListener {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(DeviceMessageListener.class);

	/** The property deviceMap. */
	private Map<Long, DeviceDTO> deviceMap;
	/** The property xbee. */
	private XBee xbee;
	
	/** The property data. */
	private int[] data;
	/** The property payload. */
//	private int[] payload;
	/** The property deviceId. */
	private Integer deviceId;
	
	/** The property jsonConverter. */
	private DeviceConverter converter;
	
	/** The property webSocketClient. */
	private WebSocketClient webSocketClient;
	
	/**
	 * Class constructor.
	 */
	public DeviceMessageListener() {
		this.xbee = new XBee();
		this.data = new int[3];
//		this.payload = new int[2];
		this.deviceMap = new HashMap<Long, DeviceDTO>();
		this.converter = new DeviceConverter();
		this.webSocketClient = null;
	}
	
	/**
	 * Start XBEE device listening.
	 * 
	 * @throws XBeeException 
	 */
	public void startXbeePackageListener() throws XBeeException {
		LOGGER.debug("startXbeePackageListener() - START");
//		this.xbee.open("/dev/ttyUSB0", 9600);
//		this.xbee.addPacketListener(this);
		LOGGER.debug("startXbeePackageListener() - END");
	}
	
	/**
	 * Receives messages from WebSockets in the Hiperium Cloud Platform.
	 * 
	 * @param message
	 */
	public void onMessage(String message) {
		LOGGER.debug("onMessage - START");
		DeviceDTO deviceDTO = this.converter.fromJsonToDeviceDTO(message);
		this.sendMessageToDevice(deviceDTO);
		LOGGER.debug("onMessage - END");
	}
	
	/**
	 * 
	 * @param dto
	 */
	public void sendMessageToDevice(DeviceDTO dto) {
		LOGGER.debug("sendMessageToDevice - START: " + dto);
//		TODO: XBeeAddress16 destination = new XBeeAddress16(dto.getXbee16bitsMSB(), dto.getXbee16bitsLSB());
//		XBeeAddress16 destination = new XBeeAddress16(0x56, 0x78);
//		this.payload[0] = dto.getPinId();
//		EnumDeviceType type = EnumDeviceType.decodeByEnumName(dto.getDevType());
//		switch (type) {
//		case ELECTRIC_LIGHT: // DEVICE OPERATION IN REVERSE SENSE
//			this.payload[1] = dto.getStatus()? 0 : 1;
//			break;
//		default:
//			this.payload[1] = dto.getStatus()? 1 : 0;
//			break;
//		}
//		TxRequest16 tx = new TxRequest16(destination, this.payload);
//		TxStatusResponse status;
//		try {
//			status = (TxStatusResponse) this.xbee.sendSynchronous(tx);
//			if (status.isSuccess()) {
//				LOGGER.debug("MESSAGE DELIVERED SUCCESSFUL");
//				this.deviceMap.put(dto.getId(), dto);
//			}
//		} catch (XBeeException e) {
//			LOGGER.error(e.getMessage(), e);
//		}
		LOGGER.debug("sendMessageToDevice - END");
	}
	
	/**
	 * Receives data from devices and send it to the Hiperium Cloud Platform.
	 */
	@Override
	public void processResponse(XBeeResponse response) {
		LOGGER.debug("processResponse - START");
		if(this.webSocketClient == null) {
			LOGGER.error("WEBSOCKET CLIENT IS NULL.");
			return;
		}
		if (response.getApiId() == ApiId.RX_16_RESPONSE) {
			RxResponse16 response16 = (RxResponse16) response;
			this.data = response16.getData();
			for(int i = 0; i < this.data.length; i++) {
				LOGGER.debug("Data["+i+"]: " + this.data[i]);
			}
			this.deviceId = this.data[0];
			DeviceDTO deviceDTO = this.deviceMap.get(this.deviceId.longValue());
			if(deviceDTO == null) {
				LOGGER.error("DEVICE NOT FOUND WITH ID: " + this.data[0]);
			} else {
				deviceDTO.setTokenId(ConfigurationBean.getTokenId());
				if(this.data[1] == 0) {
					// IF DEVICE HAS A VALUE 0-100
					deviceDTO.setStatus(this.data[2] == 0? false : true);
				} else {
					// IF DEVICE HAS A STATUS 1/0
					deviceDTO.setValue(this.data[2]);
				}
				this.webSocketClient.sendMessage(this.converter.toJSON(deviceDTO));
				this.deviceMap.put(deviceDTO.getId(), deviceDTO);
			}
		}
		LOGGER.debug("processResponse - END");
	}
	
	/**
	 * @return the deviceMap
	 */
	public Map<Long, DeviceDTO> getDeviceMap() {
		return deviceMap;
	}

	/**
	 * @param deviceMap the deviceMap to set
	 */
	public void setDeviceMap(Map<Long, DeviceDTO> deviceMap) {
		this.deviceMap = deviceMap;
	}

	/**
	 * @return the socketClient
	 */
	public WebSocketClient getSocketClient() {
		return webSocketClient;
	}

	/**
	 * @param socketClient the socketClient to set
	 */
	public void setSocketClient(WebSocketClient socketClient) {
		this.webSocketClient = socketClient;
	}

	/**
	 * Close the connection releasing the resources.
	 */
	@PreDestroy
	public void destroy() {
		if(this.xbee != null){
			this.xbee.close();
		}
	}
}
