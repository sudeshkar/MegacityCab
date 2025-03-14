package com.megacitycab.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.UserService;
 
public class AddAdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	@Override
	public void init() throws ServletException{
		userService = UserService.getInstance();
		
	}
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> users = userService.getUserByRole(UserRole.ADMIN);
		request.setAttribute("users", users);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/AddAdminUsers.jsp");
		dispatcher.forward(request, response);
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 String name = request.getParameter("name");
	 String password = request.getParameter("password");
	 String email = request.getParameter("email");
	 if (userService.isUserExist(email)) {
          System.out.println("User Already Existing");
         request.setAttribute("Message", "User already exists with this email.");
         request.getRequestDispatcher("/WEB-INF/views/admin/AddAdminUsers.jsp").forward(request, response);
     }
	 else {
	 User user = new User(
			 		name,
			 		email,
			 		UserRole.ADMIN,
			 		password
			 );
	 
	 userService.addUser(user);
	 System.out.println("User Added Successfully.");
	 request.setAttribute("Message", "User Added Successfully.");
	 response.sendRedirect(request.getContextPath()+"/AddAdminUser.jsp");
	}
	}
}
