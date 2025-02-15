package com.megacitycab.dao;

import java.sql.Connection;	
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.UserRole;

public class BookingDAO {
	
	
	public void addBooking(Booking booking) {
		String query= "INSERT INTO booking (customerID, bookingDateTime, pickupLocation,destination,distance,status,cabID,driverID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			Connection connection = DBConnectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, booking.getCustomer().getUserID());
			statement.setTimestamp(2, java.sql.Timestamp.valueOf(booking.getBookingDateTime()));
			statement.setString(3, booking.getPickupLocation());
			statement.setString(4, booking.getDestination());
			statement.setDouble(5, booking.getDistance());
			statement.setString(6, booking.getStatus().toString());
			statement.setInt(7, booking.getCab().getCabID());
			statement.setInt(8, booking.getDriver().getUserID());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public List<Booking> getAllBookings()throws SQLException{
		List<Booking> bookings = new ArrayList<Booking>();
		String query = "SELECT b.*, c.customerID, c.name AS customer_name, c.address AS customer_address"+
                "c.mobileNumber AS customer_mobile, c.phoneNumber AS customer_phone"+
                "d.driverID, d.name AS driver_name, d.licenseNumber, d.contactNumber AS driver_contact"+ 
                "cb.cabID, cb.vehicleNumber, cb.model, cb.category, cb.capacity, cb.currentLocation, cb.status AS cab_status"+
                "FROM booking b"+ 
                "JOIN customer c ON b.customerID = c.customerID "+
                "JOIN driver d ON b.driverID = d.driverID "+
                "JOIN cab cb ON b.cabID = cb.cabID";
		
        try(Connection conn=DBConnectionFactory.getConnection();
        		PreparedStatement prepareStatement=conn.prepareStatement(query);
        		ResultSet rs= prepareStatement.executeQuery()){
        	
        	while (rs.next()) {
        		int bookingNumber= rs.getInt("bookingNumber");
        				
        				Customer customers = new Customer(
        	                    rs.getInt("customerID"),
        	                    rs.getInt("userID"),
        	                    rs.getString("name"),
        	                    rs.getString("password"),
        	                    rs.getString("email"),
        	                    UserRole.valueOf(rs.getString("role")),
        	                    rs.getString("address"),
        	                    rs.getString("mobilenumber")
        	                    
        	                );

        	                // Create Driver object
        	                Driver driver = new Driver(
        	                    rs.getInt("driverID"),
        	                    rs.getInt("userID"),
        	                    rs.getString("name"),
        	                    rs.getString("password"),
        	                    rs.getString("email"),
        	                    UserRole.valueOf(rs.getString("role")),
        	                    rs.getString("licenseNumber"),
        	                    rs.getString("contactNumber"),
        	                    rs.getString("address"),
        	                    DriverStatus.valueOf(rs.getString("status"))
        	                );

        	                // Create Cab object
        	                Cab cab = new Cab(
        	                    rs.getInt("cabID"),
        	                    driver,
        	                    rs.getString("vehicleNumber"),
        	                    rs.getString("model"),
        	                    CabCategory.valueOf(rs.getString("category")),
        	                    rs.getInt("capacity"),
        	                    rs.getString("currentLocation"),
        	                    CabStatus.valueOf(rs.getString("status"))
        	                );	
				
			}
			
		} catch (Exception e) {
			
		}
		
		
		return bookings;
	}
	
	

}
