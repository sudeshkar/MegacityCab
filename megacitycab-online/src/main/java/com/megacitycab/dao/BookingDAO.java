package com.megacitycab.dao;

import java.sql.Connection;	
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.UserRole;

public class BookingDAO {
	
	private Connection conn;
	
	public BookingDAO(Connection Conn) {
		this.conn = conn;
		
	}
	
	public BookingDAO() {
		 
		
	}
	
	
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
			
			int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Booking successfully added.");
	        } else {
	            System.out.println("Failed to add booking.");
	        }
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public List<Booking> getAllBookings() throws SQLException {
		List<Booking> bookings = new ArrayList<Booking>();
		String query= "SELECT * FROM booking";
		try(	Connection connection = DBConnectionFactory.getConnection();
				PreparedStatement stm =connection.prepareStatement(query);
				ResultSet rs = stm.executeQuery()) {
			
			CustomerDAO customerDAO = new CustomerDAOImplementation(connection);
	        DriverDAO driverDAO = new DriverDAOImplementation(connection);
	        CabDAO cabDAO = new CabDAOImplementation(connection,driverDAO);
			
			while(rs.next()) {
				int bookingNumber = rs.getInt("bookingNumber");
	            int customerID = rs.getInt("customerID");
	            LocalDateTime bookingDateTime = rs.getTimestamp("bookingDateTime").toLocalDateTime();
	            String pickupLocation = rs.getString("pickupLocation");
	            String destination = rs.getString("destination");
	            double distance = rs.getDouble("distance");
	            BookingStatus status = BookingStatus.valueOf(rs.getString("status"));
	            int cabID = rs.getInt("cabID");
	            int driverID = rs.getInt("driverID");
	            
	            Customer customer = customerDAO.getCustomerById(customerID);
	            Cab cab = cabDAO.getCabById(cabID);
	            Driver driver = driverDAO.getDriverById(driverID);
	            
	            Booking booking = new Booking(
	                    bookingNumber, customer, bookingDateTime, pickupLocation,
	                    destination, distance, status, cab, driver
	                );
	            
	            bookings.add(booking);
				
			}
			
			
		} catch (Exception e) {
			 e.printStackTrace();
			 throw e;
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

	    try { Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement preparedStatement = conn.prepareStatement(query);
	        
	        preparedStatement.setInt(1, bookingId);
	        
	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            if (rs.next()) {
	                // Create Customer object
	            	Customer customer = new Customer(
		        		    rs.getInt("customerID"),
		        		    rs.getInt("customer_userID"),
		        		    rs.getString("customer_name"),
		        		    rs.getString("customer_password"),
		        		    rs.getString("customer_email"),
		        		    UserRole.valueOf(rs.getString("customer_role")),
		        		    rs.getString("customer_address"),
		        		    rs.getString("customer_mobile"),
		        		    CustomerStatus.valueOf(rs.getString("status")),
		        		    rs.getTimestamp("customer_registrationDate").toLocalDateTime()
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
    		    rs.getInt("customer_userID"),
    		    rs.getString("customer_name"),
    		    rs.getString("customer_password"),
    		    rs.getString("customer_email"),
    		    UserRole.valueOf(rs.getString("customer_role")),
    		    rs.getString("customer_address"),
    		    rs.getString("customer_mobile"),
    		    CustomerStatus.valueOf(rs.getString("status")),
    		    rs.getTimestamp("customer_registrationDate").toLocalDateTime()
    		);

	    Driver driver = new Driver(
	        rs.getInt("driverID"),
	        rs.getInt("userID"),
	        rs.getString("name"),
	        rs.getString("password"),
	        rs.getString("email"),
	        UserRole.valueOf(rs.getString("role")),
	        rs.getString("licenseNumber"),
	        rs.getString("contactNumber"),
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
