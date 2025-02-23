package com.megacitycab.model;

import java.sql.Timestamp;
import java.time.Instant;

public  class User {
	private int userID;
	private String name;
	private String password;
	private String email;
	private UserRole role;
	private Timestamp lastLogindate;
	public User() {
		
	}

	public User(int userID, String name, String email, UserRole role) {
		this.userID=userID;
		this.name =name;
		this.email =email;
		this.role = role;
		this.lastLogindate = Timestamp.from(Instant.now());
	}
	public User(int userID,String name,String password,String email,UserRole role,Timestamp lastLogindate) {
		this.userID=userID;
		this.name =name;
		this.password= password;
		this.email =email;
		this.role = role;
		this.lastLogindate = lastLogindate;
	}

	public User(int userID, String name, String password,String email, UserRole role) {
		this.userID=userID;
		this.name =name;
		this.email =email;
		this.role = role;
		this.lastLogindate = Timestamp.from(Instant.now());
	}

	public void setLastLogindate(Timestamp lastLogindate) {
		this.lastLogindate = lastLogindate;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Timestamp getLastLogindate() {
		return lastLogindate;
	}



}
