package com.megacitycab.controller;


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
import com.megacitycab.model.Cab;
import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.User;
import com.megacitycab.service.BookingService;
import com.megacitycab.service.CabService;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.utils.SessionUtils;

@WebServlet("/AddBooking")
public class BookingController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingService bookingService;
	private DriverService driverService;


	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
		driverService = DriverService.getInstance();
	}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
		String pathInfo = request.getPathInfo();
		System.out.println("PathInfo: " + pathInfo);

		if (pathInfo == null ||pathInfo.equals("/list")) {
	        try {
				listBookings(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    } else {
	        System.out.println("incorrect Path");
	    }
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
		String action = request.getParameter("action");
		if (action.equals("addBooking")) {
			addbooking(request, response);
		}
		System.out.println("Nothing");
		response.sendRedirect(request.getContextPath() + "/BookingController/list");

	}

	private void listBookings(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	List<Booking> bookingList = new ArrayList<>();
		try {
			User LoggedInUser = SessionUtils.getLoggedInUser(request);
			String userRole = LoggedInUser.getRole().toString();
			System.out.println("Going to check userRole");
			if(LoggedInUser.getRole().toString().equalsIgnoreCase("CUSTOMER")) {
				int userid =LoggedInUser.getUserID();
				System.out.println(userid);
				CustomerService customerService = CustomerService.getInstance();
				Customer customer =customerService.getCustomerByUserId(LoggedInUser);
				int customerID = customer.getCustomerID();
				List<Booking> bookings =bookingService.getBookingByCustomerID(customerID);
				if (bookings == null || bookings.isEmpty()) {
		            System.out.println("Booking List is Empty or Null");
		        } else {
		            System.out.println("Bookings Found: " + bookings.size());
		        }
				request.setAttribute("bookings", bookings);
				request.getRequestDispatcher("/WEB-INF/views/ManageBooking.jsp").forward(request, response);
			}else if(LoggedInUser.getRole().toString().equalsIgnoreCase("DRIVER")) {
				int userid =LoggedInUser.getUserID();
				Driver driver= driverService.getDriverByUserID(userid);
				List<Booking> bookings =bookingService.getBookingByDriverID(driver.getDriverID());
				if (bookings == null || bookings.isEmpty()) {
		            System.out.println("Booking List is Empty or Null");
		        } else {
		            System.out.println("Bookings Found: " + bookings.size());
		        }
				List<Booking> nonPendingBookings =bookingService.getNonPendingBookings(driver.getDriverID());
				if (nonPendingBookings == null || nonPendingBookings.isEmpty()) {
		            System.out.println("nonPendingBookings List is Empty or Null");
		        } else {
		            System.out.println("nonPendingBookings Found: " + nonPendingBookings.size());
		        }
				request.setAttribute("bookings", bookings);
				request.setAttribute("nonPendingBookings", nonPendingBookings);
				request.getRequestDispatcher("/WEB-INF/views/ManageBooking.jsp").forward(request, response);

			}

			else {
			bookingList = bookingService.getAllBookings();
			request.setAttribute("bookings", bookingList);
			CustomerService customerService = CustomerService.getInstance();
            List<Customer> customers = customerService.getAllCustomers();
            DriverService driverService = DriverService.getInstance();
            List<Driver> drivers = driverService.getAllDrivers();
            CabService cabService = CabService.getInstance();
            List<Cab> cabs = cabService.getAllCabs();
            request.setAttribute("cabs", cabs);
            request.setAttribute("drivers", drivers);
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/views/ManageBooking.jsp").forward(request, response);
            }
		} catch ( SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
		}


    }

	public void addbooking(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		int customerID = Integer.parseInt(request.getParameter("customerID"));
        String pickupLocation = request.getParameter("pickupLocation");
        String destination = request.getParameter("destination");
        double distance = Double.parseDouble(request.getParameter("distance"));
        int cabID = Integer.parseInt(request.getParameter("cabID"));
        int driverID = Integer.parseInt(request.getParameter("driverID"));


        CustomerService customerService = CustomerService.getInstance();
        Customer customer =customerService.getCustomerByID(customerID);
        System.out.println(customerID);

        CabService cabService = CabService.getInstance();
        Cab cab = cabService.getCabByCabID(cabID);
        System.out.println(cabID);

        DriverService driverService = DriverService.getInstance();
        Driver driver = driverService.getDriverByID(driverID);
        System.out.println(driverID);

        Booking booking = new Booking(
        		customer,pickupLocation,destination,distance,cab,
        		driver);
        bookingService.addBooking(booking);


	}

}
