package com.megacitycab.model;

public class Driver extends User{

	private String licenseNumber;
	private String contactNumber;
	private String phoneNumber;
	private String address;
	private DriverStatus driverStatus;


	public Driver(int userID,String name, String password, String email, UserRole role,String licenseNumber,String contactNumber,String phoneNumber,
			String address) {
		super(userID,name, password, email, role);
		this.licenseNumber=licenseNumber;
		this.contactNumber=contactNumber;
		this.phoneNumber = phoneNumber;
		this.address =address;
		this.driverStatus = DriverStatus.AVAILABLE;

	}
	public Driver(int userID,String name, String password, String email, UserRole role,String licenseNumber,String contactNumber,
			String address,DriverStatus driverStatus) {
		super(userID,name, password, email, role);
		this.licenseNumber=licenseNumber;
		this.contactNumber=contactNumber;
		this.address =address;
		this.driverStatus = DriverStatus.AVAILABLE;

	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public DriverStatus getDriverStatus() {
		return driverStatus;
	}
	public void setDriverStatus(DriverStatus driverStatus) {
		this.driverStatus = driverStatus;
	}


}
