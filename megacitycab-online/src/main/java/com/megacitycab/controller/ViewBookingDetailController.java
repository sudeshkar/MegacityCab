package com.megacitycab.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.service.BillService;
import com.megacitycab.service.BookingService;

public class ViewBookingDetailController extends HttpServlet {
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

		int bookingNumber = Integer.parseInt(request.getParameter("bookingNumber")) ;
		Booking booking = bookingService.getBookingById(bookingNumber);
		try {
			Bill bill = billService.getBillByBookingNumber(bookingNumber);
			request.setAttribute("bill", bill);
			request.setAttribute("booking", booking);
			request.getRequestDispatcher("/WEB-INF/views/ViewBookingDetail.jsp").forward(request, response);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int bookingNumber = Integer.parseInt(request.getParameter("bookingID")) ;
		Booking booking = bookingService.getBookingById(bookingNumber);
		try {
			Bill bill = billService.getBillByBookingNumber(bookingNumber);
			request.setAttribute("bill", bill);
			request.setAttribute("booking", booking);
			request.getRequestDispatcher("/WEB-INF/views/ViewBookingDetail.jsp").forward(request, response);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
