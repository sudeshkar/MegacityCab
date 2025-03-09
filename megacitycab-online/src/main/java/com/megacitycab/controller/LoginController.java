package com.megacitycab.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.megacitycab.model.User;
import com.megacitycab.service.LoginService;

public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LoginService loginService;
	@Override
	public void init() {
		try {
			loginService = LoginService.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/views/index.jsp");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		login(request,response);

	}

	private void login(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		User user = new User();

		String email = request.getParameter("email");
		String password= request.getParameter("pswd");
		user.setEmail(email);
		user.setPassword(password);
		user =loginService.login(email,password);
		try {
			if(user==null) {
				System.out.println("User Null");
				HttpSession session = request.getSession();
				session.setAttribute("loginMessage", "Invalid username or password.");
			    session.setAttribute("messageType", "error");
			    response.sendRedirect(request.getContextPath() + "/index.jsp"); 
			    
			}else
			{
				HttpSession session = request.getSession();
				session.setAttribute("loginMessage", "Login successful!");
			    session.setAttribute("messageType", "success");
				session.setAttribute("loggedInUser", user);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/home.jsp");
				dispatcher.forward(request, response);
				 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		

	}

}
