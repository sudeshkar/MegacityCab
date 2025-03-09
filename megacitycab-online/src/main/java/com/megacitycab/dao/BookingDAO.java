package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Cab;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.service.BookingService;
import com.megacitycab.service.CabService;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;

public class BookingDAO {
	private CustomerService customerService;
	private CabService cabService;
	private DriverService driverService;
	private UserService userService;
	
	
	public BookingDAO () {
		customerService = CustomerService.getInstance();
		cabService = CabService.getInstance();
		driverService = DriverService.getInstance();
		userService = UserService.getInstance();
	}
	
	
	



	public int addBooking(Booking booking) {
	    int bookingNumber = 0;
	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(
	             "INSERT INTO booking (customerID, bookingDateTime, pickupLocation, destination, distance, status, cabID, driverID) " +
	             "VALUES (?, ?, ?, ?, ?, 'PENDING', ?, ?)", 1)) {

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
		List<Booking> bookings = new ArrayList<>();
		String query= "SELECT * FROM booking";
		try(	Connection connection = DBConnectionFactory.getConnection();
				PreparedStatement stm =connection.prepareStatement(query);
				ResultSet rs = stm.executeQuery()) {

			

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

	            Customer customer = customerService.getCustomerByID(customerID);
	            Cab cab = cabService.getCabByCabID(cabID);
	            Driver driver = driverService.getDriverByID(driverID);

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
		List<Booking> bookings = new ArrayList<>();
		String query= "SELECT * FROM booking WHERE customerID = ?";

		try(Connection connection = DBConnectionFactory.getConnection();
			PreparedStatement statement = connection.prepareStatement(query)){

			 

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

	            Customer customer = customerService.getCustomerByID(customerid);
	            Cab cab = cabService.getCabByCabID(cabID);
	            Driver driver = driverService.getDriverByID(driverID);

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
		List<Booking> bookings = new ArrayList<>();
		try(Connection connection = DBConnectionFactory.getConnection()){

			 
			User user = userService.getUserById(userid);
			System.out.println(user.getName());
			
			int customerid = customerService.getCustomerByUserId(user).getCustomerID();
			System.out.println(customerid);
			bookings = getBookingByCustomerID(customerid);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookings;
	}

	public boolean deleteBooking(int bookingNumber) {
	    String sql = "DELETE FROM booking WHERE bookingNumber = ?";

	    try (	Connection conn= DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, bookingNumber);


	        int rowsAffected = ps.executeUpdate();


	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean updateBooking(Booking booking) {
	    boolean updated = false;
	    
	    String query = "UPDATE booking SET customerID = ?, bookingDateTime = ?, pickupLocation = ?, " +
	                   "destination = ?, distance = ?, status = ?, cabID = ?, driverID = ? WHERE bookingID = ?";
	    
	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        stmt.setInt(1, booking.getCustomer().getCustomerID());
	        stmt.setTimestamp(2, Timestamp.valueOf(booking.getBookingDateTime()));
	        stmt.setString(3, booking.getPickupLocation());
	        stmt.setString(4, booking.getDestination());
	        stmt.setDouble(5, booking.getDistance());
	        stmt.setString(6, booking.getStatus().toString()); // Assuming status is an ENUM
	        stmt.setInt(7, booking.getCab().getCabID());
	        stmt.setInt(8, booking.getDriver().getDriverID());
	        stmt.setInt(9, booking.getBookingNumber()); // Identify which booking to update

	        int affectedRows = stmt.executeUpdate();
	        updated = affectedRows > 0;
	        
	        if (updated) {
	            System.out.println("Booking Updated Successfully");
	        } else {
	            System.out.println("Booking Update Failed");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return updated;
	}
	
	public boolean updateBooking(Booking booking,String status) {
		boolean updated = false;
	    String query = "UPDATE booking SET customerID = ?, bookingDateTime = ?, pickupLocation = ?, " +
	                   "destination = ?, distance = ?, status = ?, cabID = ?, driverID = ? WHERE bookingNumber = ?";
	    
	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        stmt.setInt(1, booking.getCustomer().getCustomerID());
	        stmt.setTimestamp(2, Timestamp.valueOf(booking.getBookingDateTime()));
	        stmt.setString(3, booking.getPickupLocation());
	        stmt.setString(4, booking.getDestination());
	        stmt.setDouble(5, booking.getDistance());
	        stmt.setString(6, status.toUpperCase());  
	        stmt.setInt(7, booking.getCab().getCabID());
	        stmt.setInt(8, booking.getDriver().getDriverID());
	        stmt.setInt(9, booking.getBookingNumber());  

	        int affectedRows = stmt.executeUpdate();
	        updated = affectedRows > 0;
	        
	        if (updated) {
	            System.out.println("Booking Updated Successfully");
	        } else {
	            System.out.println("Booking Update Failed");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return updated;
	}
	
	public Booking getBookingById(int bookingid) {
		String sql = "SELECT * FROM booking WHERE bookingNumber = ?";
	    Booking booking = null;
	    try (	Connection conn = DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, bookingid);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {

	            	int bookingNumber = rs.getInt("bookingNumber");
		            int customerID = rs.getInt("customerID");
		            LocalDateTime bookingDateTime = rs.getTimestamp("bookingDateTime").toLocalDateTime();
		            String pickupLocation = rs.getString("pickupLocation");
		            String destination = rs.getString("destination");
		            double distance = rs.getDouble("distance");
		            BookingStatus status = BookingStatus.valueOf(rs.getString("status"));
		            int cabID = rs.getInt("cabID");
		            int driverID = rs.getInt("driverID");
		            System.out.println("Fetching Customer ID: " + customerID);
		            Customer customer=customerService.getCustomerByID(customerID);
		            if (customer == null) {
		                System.out.println("Customer was NULL for ID: " + customerID);
		            }
		            System.out.println("Fetching Driver ID: " + driverID);
		            Driver driver= driverService.getDriverByID(driverID);
		            if (driver == null) {
		                System.out.println("Driver was NULL for ID: " + driverID);
		            }
		            System.out.println("Fetching Cab ID: " + cabID);
		            Cab cab = cabService.getCabByCabID(cabID);
		            if (cab == null) {
		                System.out.println("Cab was NULL for ID: " + cabID);
		            }
		            
		            booking = new Booking(
		                    bookingNumber, customer, bookingDateTime, pickupLocation,
		                    destination, distance, status, cab, driver
		                );
		             

		            
	            }
	          
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (booking == null) {
	        System.out.println("Booking Was Null in getBookingById() for bookingID: " + bookingid);
	    } else {
	        System.out.println("Booking Retrieved Successfully: " + booking.getBookingNumber());
	    }
		return booking;
	}
	public List<Booking> getBookingByDriverID(int driverid) {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT * FROM booking WHERE driverID = ? AND status = 'PENDING'";
	    
	    try (	Connection conn = DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, driverid);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	        	if (!rs.isBeforeFirst()) {  
	                System.out.println("No pending bookings found for driver ID: " + driverid);
	            }
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

		            Customer customer = customerService.getCustomerByID(customerID);
		            Cab cab = cabService.getCabByCabID(cabID);
		            Driver driver = driverService.getDriverByID(driverID);
		            
		            if (customer == null) System.out.println("Customer not found for ID: " + customerID);
	                if (cab == null) System.out.println("Cab not found for ID: " + cabID);
	                if (driver == null) System.out.println("Driver not found for ID: " + driverID);


		            Booking booking = new Booking(
		                    bookingNumber, customer, bookingDateTime, pickupLocation,
		                    destination, distance, status, cab, driver
		                );

		            bookings.add(booking);

				}

	          
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
		return bookings;
	}
	
	public List<Booking> getNonPendingBookings(int driverID) {
	    List<Booking> bookings = new ArrayList<>();
	    String sql = "SELECT * FROM booking WHERE status <> 'PENDING' AND driverID = ?";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, driverID);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                int bookingNumber = rs.getInt("bookingNumber");
	                int customerID = rs.getInt("customerID");
	                LocalDateTime bookingDateTime = rs.getTimestamp("bookingDateTime").toLocalDateTime();
	                String pickupLocation = rs.getString("pickupLocation");
	                String destination = rs.getString("destination");
	                double distance = rs.getDouble("distance");
	                BookingStatus status = BookingStatus.valueOf(rs.getString("status").toUpperCase());
	                int cabID = rs.getInt("cabID");
	                
	                Customer customer = customerService.getCustomerByID(customerID);
	                Cab cab = cabService.getCabByCabID(cabID);
	                Driver driver = driverService.getDriverByID(driverID);
	                
	                Booking booking = new Booking(
	                    bookingNumber, customer, bookingDateTime, pickupLocation,
	                    destination, distance, status, cab, driver
	                );
	                
	                bookings.add(booking);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return bookings;
	}


}
