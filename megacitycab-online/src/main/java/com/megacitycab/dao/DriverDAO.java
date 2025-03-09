package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.Driver;

public interface DriverDAO {

	boolean addDriver(Driver driver);
	Driver getDriverById(int driverId);
	Driver getDriverByEmail(String email);
	List<Driver> getAllDrivers();
	boolean updateDriver(Driver driver);
	boolean deleteDriver(int userID);
	Driver getDriverByUserID(int userid);
	boolean registerDriver(Driver driver,int userid);



}
