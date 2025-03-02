package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Driver;

public class CabDAOImplementation implements CabDAO{
	
	private Connection conn;
    private DriverDAO driverDAO;  

    public CabDAOImplementation(Connection conn, DriverDAO driverDAO) {
    	this.conn= conn;
    	this.driverDAO=driverDAO;
    }
    public CabDAOImplementation() {
    	this.driverDAO = new DriverDAOImplementation();
    }
	
	@Override
	public boolean addCab(Cab cab) {
		String query = "INSERT INTO cab (vehicleNumber, model, category,capacity,currentLocation,status,lastUpdated,driverID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try 
        {   Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cab.getVehicleNumber());
            statement.setString(2, cab.getModel());
            statement.setString(3, cab.getCategory().toString());
            statement.setInt(4, cab.getCapacity());
            statement.setString(5, cab.getCurrentLocation());
            statement.setString(6, cab.getCabStatus().toString());
            statement.setTimestamp(7, Timestamp.valueOf(cab.getLastUpdated()));
            statement.setInt(8, cab.getDriver().getDriverID());
            int rowinserted= statement.executeUpdate();
            return rowinserted > 0;
        } 
        catch (SQLException e) 
        {
        	System.err.println("Error adding cab: " + e.getMessage());
            return false;
        }
        
		
	}

	@Override
	public Cab getCabById(int cabId) {
	    String query = "SELECT * FROM cab WHERE cabID = ?";
	    
	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setInt(1, cabId);
	        ResultSet resultSet = statement.executeQuery(); 

	        if (resultSet.next()) {
	            Cab cab = new Cab();
	            cab.setCabID(resultSet.getInt("cabID"));
	            cab.setVehicleNumber(resultSet.getString("vehicleNumber"));
	            cab.setModel(resultSet.getString("model"));
	            cab.setCategory(CabCategory.valueOf(resultSet.getString("category").toUpperCase())); 
	            cab.setCapacity(resultSet.getInt("capacity"));
	            cab.setCurrentLocation(resultSet.getString("currentLocation"));
	            cab.setCabStatus(CabStatus.valueOf(resultSet.getString("status"))); 
	            cab.setLastUpdated(resultSet.getTimestamp("lastUpdated").toLocalDateTime());

	             
	         

	            return cab;  
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching cab: " + e.getMessage());
	    }
	    return null;  
	}


	public List<Cab> getAllCabs() {
	    List<Cab> cabs = new ArrayList<Cab>();
	    String sql = "SELECT * FROM cab";

	    try ( Connection connection = DBConnectionFactory.getConnection();
	    	PreparedStatement ps = connection.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	        	Driver driver = driverDAO.getDriverById(rs.getInt("driverID"));
	            Cab cab = new Cab(
	            		rs.getInt("cabID"),
	                    rs.getString("vehicleNumber"),
	                    rs.getString("model"),
	                    CabCategory.valueOf(rs.getString("category").toString()),  
	                    rs.getInt("capacity"),
	                    rs.getString("currentLocation"),
	                    CabStatus.valueOf(rs.getString("status").toString()),  
	                    rs.getTimestamp("lastUpdated").toLocalDateTime(), 
	                    driver
	                      
	            );
	            cabs.add(cab);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cabs;
	}
	
	@Override
	public boolean updateCab(Cab cab) {
	    String sql = "UPDATE cab SET vehicleNumber = ?, model = ?, category = ?, capacity = ?, " +
	                 "currentLocation = ?, cabStatus = ?, lastUpdated = ?, driverID = ? WHERE cabID = ?";
	    
	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setString(1, cab.getVehicleNumber());
	        ps.setString(2, cab.getModel());
	        ps.setString(3, cab.getCategory().toString());
	        ps.setInt(4, cab.getCapacity());
	        ps.setString(5, cab.getCurrentLocation());
	        ps.setString(6, cab.getCabStatus().toString());
	        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));  
	        ps.setInt(8, cab.getDriver().getDriverID()); 
	        ps.setInt(9, cab.getCabID());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean deleteCab(int cabId) {
	    String sql = "DELETE FROM cab WHERE cabID = ?";

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, cabId);

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}


}
