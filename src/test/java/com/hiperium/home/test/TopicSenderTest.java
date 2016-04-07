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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;



/**
 * This is an utility class to send message to a specific push topic.
 * 
 * @author Andres Solorzano
 * 
 */
public final class TopicSenderTest {

	/** The LOGGER property for logger messages. */
	private static final Logger LOGGER = Logger.getLogger(TopicSenderTest.class);
	
	// Set up all the default values
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    public static final String DEFAULT_PLATFORM_DESTINATION = "jms/topic/platformTopic";
    public static final String DEFAULT_DEVICE_DESTINATION = "jms/topic/deviceTopic";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://172.16.76.70:4447";
    
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("main() - START");
		try {
			Context namingContext = null;
			String userName = System.getProperty("username", "integration");
	        String password = System.getProperty("password", "Aldebaran8585");
	        
	        final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            namingContext = new InitialContext(env);
            
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            LOGGER.debug("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(connectionFactoryString);
            LOGGER.debug("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DEVICE_DESTINATION);
            LOGGER.debug("Attempting to acquire destination \"" + destinationString + "\"");
            
            Connection connection = connectionFactory.createConnection(userName, password);
            TopicSession session = (TopicSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) namingContext.lookup(destinationString);
            
         // Create a JMS Message Producer to send a message on the topic
 			MessageProducer producer = session.createProducer(topic);

 			TextMessage textMessage = session.createTextMessage("Hola Mundo5");
// 			textMessage.setLongProperty("homeId", 1L);
 			producer.send(textMessage);
 			
 			session.close();
 			connection.close();
		} catch (Exception e) {
			LOGGER.error("ERROR: " + e.getMessage());
		}
		LOGGER.info("main() - END");
	}
	
}
