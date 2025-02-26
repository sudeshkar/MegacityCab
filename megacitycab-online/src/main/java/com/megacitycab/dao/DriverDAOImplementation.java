package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

public class DriverDAOImplementation implements DriverDAO{
	private UserDAO userDAO;
	public DriverDAOImplementation (Connection conn, UserDAO userDAO) {
		this.conn = conn;
        this.userDAO = userDAO;
    }

	
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
		User user = userDAO.getUserByEmail(email);
		if (user == null) {
            return null; 
        }
		if (!user.getRole().toString().equalsIgnoreCase("Driver")) {
	        return null; 
	    }
		String sql = "SELECT * FROM driver WHERE userID = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getUserID());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { 
                    return new Driver(
                    	rs.getInt("driverID"),
                        user.getUserID(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole(),
                        rs.getString("licenseNumber"),
                        rs.getString("contactNumber"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        DriverStatus.valueOf(rs.getString("status").toString())
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    
	}

	@Override
	public List<Driver> getAllDrivers() {
		 List<Driver> drivers = new ArrayList();

	        
	        List<User> allUsers = userDAO.getAllUsers();

	        
	        String sql = "SELECT * FROM driver WHERE userID = ?";

	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            for (User user : allUsers) {
	                ps.setInt(1, user.getUserID());
	                try (ResultSet rs = ps.executeQuery()) {
	                    if (rs.next()) {  
	                        Driver driver = new Driver(
	                        	rs.getInt("driverID"),
	                        	user.getUserID(),
	                            user.getName(),
	                            user.getEmail(),
	                            user.getPassword(),
	                            UserRole.valueOf(user.getRole().toString()) ,
	                            rs.getString("licenseNumber"),
	                            rs.getString("contactNumber"),
	                            rs.getString("phoneNumber"),
	                            rs.getString("address"),
	                            DriverStatus.valueOf(rs.getString("status"))
	                        );
	                        drivers.add(driver);
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return drivers;
	    }


	@Override
	public boolean updateDriver(Driver driver) {
		boolean userUpdated = userDAO.updateUser(driver);
		if(!userUpdated) {
	        return false; 
	    }
		String sql = "UPDATE driver SET licenseNumber = ?, contactNumber = ?, phoneNumber = ?, address = ?, status = ? WHERE userID = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, driver.getLicenseNumber());
	        ps.setString(2, driver.getContactNumber());
	        ps.setString(3, driver.getPhoneNumber());
	        ps.setString(4, driver.getAddress());
	        ps.setString(5, driver.getDriverStatus().toString());  
	        ps.setInt(6, driver.getUserID());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return false;
	}

	@Override
	public boolean deleteDriver(int userID) {
	    
	    String roleCheckQuery = "SELECT role FROM users WHERE userID = ?";
	    try (PreparedStatement ps = conn.prepareStatement(roleCheckQuery)) {
	        ps.setInt(1, userID);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String role = rs.getString("role");
	                if (!"Driver".equalsIgnoreCase(role)) {
	                    return false;  
	                }
	            } else {
	                return false; 
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    
	    String deleteDriverQuery = "DELETE FROM driver WHERE userID = ?";
	    try (PreparedStatement ps = conn.prepareStatement(deleteDriverQuery)) {
	        ps.setInt(1, userID);
	        int rowsAffected = ps.executeUpdate();
	        
	        
	        if (rowsAffected > 0) {
	            return deleteUser(userID); 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	private boolean deleteUser(int userID) {
	    String deleteUserQuery = "DELETE FROM users WHERE userID = ?";
	    try (PreparedStatement ps = conn.prepareStatement(deleteUserQuery)) {
	        ps.setInt(1, userID);
	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0; 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}


}
