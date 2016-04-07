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
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

/**
 * 
 * This test class implements the MessageListener interface for the
 * javax.message package. The utility, listen for a user connection if this
 * Hyperium System Home Project and make a HTTP session with the Hyperium Cloud.
 * 
 * @author Aldebaran Solutions Corp.
 * 
 */
public class HiperiumTopicListener implements MessageListener {

	/** The LOGGER property for logger messages. */
	private static final Logger LOGGER = Logger.getLogger(HiperiumTopicListener.class);
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Topic topic;
	private MessageConsumer consumer;
	private boolean quit = false;
	
	/**
	 * @param args
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		LOGGER.debug("main - START");
		HiperiumTopicListener listener = new HiperiumTopicListener();
		final Map<String, Object> p = new HashMap<String, Object>();
		p.put(TransportConstants.HOST_PROP_NAME, "190.152.34.22");
		TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), p);
        listener.factory = (ConnectionFactory) HornetQJMSClient
                .createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
        
        // Create the JMS objects to connect to the server and manage a session
        listener.connection = listener.factory.createConnection("integration", "Aldebaran8585");
        listener.connection.setClientID("Home100");
        listener.session = listener.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        listener.topic = HornetQJMSClient.createTopic("deviceTopic");
        listener.consumer = listener.session.createDurableSubscriber(
        		listener.topic,				// JMS Cloud Topic
				"HiperiumTopicListener",	// clientID = clientID + "##" + subName
				"homeId=1",					// Message selector = homeId
				Boolean.TRUE);
        listener.consumer.setMessageListener(listener);
		listener.connection.start();
		
		System.out.println("JMS Ready To Receive Messages...");
		
		// Wait until a "quit" message has been received.
		synchronized (listener) {
			while (!listener.quit) {
				try {
					listener.wait();
				} catch (InterruptedException ie) {
				}
			}
		}
		LOGGER.debug("main - END");
	}

	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		try {
			String msgText;
			if (message instanceof TextMessage) {
				msgText = ((TextMessage) message).getText();
			} else {
				msgText = message.toString();
			}
			System.out.println("JSON: " + msgText);
			if (msgText.equalsIgnoreCase("quit")) {
				synchronized (this) {
					quit = true;
					this.notifyAll(); // Notify main thread to quit
				}
			}
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}
	
	/**
	 * Close the connection releasing the resources
	 * 
	 * @throws JMSException
	 */
	public void close() throws JMSException {
		this.consumer.close();
		this.session.close();
		this.connection.close();
	}
}
