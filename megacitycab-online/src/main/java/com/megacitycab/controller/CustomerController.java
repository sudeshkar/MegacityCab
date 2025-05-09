package com.megacitycab.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.megacitycab.service.CustomerService;

public class CustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CustomerService customerService;

	@Override
	public void init() {
		customerService = CustomerService.getInstance();
		}


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
