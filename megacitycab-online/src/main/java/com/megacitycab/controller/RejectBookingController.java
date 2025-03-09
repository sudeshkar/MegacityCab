package com.megacitycab.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.service.BookingService;

public class RejectBookingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookingService bookingService;
	
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
	}
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 
	}

	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bookingID = request.getParameter("bookingID");
	    System.out.println("Received bookingID: " + bookingID);
	    if (bookingID == null || bookingID.isEmpty()) {
	        System.out.println("Error: bookingID is null or empty.");
	        response.sendRedirect("/home.jsp");  
	        return;
	    }
			 rejectBooking(request,response);
			 
			 response.sendRedirect(request.getContextPath() + "/BookingController/list"); 
		 
	}


	private void rejectBooking(HttpServletRequest request, HttpServletResponse response) {
		int bookingNumber =Integer.parseInt(request.getParameter("bookingID"));
		 Booking booking= bookingService.getBookingById(bookingNumber);
		 String status = BookingStatus.CANCELLED.toString();
		 bookingService.updateBooking(booking,status);
		
	}

}
