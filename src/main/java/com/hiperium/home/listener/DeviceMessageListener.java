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

import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;

import org.hornetq.api.jms.HornetQJMSClient;

import com.hiperium.home.common.Resources;
import com.hiperium.home.dto.DeviceDTO;
import com.hiperium.home.gson.converter.DeviceConverter;
import com.hiperium.home.logger.HiperiumLogger;
import com.hiperium.home.main.MainClass;


/**
 * This class represents a Message Driven Bean that gets a message from the cloud server for
 * any Internet user smart home administration.
 * 
 * @author Andres Solorzano
 * 
 */
public class DeviceMessageListener extends AbstractMessageListener implements MessageListener {

	/** The LOGGER property for logger messages. */
	private static final HiperiumLogger LOGGER = HiperiumLogger.getLogger(DeviceMessageListener.class);

	/** The property jsonConverter. */
	private DeviceConverter jsonConverter;
	
	/** The property jsonConverter. */
	private DeviceDataListener dataListener;
	
	/** The topicSession property. */
	private TopicSession topicSession;
	/** The queueSession property. */
	private QueueSession queueSession;
	
	/**
	 * 
	 * @param properties
	 * @throws JMSException
	 */
	public DeviceMessageListener(Properties properties) throws JMSException {
		super(properties);
		this.jsonConverter = new DeviceConverter();
	}
	
	/**
	 * Start the listening.
	 * @throws JMSException 
	 */
	public void start() throws JMSException {
		this.topicSession = (TopicSession) super.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = HornetQJMSClient.createTopic(Resources.CLIENT_DEVICE_TOPIC);
		MessageConsumer consumer = this.topicSession.createDurableSubscriber(
				topic,								// JMS Cloud Topic
				"DeviceMessageListener",			// clientID = clientID + "##" + subName
				"homeId="+  MainClass.HOME_ID,		// Message selector = homeId
				Boolean.TRUE);
		consumer.setMessageListener(this);
		super.connection.start();
		LOGGER.info("DeviceMessageListener Ready To Receive Messages...");
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void onMessage(Message message) {
		LOGGER.debug("onMessage - START");
		if(this.dataListener == null) {
			LOGGER.error("DEVICE DATA LISTENER IS NULL.");
			return;
		}
		try {
			TextMessage tm = (TextMessage) message;
			String jsonMessage = tm.getText();
			DeviceDTO deviceDTO = this.jsonConverter.fromJsonToDeviceDTO(jsonMessage);
			this.dataListener.sendValueToXBee(deviceDTO);
		} catch (JMSException e) {
			LOGGER.error("onMessage - ERROR: " + e.getMessage());
		}
		LOGGER.debug("onMessage - END");
	}
	
	/**
	 * 
	 * @param deviceDTO
	 */
	public void sendMessage(DeviceDTO deviceDTO) {
		LOGGER.debug("sendMessage() - START");
		if(deviceDTO.getHomeId() == null || deviceDTO.getHomeId() == 0L) {
			LOGGER.error("DO NOT EXIST HOME ID TO SEND THE MESSAGE.");
			return;
		}
		try {
			this.queueSession = (QueueSession) super.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = HornetQJMSClient.createQueue(Resources.CLIENT_DEVICE_QUEUE);
			MessageProducer producer = this.queueSession.createProducer(queue);
			TextMessage message = this.queueSession.createTextMessage(this.jsonConverter.toJSON(deviceDTO));
			producer.send(message);
			producer.close();
        } catch (JMSException e) {
        	LOGGER.error("ERROR: " + e.getMessage());
		}
		LOGGER.debug("sendMessage() - END");
	}
	
	/**
	 * @return the dataListener
	 */
	public DeviceDataListener getDataListener() {
		return dataListener;
	}

	/**
	 * @param dataListener
	 *            the dataListener to set
	 */
	public void setDataListener(DeviceDataListener dataListener) {
		this.dataListener = dataListener;
	}

	/**
	 * Close the connection releasing the resources.
	 */
	@Override
	@PreDestroy
	public void destroy() {
		if(this.connection != null) {
			try {
				this.connection.close();
			} catch (JMSException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
