package com.gts.webservices.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GeoCorridor")
public class GeoCorridor {
     
	private String  accountId;
	private String  corridorId;
	private Integer  radius;
	private String  displayName;
	private String  description;
	private Integer lastUpdateTime;
	private Integer creationTime;
	
	@Id
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCorridorId() {
		return corridorId;
	}
	public void setCorridorId(String corridorId) {
		this.corridorId = corridorId;
	}
	public Integer getRadius() {
		return radius;
	}
	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Integer lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Integer creationTime) {
		this.creationTime = creationTime;
	}
	
	
}
