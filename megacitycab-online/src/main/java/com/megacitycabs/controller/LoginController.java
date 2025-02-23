package com.megacitycabs.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.megacitycab.model.User;
import com.megacitycab.service.LoginService;

@WebServlet("/Clogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private LoginService loginService;
	public void init() {
		try {
			loginService = LoginService.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		login(request,response);
		
		

		doGet(request, response);
	}
	
	private void login(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		User user = new User();
		
		String email = request.getParameter("email");
		String password= request.getParameter("pswd");
		user.setEmail(email);
		user.setPassword(password);
		user =loginService.login(user);
		if(user==null) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
			
		}else
		{
			HttpSession session = request.getSession();
			session.setAttribute("email", user.getEmail());
			RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
			dispatcher.forward(request, response);
		}
	
	}

}
