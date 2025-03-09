package com.megacitycab.service;

import java.sql.SQLException;
import java.util.List;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;

public class BookingService {
	private static BookingService instance;
	private BookingDAO bookingDAO;

	private BookingService() {
		this.bookingDAO = new BookingDAO();
	}

		 public static BookingService getInstance() {
		        if (instance == null) {
		            synchronized (BookingService.class) {
		                if (instance == null) {
		                    instance = new BookingService();
		                }
		            }
		        }
		        return instance;
		    }


		 public List<Booking> getAllBookings()throws SQLException{

			 return bookingDAO.getAllBookings();
		 }

		 public List<Booking> getBookingsByUserID(int userid) throws SQLException
		 {
			 return bookingDAO.getBookingByUserID(userid);
		 }

		 public int addBooking(Booking booking) {
			 return bookingDAO.addBooking(booking);
		 }

		 public List<Booking> getBookingByCustomerID(int customerid) throws Exception{

			 return bookingDAO.getBookingByCustomerID(customerid);
		 }

		 public boolean deleteBooking(int BookingNumber) {
			 return bookingDAO.deleteBooking(BookingNumber);
		 }
		 
		 public Booking getBookingById(int bookingid) {
			 return bookingDAO.getBookingById(bookingid);
		 }
		 public boolean updateBooking(Booking booking,String status) {
			 return bookingDAO.updateBooking(booking,status);
		 }
		 public List<Booking> getBookingByDriverID(int driverid) throws Exception{

			 return bookingDAO.getBookingByDriverID(driverid);
		 }
		 public List<Booking> getNonPendingBookings(int driverid) {

			 return bookingDAO.getNonPendingBookings(driverid);
		 }

		 


}



