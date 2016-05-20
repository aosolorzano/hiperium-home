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
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

import com.hiperium.home.common.bean.ConfigurationBean;
import com.hiperium.home.main.MainClass;

/**
 * 
 * @author Andres Solorzano
 * 
 */
public abstract class AbstractMessageListener {

    /** The connection property. */
    protected Connection connection;
	
	/**
	 * 
	 * @param properties
	 * @throws JMSException
	 */
	public AbstractMessageListener(Properties properties) throws JMSException {
		// Create the JMS objects to connect to the server and manage a session
		final Map<String, Object> p = new HashMap<String, Object>();
		p.put(TransportConstants.HOST_PROP_NAME, properties.getProperty(ConfigurationBean.MESSAGE_SERVICE_HOST));
		TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), p);
		ConnectionFactory factory = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
        this.connection = factory.createConnection(properties.getProperty(Context.SECURITY_PRINCIPAL),
        		properties.getProperty(Context.SECURITY_CREDENTIALS));
        this.connection.setClientID("Home".concat(MainClass.HOME_ID.toString()));
	}
	
	/**
	 * Close the connection releasing the resources.
	 */
	public abstract void destroy();
}
