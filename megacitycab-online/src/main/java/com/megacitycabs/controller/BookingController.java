package com.megacitycabs.controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Booking;
import com.megacitycab.service.BookingService;


public class BookingController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingService bookingService;
	
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String pathInfo = request.getPathInfo();
		System.out.println("PathInfo: " + pathInfo);
	    
		if (pathInfo == null || pathInfo.equals("/list")) {
	        listBookings(request, response);
	    } else {
	        System.out.println("incorrect Path");
	    }
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}
	
	private void listBookings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<Booking> bookingList = new ArrayList<Booking>();
		try {
			bookingList = bookingService.getAllBookings();
			request.setAttribute("bookings", bookingList);
			request.getRequestDispatcher("/ManageBooking.jsp").forward(request, response);
		} catch ( SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
		}
    	
       
    }

}
