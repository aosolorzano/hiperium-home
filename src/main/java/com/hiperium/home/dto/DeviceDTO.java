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
 * This class sets device actions performed by the Hiperium service or by the
 * user. The utility is to send this data to the Hiperium Queue to update or
 * operate a device.
 * 
 * @author Andres Solorzano
 * 
 */
@XmlRootElement
public class DeviceDTO implements Serializable {

	/**
	 * The property serialVersionUID.
	 */
	private static final long serialVersionUID = -3222609201425549056L;

	/** Device identifier. */
	private Long id;
	/** Home identifier. */
	private Long homeId;
	/** Device father identifier. */
	private Long fatherId;
	/** Device tokenId. */
	private String tokenId;
	/** Device name. */
	private String name;
	/** Device devClass. */
	private String devClass;
	/** Device devType. */
	private String devType;
	/** Device status. */
	private Boolean status;
	/** Device status. */
	private Integer value;
	/** Device operation level. */
	private Integer operationLevel;
	/** Device pin Identifier. */
	private Integer pinId;
	/** 64 Bits source address of the remote radio device. */
	private Integer xbee16bitsMSB;
	/** 16 Bits source address of the remote radio device. */
	private Integer xbee16bitsLSB;

	/**
	 * Class constructor.
	 */
	public DeviceDTO() {
		// Nothing to do.
	}

	/**
	 * 
	 * @param id
	 * @param homeId
	 * @param name
	 * @param status
	 * @param value
	 * @param devClass
	 * @param operationLevel
	 * @param pinId
	 * @param xbee16bitsMSB
	 * @param xbee16bitsLSB
	 */
	public DeviceDTO(Long id, Long homeId, String name, Boolean status,
			Integer value, String devClass, String devType, Integer operationLevel,
			Integer pinId, Integer xbee16bitsMSB, Integer xbee16bitsLSB) {
		this.id = id;
		this.homeId = homeId;
		this.name = name;
		this.status = status;
		this.value = value;
		this.devClass = devClass;
		this.devType = devType;
		this.operationLevel = operationLevel;
		this.pinId = pinId;
		this.xbee16bitsMSB = xbee16bitsMSB;
		this.xbee16bitsLSB = xbee16bitsLSB;
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
	 * Gets the name property.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name property.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the operationLevel property.
	 * 
	 * @return the operationLevel.
	 */
	public Integer getOperationLevel() {
		return operationLevel;
	}

	/**
	 * Sets the operationLevel property.
	 * 
	 * @param operationLevel
	 *            the operationLevel to set.
	 */
	public void setOperationLevel(Integer operationLevel) {
		this.operationLevel = operationLevel;
	}

	/**
	 * 
	 * @return
	 */
	public Long getHomeId() {
		return homeId;
	}

	/**
	 * 
	 * @param homeId
	 */
	public void setHomeId(Long homeId) {
		this.homeId = homeId;
	}

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId
	 *            the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @return the devClass
	 */
	public String getDevClass() {
		return devClass;
	}

	/**
	 * @param devClass
	 *            the devClass to set
	 */
	public void setDevClass(String devClass) {
		this.devClass = devClass;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * @return the fatherId
	 */
	public Long getFatherId() {
		return fatherId;
	}

	/**
	 * @param fatherId
	 *            the fatherId to set
	 */
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}

	/**
	 * @return the devType
	 */
	public String getDevType() {
		return devType;
	}

	/**
	 * @param devType
	 *            the devType to set
	 */
	public void setDevType(String devType) {
		this.devType = devType;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the xbee16bitsMSB
	 */
	public Integer getXbee16bitsMSB() {
		return xbee16bitsMSB;
	}

	/**
	 * @param xbee16bitsMSB
	 *            the xbee16bitsMSB to set
	 */
	public void setXbee16bitsMSB(Integer xbee16bitsMSB) {
		this.xbee16bitsMSB = xbee16bitsMSB;
	}

	/**
	 * @return the xbee16bitsLSB
	 */
	public Integer getXbee16bitsLSB() {
		return xbee16bitsLSB;
	}

	/**
	 * @param xbee16bitsLSB
	 *            the xbee16bitsLSB to set
	 */
	public void setXbee16bitsLSB(Integer xbee16bitsLSB) {
		this.xbee16bitsLSB = xbee16bitsLSB;
	}

	/**
	 * @return the pinId
	 */
	public Integer getPinId() {
		return pinId;
	}

	/**
	 * @param pinId
	 *            the pinId to set
	 */
	public void setPinId(Integer pinId) {
		this.pinId = pinId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceDTO other = (DeviceDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceDTO [id=" + id + ", homeId=" + homeId + ", name=" + name
				+ ", devClass=" + devClass + ", devType=" + devType
				+ ", status=" + status + ", value=" + value + ", pinId="
				+ pinId + ", xbee16bitsMSB=" + xbee16bitsMSB
				+ ", xbee16bitsLSB=" + xbee16bitsLSB + "]";
	}

}
