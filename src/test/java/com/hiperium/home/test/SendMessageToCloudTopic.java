/**
 * Aldebaran Solutions Corp.
 * Product: Hyperium
 * Created: 12-Mar-2011 - 00:00:00
 * 
 * 
 * The contents of this file are copyrighted by Aldebaran Solutions Corp. 
 * and it is protected by the license: "GPL V3." You can find a copy of this 
 * license at: http://www.aldebaran-solutions.com
 * 
 * Copyright 2011 Aldebaran Solutions Corp. All rights reserved.
 * 
 */
package com.hiperium.home.test;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

import com.hiperium.home.dto.DeviceDTO;
import com.hiperium.home.gson.GsonConverterUtil;


/**
 * 
 * This test class implements the MessageListener interface for the
 * javax.message package. The utility, listen for a user connection if this
 * Hiperium System Home Project and make a HTTP session with the Hiperium Cloud.
 * 
 * @author Aldebaran Solutions Corp.
 * 
 */
public class SendMessageToCloudTopic {

	/** The LOGGER property for logger messages. */
	private static final Logger LOGGER = Logger.getLogger(SendMessageToCloudTopic.class);
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.debug("main() - START");
		final Map<String, Object> p = new HashMap<String, Object>();
		p.put(TransportConstants.HOST_PROP_NAME, "190.152.34.22");
		TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), p);
        ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient
                .createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
        Connection connection = null;
        
        try {
	        // Create the JMS objects to connect to the server and manage a session
	        connection = factory.createConnection("integration", "Aldebaran8585");
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        Topic topic = HornetQJMSClient.createTopic("deviceTopic");
			// Create a JMS Message Producer to send a message on the queue
			MessageProducer producer = session.createProducer(topic);
			// OBJECT THAT CONTAINS DATA TO PERFORM IN THE CLOUD
			// JSON message
			TextMessage message = session.createTextMessage(new GsonConverterUtil().toJSON(updateDevice()));
			message.setLongProperty("homeId", 1L);
			producer.send(message);
        } catch (JMSException e) {
			LOGGER.error("insertInitialConfiguration - ERROR: " + e.getMessage());
		} finally {
			// CLOSE THE CLOUD SESSION QUEUE
			if(connection != null){
				try {
					connection.close();
				} catch (JMSException e) {
					LOGGER.error("insertInitialConfiguration - ERROR: " + e.getMessage());
				}
			}
		} 
        LOGGER.debug("main() - END");
	}
	
	/**
	 * 
	 * @return
	 */
	private static DeviceDTO updateDevice() {
		DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setId(3L);
		deviceDTO.setHomeId(1L);
		deviceDTO.setTokenId("12345678");
		deviceDTO.setValue(30);
		deviceDTO.setStatus(false);
		deviceDTO.setPinId(2);
		deviceDTO.setXbee16bitsMSB(56);
		deviceDTO.setXbee16bitsLSB(78);
		return deviceDTO;
	}
}
