package com.megacitycab.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Booking;
import com.megacitycab.service.BillService;
import com.megacitycab.service.BookingService;

public class UpdateBookingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookingService bookingService;
	private BillService billService;

	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
		billService = BillService.getInstance();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
	    String bookingID = request.getParameter("bookingID");

	    if ("update".equals(action)) {

	        String newStatus = request.getParameter("status");
	        updateBookingStatus(bookingID, newStatus);
	        response.sendRedirect(request.getContextPath() + "/BookingController/list");
	    } else if ("delete".equals(action)) {
	    	
	  
				deleteBooking(bookingID);
				response.sendRedirect(request.getContextPath() + "/BookingController/list");
		
			
	        
	    }
	}

	private void deleteBooking(String bookingID) {
		int bookingNumber =Integer.parseInt(bookingID) ;
		System.out.println(bookingNumber);
		bookingService.deleteBooking(bookingNumber);

	}

	private void updateBookingStatus(String bookingID, String newStatus) {
		int bookingNumber =Integer.parseInt(bookingID) ;
		System.out.println(bookingNumber);
		Booking booking = bookingService.getBookingById(bookingNumber);

		bookingService.updateBooking(booking, newStatus);

	}

}
