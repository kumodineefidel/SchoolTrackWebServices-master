package com.gts.webservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RouteDeviceInfo")
public class RouteDeviceInfo {


	private Integer id;
	private String geoCorridorId;
	private String deviceId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getGeoCorridorId() {
		return geoCorridorId;
	}
	public void setGeoCorridorId(String geoCorridorId) {
		this.geoCorridorId = geoCorridorId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
