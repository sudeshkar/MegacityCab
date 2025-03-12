package com.megacitycab.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.User;
import com.megacitycab.service.BillService;
import com.megacitycab.service.BookingService;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;
import com.megacitycab.utils.SessionUtils;

public class TrackBooking extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingService bookingService;
	private DriverService driverService;
	private UserService userService;
	private CustomerService customerService;
	private BillService billService;

	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
		driverService = DriverService.getInstance();
		userService = UserService.getInstance();
		customerService = CustomerService.getInstance();
		billService = BillService.getInstance();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = SessionUtils.getLoggedInUser(request);
		user = userService.getUserByEmail(user.getEmail());
		if (user.getRole().toString().equals("DRIVER")) {

			Driver driver =driverService.getDriverByEmail(user.getEmail());
			List<Booking> bookings = bookingService.getConfirmedBookingsByDriverID(driver.getDriverID());
			request.setAttribute("bookings", bookings);
			request.getRequestDispatcher("/WEB-INF/views/OnGoingBooking.jsp").forward(request, response);
		}
		else if(user.getRole().toString().equals("CUSTOMER")) {

			Customer customer =customerService.getCustomerByUserId(user.getUserID());
			List<Booking> bookings = bookingService.getConfirmedBookingsByCustomerID(customer.getCustomerID());
			request.setAttribute("bookings", bookings);
			request.getRequestDispatcher("/WEB-INF/views/CustomerOnGoingBooking.jsp").forward(request, response);
		}


	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int bookingID = Integer.parseInt(request.getParameter("bookingID"));
		System.out.println(bookingID);
		Booking booking = bookingService.getBookingById(bookingID);
		boolean result = bookingService.updateBooking(booking, BookingStatus.COMPLETED.toString());
		Bill bill =billService.Createbill(booking, 0);
		try {
			billService.addBill(bill);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		if (result) {
			System.out.println("Update Booking Status");
		}
		else {
			System.out.println("Failed to Update Status");
		}

		response.sendRedirect(request.getContextPath() + "/TrackBooking");


	}



}
