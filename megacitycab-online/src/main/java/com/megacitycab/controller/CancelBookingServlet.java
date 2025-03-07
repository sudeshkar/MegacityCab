package com.megacitycab.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.service.BookingService;

public class CancelBookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingService bookingService;

	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int bookingNumber =Integer.parseInt(request.getParameter("bookingNumber")) ;
		 System.out.println(bookingNumber);
		bookingService.deleteBooking(bookingNumber);
		response.sendRedirect(request.getContextPath() + "/BookingController/list");
	}

}
