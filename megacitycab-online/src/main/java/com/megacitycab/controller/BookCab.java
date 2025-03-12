package com.megacitycab.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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


public class BookCab extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookingService bookingService;
	private DriverService driverService;
	private CabService cabService;
	private CustomerService customerService;

	@Override
	public void init() throws ServletException{
		bookingService = BookingService.getInstance();
		driverService = DriverService.getInstance();
		cabService = CabService.getInstance();
		customerService = CustomerService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect("/index.jsp");
            return;
        }
		User user= SessionUtils.getLoggedInUser(request);
		if (user.getRole().toString().equals("ADMIN")) {
			ListCustomerDriverAndCab(request,response);
		}
		else if(user.getRole().toString().equals("CUSTOMER")){
			ListCabAndDriver(request,response);
		}
		else {
			response.sendRedirect("/index.jsp");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect("/index.jsp");
            return;
        }
		User user= SessionUtils.getLoggedInUser(request);
		if (user.getRole().toString().equals("ADMIN")) {
			addBookingAdmin(request,response);
		}
		else {
			addBooking(request,response);
		}

	}

	public void ListCabAndDriver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<Driver> drivers = driverService.getAllDrivers();
        List<Cab> cabs = cabService.getAllCabs();
        request.setAttribute("drivers", drivers);
        request.setAttribute("cabs", cabs);
        request.getRequestDispatcher("/WEB-INF/views/BookCab.jsp").forward(request, response);
	}

	public void ListCustomerDriverAndCab(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<Driver> drivers = driverService.getAllDrivers();
        List<Cab> cabs = cabService.getAllCabs();
        List<Customer> customers = customerService.getAllCustomerAdmin();
        request.setAttribute("drivers", drivers);
        request.setAttribute("cabs", cabs);
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/WEB-INF/views/admin/BookCabAdmin.jsp").forward(request, response);
	}

	public void addBooking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		User user = SessionUtils.getLoggedInUser(request);
		System.out.println(user.getEmail());
		System.out.println(user.getName());
		request.setAttribute("customerEmail", user.getEmail());
		Customer customer =customerService.getCustomerByUserId(user);


		String bookingDateTime = request.getParameter("bookingDateTime");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime parsedDateTime = LocalDateTime.parse(bookingDateTime, formatter);
        String pickupLocation = request.getParameter("pickup");
        String destination = request.getParameter("destination");
        double distance= getDistance(pickupLocation, destination);
        int cabID = Integer.parseInt(request.getParameter("cabID"));
        int driverID = Integer.parseInt(request.getParameter("driverID"));
        System.out.println(customer.getCustomerID());


        Cab cab = cabService.getCabByCabID(cabID);
        System.out.println(cabID);


        Driver driver = driverService.getDriverByID(driverID);
        System.out.println(driverID);

        Booking booking = new Booking(
        		customer,parsedDateTime,pickupLocation,destination,distance,cab,
        		driver);


        int bookingNumber = bookingService.addBooking(booking);
        request.setAttribute("bookingNumber", bookingNumber);
        request.getSession().setAttribute("booking", booking);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/bookingConfirmation.jsp");
        dispatcher.forward(request, response);
        }

	public void addBookingAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int customerid =Integer.parseInt(request.getParameter("customerID"));
		System.out.println("CustomerID :"+ customerid+"In addBookingAdmin");
		Customer customer =customerService.getCustomerByID(customerid);
		String customerEmail = customer.getEmail();
		request.setAttribute("customerEmail", customerEmail);
		System.out.println(customerEmail);
		String bookingDateTime = request.getParameter("bookingDateTime");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime parsedDateTime = LocalDateTime.parse(bookingDateTime, formatter);
        String pickupLocation = request.getParameter("pickup");
        String destination = request.getParameter("destination");
        double distance= getDistance(pickupLocation, destination);
        int cabID = Integer.parseInt(request.getParameter("cabID"));
        int driverID = Integer.parseInt(request.getParameter("driverID"));
        System.out.println(customer.getCustomerID());

        Cab cab = cabService.getCabByCabID(cabID);
        System.out.println(cabID);

        Driver driver = driverService.getDriverByID(driverID);
        System.out.println(driverID);

        Booking booking = new Booking(
        		customer,parsedDateTime,pickupLocation,destination,distance,cab,
        		driver);


        int bookingNumber = bookingService.addBooking(booking);

        request.setAttribute("bookingNumber", bookingNumber);
        request.getSession().setAttribute("booking", booking);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/bookingConfirmation.jsp");
        dispatcher.forward(request, response);
        }

	private static final Map<String, Double> cityDistances = new HashMap<>();

    static {
        cityDistances.put("Colombo-Kandy", 115.0);
        cityDistances.put("Colombo-Galle", 119.0);
        cityDistances.put("Colombo-Jaffna", 397.0);
        cityDistances.put("Colombo-Negombo", 37.0);
        cityDistances.put("Kandy-Galle", 158.0);
        cityDistances.put("Kandy-Jaffna", 367.0);
        cityDistances.put("Kandy-Negombo", 97.0);
        cityDistances.put("Galle-Jaffna", 476.0);
        cityDistances.put("Galle-Negombo", 156.0);
        cityDistances.put("Jaffna-Negombo", 404.0);

        cityDistances.put("Colombo-Bentota", 65.0);
        cityDistances.put("Colombo-Nuwara Eliya", 180.0);
        cityDistances.put("Colombo-Dambulla", 148.0);
        cityDistances.put("Colombo-Matara", 160.0);
        cityDistances.put("Colombo-Anuradhapura", 205.0);
        cityDistances.put("Kandy-Matara", 213.0);
        cityDistances.put("Kandy-Nuwara Eliya", 77.0);
        cityDistances.put("Galle-Nuwara Eliya", 155.0);
        cityDistances.put("Galle-Anuradhapura", 358.0);
        cityDistances.put("Jaffna-Matara", 510.0);

    }

    public static double getDistance(String city1, String city2) {
        String cityPair1 = city1 + "-" + city2;
        String cityPair2 = city2 + "-" + city1;


        if (cityDistances.containsKey(cityPair1)) {
            return cityDistances.get(cityPair1);
        } else if (cityDistances.containsKey(cityPair2)) {
            return cityDistances.get(cityPair2);
        } else {
            System.out.println("Cities not found in the predefined list.");
            return 50; // Default distance
        }
    }
}
