package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.Customer;
import com.megacitycab.model.User;


public interface CustomerDAO {
	boolean addCustomer(Customer customer);
	Customer getCustomerById(int customerID);
	 List<Customer> getAllCustomer();
	 boolean createCustomer(Customer customer);
	 boolean updateCustomer(Customer customer);
	 boolean deleteCustomer(int customerID);
	 Customer getCustomerByuserID(User user);


}
