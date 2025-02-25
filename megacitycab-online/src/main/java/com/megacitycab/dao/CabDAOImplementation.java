package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;

public class CabDAOImplementation implements CabDAO{
	
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

	             
	         

	            return cab; // Return the populated cab object
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching cab: " + e.getMessage());
	    }
	    return null; // Return null if no cab found or error occurs
	}


	@Override
	public Cab getCabByEmail(String email) {
		
		return null;
	}

	@Override
	public List<Cab> getAllCabs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateCab(Cab Cab) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCab(int Cabid) {
		// TODO Auto-generated method stub
		return false;
	}

}
