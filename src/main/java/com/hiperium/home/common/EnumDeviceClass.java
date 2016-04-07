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
package com.hiperium.home.common;



/**
 * This enumeration specifies the existing device classes in the system.
 * 
 * @author Andres Solorzano
 */
public enum EnumDeviceClass {

	/** The action element witch label is action. */
	SENSOR("sensor"),

	/** The activation element witch label is activation. */
	ACTUATOR("actuator");

	/** Property that contains the label of the element */
	private final String label;

	/**
	 * Enumeration constructor.
	 * 
	 * @param label
	 *            Label of the enumeration element
	 */
	EnumDeviceClass(String label) {
		this.label = label;
	}

	/**
	 * Return the label associate to the element of the enumeration.
	 * 
	 * @return the label associate with the enumeration
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Return the enumerated associated to the name.
	 * 
	 * @param name
	 *            the enumeration name
	 * @return the enumerated associated to the value.
	 */
	public static EnumDeviceClass decodeByEnumName(String name) {
		for (EnumDeviceClass e : EnumDeviceClass.values()) {
			if (e.name().equals(name)) {
				return e;
			}
		}
		return EnumDeviceClass.SENSOR;
	}
	
	/**
	 * Return the enumerated associated to the label.
	 * 
	 * @param label
	 *            the enumeration name
	 * @return the enumerated associated to the value.
	 */
	public static EnumDeviceClass decodeByEnumLabel(String label) {
		for (EnumDeviceClass e : EnumDeviceClass.values()) {
			if (e.label.equals(label)) {
				return e;
			}
		}
		return EnumDeviceClass.SENSOR;
	}
}
