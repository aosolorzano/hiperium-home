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
 * This class represents the response for home authentication.
 * 
 * @author Andres Solorzano
 * 
 */
@XmlRootElement
public class HomeAuthResponseDTO implements Serializable {

	/**
	 * The property serialVersionUID.
	 */
	private static final long serialVersionUID = -5151190867212462748L;

	/** The property param1. */
	private String param1;

	/** The property param2. */
	private String param2;

	/** The property param3. */
	private String param3;

	/**
	 * Default constructor.
	 */
	public HomeAuthResponseDTO() {
		// Nothing to do.
	}

	/**
	 * @return the param1
	 */
	public String getParam1() {
		return param1;
	}

	/**
	 * @param param1
	 *            the param1 to set
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	/**
	 * @return the param2
	 */
	public String getParam2() {
		return param2;
	}

	/**
	 * @param param2
	 *            the param2 to set
	 */
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	/**
	 * @return the param3
	 */
	public String getParam3() {
		return param3;
	}

	/**
	 * @param param3
	 *            the param3 to set
	 */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

}
