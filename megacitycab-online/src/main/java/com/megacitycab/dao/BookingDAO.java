package com.megacitycab.dao;

import java.sql.Connection;	
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

public class BookingDAO {
	
	private Connection conn;
	
	public BookingDAO(Connection Conn) {
		this.conn = conn;
		
	}
	
	public BookingDAO() {
		 
		
	}
	
	
	public int addBooking(Booking booking) {
	    int bookingNumber = 0;
	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(
	             "INSERT INTO booking (customerID, bookingDateTime, pickupLocation, destination, distance, status, cabID, driverID) " +
	             "VALUES (?, ?, ?, ?, ?, 'CONFIRMED', ?, ?)", 1)) {
	        
	        stmt.setInt(1, booking.getCustomer().getCustomerID());
	        stmt.setTimestamp(2, Timestamp.valueOf(booking.getBookingDateTime()));
	        stmt.setString(3, booking.getPickupLocation());
	        stmt.setString(4, booking.getDestination());
	        stmt.setDouble(5, booking.getDistance());
	        stmt.setInt(6, booking.getCab().getCabID());
	        stmt.setInt(7, booking.getDriver().getDriverID());
	        
	        int affectedRows = stmt.executeUpdate();
	        
	        if (affectedRows > 0) {
	           System.out.println("Booking Added");
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                bookingNumber = generatedKeys.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return bookingNumber;
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


	public List<Booking> getBookingByCustomerID(int customerID) throws Exception{
		List<Booking> bookings = new ArrayList<Booking>();
		String query= "SELECT * FROM booking WHERE customerID = ?";
		
		try(Connection connection = DBConnectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)){
			 
			CustomerDAO customerDAO = new CustomerDAOImplementation(connection);
	        DriverDAO driverDAO = new DriverDAOImplementation(connection);
	        CabDAO cabDAO = new CabDAOImplementation(connection,driverDAO);
			
			statement.setInt(1, customerID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				int bookingNumber = rs.getInt("bookingNumber");
	            int customerid = rs.getInt("customerID");
	            LocalDateTime bookingDateTime = rs.getTimestamp("bookingDateTime").toLocalDateTime();
	            String pickupLocation = rs.getString("pickupLocation");
	            String destination = rs.getString("destination");
	            double distance = rs.getDouble("distance");
	            BookingStatus status = BookingStatus.valueOf(rs.getString("status"));
	            int cabID = rs.getInt("cabID");
	            int driverID = rs.getInt("driverID");
	            
	            Customer customer = customerDAO.getCustomerById(customerid);
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
	
	
	public List<Booking> getBookingByUserID(int userid){
		List<Booking> bookings = new ArrayList<Booking>();
		try(Connection connection = DBConnectionFactory.getConnection()){
				
			UserDAO userDAO = new UserDAOImplementation(connection);
			User user = userDAO.getUserById(userid);
			System.out.println(user.getName());
			CustomerDAO customerDAO = new CustomerDAOImplementation(connection);
			int customerid = customerDAO.getCustomerByuserID(user).getCustomerID();
			System.out.println(customerid);
			bookings = getBookingByCustomerID(customerid);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bookings;
	}

}
