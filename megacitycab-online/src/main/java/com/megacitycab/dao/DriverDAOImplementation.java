package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;

public class DriverDAOImplementation implements DriverDAO{
	
	private Connection conn;
	public DriverDAOImplementation(Connection conn) {
		this.conn=conn;
	}

	@Override
	public boolean addDriver(Driver driver) {
		String sql = "INSERT INTO driver (name, licenseNumber, contactNumber, phoneNumber, address, status) VALUES (?, ?, ?, ?)";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setString(1, driver.getName());  
	        ps.setString(2, driver.getLicenseNumber());  
	        ps.setString(3, driver.getContactNumber());  
	        ps.setString(4, driver.getPhoneNumber());  
	        ps.setString(5, driver.getAddress());  
	        ps.setString(6, driver.getDriverStatus().name());  
	        ps.setInt(7, driver.getUserID()); 

	        
	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public Driver getDriverById(int driverId) {
		String sql = "SELECT * FROM driver WHERE driverID = ?";
	    Driver driver = null;
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	         
	        ps.setInt(1, driverId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                
	                driver = new Driver();
	                driver.setDriverID(rs.getInt("driverID"));
	                driver.setName(rs.getString("name"));
	                driver.setLicenseNumber(rs.getString("licenseNumber"));
	                driver.setContactNumber(rs.getString("contactNumber"));
	                driver.setPhoneNumber(rs.getString("phoneNumber"));
	                driver.setAddress(rs.getString("address"));
	                driver.setDriverStatus(DriverStatus.valueOf(rs.getString("status")));;
	                driver.setUserID(rs.getInt("userId"));  
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		return driver;
	}

	@Override
	public Driver getDriverByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Driver> getAllDrivers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateDriver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteDriver() {
		// TODO Auto-generated method stub
		return false;
	}

}
