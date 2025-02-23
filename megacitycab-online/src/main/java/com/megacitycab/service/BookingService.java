package com.megacitycab.service;

import java.sql.SQLException;
import java.util.List;

import com.megacitycab.dao.BookingDAO;
import com.megacitycab.model.Booking;

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

		}



