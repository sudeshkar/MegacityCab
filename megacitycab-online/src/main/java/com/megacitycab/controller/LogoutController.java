package com.megacitycab.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.utils.SessionUtils;


public class LogoutController extends HttpServlet {
private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 request.getSession().invalidate();
		 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  
		    response.setHeader("Pragma", "no-cache");  
		    response.setHeader("Expires", "0");  
	        response.sendRedirect(request.getContextPath() + "/index.jsp");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionUtils.logoutUser(request);
        response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

}
