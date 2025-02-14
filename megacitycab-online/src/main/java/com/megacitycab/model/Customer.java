package com.megacitycab.model;

import java.time.LocalDateTime;

public class Customer extends User{

	private String address;
	private String mobilenumber;
	private String phonenumber;
	private LocalDateTime registrationDate;
	private CustomerStatus status;
	public Customer(int userID,String name, String password, String email, UserRole role,String address,String mobilenumber,String phonenumber) {
		super(userID,name, password, email, role);
		this.address = address;
		this.mobilenumber=mobilenumber;
		this.phonenumber= phonenumber;


	}
	public Customer(int userID,String name, String password, String email, UserRole role,String address,String mobilenumber) {
		super(userID,name, password, email, role);
		this.address = address;
		this.mobilenumber=mobilenumber;
		this.status = CustomerStatus.ACTIVE;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobilenumber() {
		return mobilenumber;
	}
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}
	public CustomerStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerStatus status) {
		this.status = status;
	}


}
