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
        	                    rs.getString("mobileNumber"),
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
	public Booking getBookingById(int bookingId) throws SQLException {
	    String query = "SELECT b.*, " +
	                  "c.customerID, c.name AS customer_name, c.address AS customer_address, " +
	                  "c.mobileNumber AS customer_mobile, " +
	                  "d.driverID, d.name AS driver_name, d.licenseNumber, d.contactNumber AS driver_contact, " +
	                  "cb.cabID, cb.vehicleNumber, cb.model, cb.category, cb.capacity, cb.currentLocation, cb.status AS cab_status " +
	                  "FROM booking b " +
	                  "JOIN customer c ON b.customerID = c.customerID " +
	                  "JOIN driver d ON b.driverID = d.driverID " +
	                  "JOIN cab cb ON b.cabID = cb.cabID " +
	                  "WHERE b.bookingNumber = ?";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	        
	        preparedStatement.setInt(1, bookingId);
	        
	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            if (rs.next()) {
	                // Create Customer object
	                Customer customer = new Customer(
	                    rs.getInt("customerID"),
	                    rs.getInt("userID"),
	                    rs.getString("name"),
	                    rs.getString("password"),
	                    rs.getString("email"),
	                    UserRole.valueOf(rs.getString("role")),
	                    rs.getString("address"),
	                    rs.getString("mobileNumber")
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
	                    rs.getString("mobileNumber"),
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

	                // Create and return Booking object using the constructor for existing bookings
	                return new Booking(
	                    rs.getInt("bookingNumber"),
	                    customer,
	                    rs.getTimestamp("bookingDateTime").toLocalDateTime(),
	                    rs.getString("pickupLocation"),
	                    rs.getString("destination"),
	                    rs.getDouble("distance"),
	                    BookingStatus.valueOf(rs.getString("status")),
	                    cab,
	                    driver
	                );
	            }
	            return null; // Return null if booking not found
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error retrieving booking with ID: " + bookingId, e);
	    }
	}
	
	
	public List<Booking> getBookingsByUserId(int userId) throws SQLException {
	    List<Booking> bookings = new ArrayList<>();
	    String query = "SELECT b.*, " +
	                  "c.customerID, c.name AS customer_name, c.address AS customer_address, " +
	                  "c.mobileNumber AS customer_mobile, " +
	                  "d.driverID, d.name AS driver_name, d.licenseNumber, d.contactNumber AS driver_contact, " +
	                  "cb.cabID, cb.vehicleNumber, cb.model, cb.category, cb.capacity, cb.currentLocation, cb.status AS cab_status " +
	                  "FROM booking b " +
	                  "JOIN customer c ON b.customerID = c.customerID " +
	                  "JOIN driver d ON b.driverID = d.driverID " +
	                  "JOIN cab cb ON b.cabID = cb.cabID " +
	                  "WHERE c.userID = ? " +
	                  "ORDER BY b.bookingDateTime DESC";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	        
	        preparedStatement.setInt(1, userId);
	        
	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            while (rs.next()) {
	                bookings.add(createBookingFromResultSet(rs));
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error retrieving bookings for user ID: " + userId, e);
	    }
	    return bookings;
	}

	public List<Booking> getBookingsByDriverId(int driverId) throws SQLException {
	    List<Booking> bookings = new ArrayList<>();
	    String query = "SELECT b.*, " +
	                  "c.customerID, c.name AS customer_name, c.address AS customer_address, " +
	                  "c.mobileNumber AS customer_mobile, " +
	                  "d.driverID, d.name AS driver_name, d.licenseNumber, d.contactNumber AS driver_contact, " +
	                  "cb.cabID, cb.vehicleNumber, cb.model, cb.category, cb.capacity, cb.currentLocation, cb.status AS cab_status " +
	                  "FROM booking b " +
	                  "JOIN customer c ON b.customerID = c.customerID " +
	                  "JOIN driver d ON b.driverID = d.driverID " +
	                  "JOIN cab cb ON b.cabID = cb.cabID " +
	                  "WHERE d.driverID = ? " +
	                  "ORDER BY b.bookingDateTime DESC";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	        
	        preparedStatement.setInt(1, driverId);
	        
	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            while (rs.next()) {
	                bookings.add(createBookingFromResultSet(rs));
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error retrieving bookings for driver ID: " + driverId, e);
	    }
	    return bookings;
	}

	public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
	    String query = "UPDATE booking SET status = ? WHERE bookingNumber = ?";
	    
	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query)) {
	        
	        preparedStatement.setString(1, status);
	        preparedStatement.setInt(2, bookingId);
	        
	        int rowsAffected = preparedStatement.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        throw new SQLException("Error updating booking status for booking ID: " + bookingId, e);
	    }
	}

	public boolean cancelBooking(int bookingId) throws SQLException {
	    // First verify if the booking can be cancelled (not already completed or cancelled)
	    String checkQuery = "SELECT status FROM booking WHERE bookingNumber = ?";
	    String updateQuery = "UPDATE booking SET status = ? WHERE bookingNumber = ?";
	    
	    try (Connection conn = DBConnectionFactory.getConnection()) {
	        // Check current booking status
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
	            checkStmt.setInt(1, bookingId);
	            ResultSet rs = checkStmt.executeQuery();
	            
	            if (rs.next()) {
	                String currentStatus = rs.getString("status");
	                if (BookingStatus.COMPLETED.toString().equals(currentStatus) || 
	                    BookingStatus.CANCELLED.toString().equals(currentStatus)) {
	                    return false; // Cannot cancel completed or already cancelled bookings
	                }
	            } else {
	                return false; // Booking not found
	            }
	        }
	        
	        // Proceed with cancellation
	        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
	            updateStmt.setString(1, BookingStatus.CANCELLED.toString());
	            updateStmt.setInt(2, bookingId);
	            
	            int rowsAffected = updateStmt.executeUpdate();
	            return rowsAffected > 0;
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error cancelling booking ID: " + bookingId, e);
	    }
	}

	// Helper method to create Booking object from ResultSet
	private Booking createBookingFromResultSet(ResultSet rs) throws SQLException {
	    Customer customer = new Customer(
	        rs.getInt("customerID"),
	        rs.getInt("userID"),
	        rs.getString("name"),
	        rs.getString("password"),
	        rs.getString("email"),
	        UserRole.valueOf(rs.getString("role")),
	        rs.getString("address"),
	        rs.getString("mobileNumber")
	    );

	    Driver driver = new Driver(
	        rs.getInt("driverID"),
	        rs.getInt("userID"),
	        rs.getString("name"),
	        rs.getString("password"),
	        rs.getString("email"),
	        UserRole.valueOf(rs.getString("role")),
	        rs.getString("licenseNumber"),
	        rs.getString("mobileNumber"),
	        rs.getString("address"),
	        DriverStatus.valueOf(rs.getString("status"))
	    );

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

	    return new Booking(
	        rs.getInt("bookingNumber"),
	        customer,
	        rs.getTimestamp("bookingDateTime").toLocalDateTime(),
	        rs.getString("pickupLocation"),
	        rs.getString("destination"),
	        rs.getDouble("distance"),
	        BookingStatus.valueOf(rs.getString("status")),
	        cab,
	        driver
	    );
	}
	

}
