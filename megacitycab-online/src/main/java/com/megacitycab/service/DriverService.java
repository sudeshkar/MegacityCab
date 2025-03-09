package com.megacitycab.service;

import java.util.List;

import com.megacitycab.dao.DriverDAO;
import com.megacitycab.dao.DriverDAOImplementation;
import com.megacitycab.model.Driver;

public class DriverService {
	private static DriverService instance;
	private DriverDAO driverDAO;

	private DriverService() {
		this.driverDAO = new DriverDAOImplementation();
	}

	public static DriverService getInstance() {
		if (instance == null) {
            synchronized (BookingService.class) {
                if (instance == null) {
                    instance = new DriverService();
                }
            }
        }
        return instance;
	}

	public List<Driver> getAllDrivers(){
		return driverDAO.getAllDrivers();
	}
	public Driver getDriverByUserID(int userid){
		return driverDAO.getDriverByUserID(userid);
	}

	public Driver getDriverByID(int driverid) {
		return driverDAO.getDriverById(driverid);
	}
	public boolean addDriver(Driver driver) {
		return driverDAO.addDriver(driver);
	}

	public Driver getDriverByEmail(String email) {
		return driverDAO.getDriverByEmail(email);
	}

	public boolean updateDriver(Driver driver) {
		return driverDAO.updateDriver(driver);
	}

	public boolean deleteDriver(int userid) {
		return driverDAO.deleteDriver(userid);
	}
	public boolean registerDriver(Driver driver,int userid) {
		return driverDAO.registerDriver(driver,userid);
	}
}
