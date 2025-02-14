package com.megacitycab.model;

import java.time.LocalDateTime;

public class Cab {
	private String vehicleNumber;
	private String model;
	private CabCategory category;
	private int capacity;
	private String currentLocation;
	private CabStatus cabStatus;
	private LocalDateTime lastUpdated;
	private Driver driver;

	public Cab(Driver driver,String vehicleNumber,String model,CabCategory category,int capacity,String currentLocation,
			CabStatus cabStatus) {
			this.driver=driver;
			this.vehicleNumber=vehicleNumber;
			this.model=model;
			this.category=category;
			this.capacity =capacity;
			this.currentLocation=currentLocation;
			this.lastUpdated= LocalDateTime.now();
			this.cabStatus= cabStatus;

	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public CabCategory getCategory() {
		return category;
	}

	public void setCategory(CabCategory category) {
		this.category = category;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public CabStatus getCabStatus() {
		return cabStatus;
	}

	public void setCabStatus(CabStatus cabStatus) {
		this.cabStatus = cabStatus;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}


}
