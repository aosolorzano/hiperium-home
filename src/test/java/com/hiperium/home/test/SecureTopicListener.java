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

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * 
 * This test class implements the MessageListener interface for the
 * javax.message package. The utility, listen for a user connection if this
 * Hiperium System Home Project and make a HTTP session with the Hiperium Cloud.
 * 
 * @author Andres Solorzano
 * 
 */
public class SecureTopicListener implements MessageListener {

	/** The LOGGER property for logger messages. */
	private static final Logger LOGGER = Logger.getLogger(SecureTopicListener.class);
	
	private boolean quit = false;
	
	/**
	 * @param args
	 * @throws JMSException 
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		LOGGER.debug("main() - START");
		SecureTopicListener listener = new SecureTopicListener();
		Context namingContext = null;
		
        // Create the JMS objects to connect to the server and manage a session
        try {
        	// Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            env.put(Context.PROVIDER_URL, "http-remoting://localhost:8443");
            env.put(Context.SECURITY_PRINCIPAL, "hiperium");
            env.put(Context.SECURITY_CREDENTIALS, "CloudUser2@14");
            env.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","true");   
            env.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS","true");
//            env.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "true");
//            env.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "true");

            namingContext = new InitialContext(env);
            
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", "java:jboss/exported/jms/RemoteConnectionFactory");
            LOGGER.debug("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(connectionFactoryString);
            LOGGER.debug("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", "jms/topic/deviceTopic");
            LOGGER.debug("Attempting to acquire destination \"" + destinationString + "\"");
            Destination destination = (Destination) namingContext.lookup(destinationString);
            LOGGER.debug("Found destination \"" + destinationString + "\" in JNDI");
            
//            JMSContext context = connectionFactory.createContext("hiperium", "CloudUser2@14");
            
            // Create the JMS consumer
//            JMSConsumer consumer = context.createConsumer(destination);
//			consumer.setMessageListener(listener);
//			context.start();
			
			LOGGER.debug("JMS Ready To Receive Messages...");
			
			// Wait until a "quit" message has been received.
			synchronized (listener) {
				while (!listener.quit) {
					try {
						listener.wait();
					} catch (InterruptedException ie) {
					}
				}
			}
		} catch (NamingException e) {
			LOGGER.error(e.getMessage(), e);
		}
        LOGGER.debug("main() - END");
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
		
	}
}
