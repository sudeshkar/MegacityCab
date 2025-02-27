package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.Customer;


public interface CustomerDAO {
	boolean addCustomer(Customer customer);
	Customer getCustomerById(int customerID);
	 List<Customer> getAllCustomer();
	 boolean updateCustomer(Customer customer);
	 boolean deleteCustomer(int customerID);

}
