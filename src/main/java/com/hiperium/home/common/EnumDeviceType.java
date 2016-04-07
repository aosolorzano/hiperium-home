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
 * This enumeration specifies the existing device types in the system.
 * 
 * @author Andres Solorzano
 */
public enum EnumDeviceType {

	/** The MOVEMENT element witch value is movement. */
	MOVEMENT("movement"),

	/** The SMOKE element witch value is smoke. */
	SMOKE("smoke"),

	/** The HUMIDITY element witch value is humidity. */
	HUMIDITY("humidity"),

	/** The TEMPERATURE element witch value is temperature. */
	TEMPERATURE("temperature"),

	/** The LUMINOSITY element witch value is luminosity. */
	LUMINOSITY("luminosity"),

	/** The AMMETER element witch value is ammeter. */
	AMMETER("ammeter"),

	/** The CONTACT element witch value is contactSensor. */
	CONTACT("contactSensor"),

	/** The ELECTRIC_LIGHT element witch value is electricLight. */
	ELECTRIC_LIGHT("electricLight"),

	/** The CAMERA element witch value is camera. */
	CAMERA("camera"),

	/** The INFRARED element witch value is infrared. */
	INFRARED("infrared"),

	/** The ALARM element witch value is alarm. */
	ALARM("alarm"),

	/** The IRRIGATION_WATER element witch value is irrigationWater. */
	IRRIGATION_WATER("irrigationWater"),

	/** The BLIND element witch value is blind. */
	BLIND("blind"),

	/** The CURTAIN element witch value is curtain. */
	CURTAIN("curtain"),

	/** The DOOR element witch value is door. */
	DOOR("door"),

	/** The WINDOW element witch value is window. */
	WINDOW("window");

	/** Property that contains the label of the element */
	private final String label;

	/**
	 * Enumeration constructor.
	 * 
	 * @param label
	 *            Label of the enumeration element
	 */
	EnumDeviceType(String label) {
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
	 * Return the enumerated associated to the value.
	 * 
	 * @param name
	 *            the enumeration name
	 * @return the enumerated associated to the value.
	 */
	public static EnumDeviceType decodeByEnumName(String name) {
		for (EnumDeviceType e : EnumDeviceType.values()) {
			if (e.name().equals(name)) {
				return e;
			}
		}
		return EnumDeviceType.MOVEMENT;
	}
}
