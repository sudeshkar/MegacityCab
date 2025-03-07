package com.megacitycab.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;

@WebServlet("/Register")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() {
		userService = UserService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("/Register.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		registerUser(request,response);
	}

	public void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userType =request.getParameter("userType");
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
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
			userService.addUser(user);
			Driver driver = new Driver(
					user.getUserID(),
					user.getName(),
					user.getEmail(),
					userRole,
					licenseNumber,phone,phone,address
					);
			DriverService driverService = DriverService.getInstance();
			boolean addDriver =driverService.addDriver(driver);
			if (addDriver) {
				System.out.println("Driver Added Successfully");
			}
			else {
				System.out.println("Failed to Add Driver");
			}
		}
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	@WebServlet("/LoginRedirect")
    public static class LoginRedirectController extends HttpServlet {
        private static final long serialVersionUID = 1L;
        @Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

 }

