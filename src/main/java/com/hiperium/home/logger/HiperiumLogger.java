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
package com.hiperium.home.logger;

import org.apache.log4j.Logger;



/**
 * 
 * @author Andres Solorzano
 *
 */
public class HiperiumLogger {

	/** The property logger. */
	private Logger logger;
	
	/**
	 * Class constructor.
	 */
	public HiperiumLogger() {
		// Nothing to do
	}
	
	/**
	 * 
	 * @param clazz
	 */
	public HiperiumLogger(Class<?> clazz) {
		this();
		this.logger = Logger.getLogger(clazz);
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static HiperiumLogger getLogger(Class<?> clazz) {
		return new HiperiumLogger(clazz);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void debug(String message) {
		this.logger.debug(message); 
	}
	
	/**
	 * 
	 * @param message
	 */
	public void info(String message) {
		this.logger.info(message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public void error(String message) {
		this.logger.error(message); 
	}
	
	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public void error(String message, Throwable throwable) {
		this.logger.error(message, throwable); 
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}
}
