package com.megacitycab.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;

public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private DriverService driverService;

	@Override
	public void init() {
		userService = UserService.getInstance();
		driverService = DriverService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/views/Register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		registerUser(request,response);
	}

	public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		 
		String userType =request.getParameter("userType");
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		if (userService.isUserExist(email)) {
			request.getSession().setAttribute("errorMessage", "User already exists with this email.");
			request.getSession().setAttribute("messageType", "error");  
		    response.sendRedirect(request.getContextPath() + "/index.jsp");
		    return;
		}
		else {
		if (userType.equalsIgnoreCase("CUSTOMER")) {
			System.out.println(userType);
			UserRole userRole = UserRole.CUSTOMER;
			User user = new User(userName,email,userRole,password);
			boolean addUser=userService.addUser(user);
			if (addUser) {
				System.out.println("User Created ");
			}
			else {
				System.out.println("User failed to create");
			}
			user = userService.getUserByEmail(user.getEmail());
			String address =request.getParameter("address");
			String phone =request.getParameter("phone");
			Customer customer = new Customer(
					user.getUserID(),
					user.getName(),
					user.getEmail(),
					user.getRole(),
					address,phone,phone
					);
			CustomerService customerService = CustomerService.getInstance();
			boolean addcustomer =customerService.addCustomer(customer);
			if (addcustomer) {
				System.out.println("Customer Added Successfully");
			}
			else {
				System.out.println("failed to add Customer");
			}

		}
		else {
			UserRole userRole = UserRole.DRIVER;
			User user = new User(userName,email,userRole,password);
			String licenseNumber =request.getParameter("license");
			String phone =request.getParameter("phone");
			String address =request.getParameter("address");
			int userid= userService.createUser(user);
			user =userService.getUserById(userid);
			Driver driver = new Driver(
					user.getUserID(),
					user.getName(),
					user.getEmail(),
					userRole,
					licenseNumber,phone,phone,address
					);

			boolean addDriver = driverService.registerDriver(driver,userid);
			if (addDriver) {
				System.out.println("Driver Added Successfully");
			}
			else {
				System.out.println("Failed to Add Driver");
			}
		}
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}
	}

 }

