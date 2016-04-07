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
package com.hiperium.home.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a POJO class that will be used to authenticate users against Web
 * Services.
 * 
 * @author Andres Solorzano
 * 
 */
@XmlRootElement
public class HomeCredentialDTO implements Serializable {

	/**
	 * The serialVersionUID property.
	 */
	private static final long serialVersionUID = -2980739833931772575L;

	/** The property id. */
	private Long id;

	/** The property serial. */
	private String serial;

	/**
	 * Default constructor.
	 */
	public HomeCredentialDTO() {
		// Nothing to do.
	}

	/**
	 * Class constructor.
	 * 
	 * @param id
	 * @param serial
	 */
	public HomeCredentialDTO(Long id, String serial) {
		this.id = id;
		this.serial = serial;
	}

	/**
	 * Gets the id property.
	 * 
	 * @return the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id property.
	 * 
	 * @param id
	 *            the id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the serial property.
	 * 
	 * @return the serial.
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * Sets the serial property.
	 * 
	 * @param serial
	 *            the serial to set.
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}
}
