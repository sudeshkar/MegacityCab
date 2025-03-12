package com.megacitycab.service;

import java.util.List;

import com.megacitycab.dao.CustomerDAO;
import com.megacitycab.dao.CustomerDAOImplementation;
import com.megacitycab.model.Customer;
import com.megacitycab.model.User;

public class CustomerService {
	private static CustomerService instance;
	private CustomerDAO customerDAO;

	private CustomerService() {
		this.customerDAO = new CustomerDAOImplementation();
	}

	public static CustomerService getInstance() {
		if (instance == null) {
            synchronized (BookingService.class) {
                if (instance == null) {
                    instance = new CustomerService();
                }
            }
        }
        return instance;
	}

	public List<Customer> getAllCustomers(){

		return customerDAO.getAllCustomer();
	}

	public Customer getCustomerByID(int customerid) {
		return customerDAO.getCustomerById(customerid);
	}

	public Customer getCustomerByUserId(User user) {
		return customerDAO.getCustomerByuserID(user);
	}
	public Customer getCustomerByUserId(int userid) {
		return customerDAO.getCustomerByuserID(userid);
	}
	public boolean addCustomer(Customer customer) {
		return customerDAO.addCustomer(customer);
	}

	public boolean updateCustomer(Customer customer) {
		return customerDAO.updateCustomer(customer);
	}
	public boolean deleteCustomer(int customerid) {
		return customerDAO.deleteCustomer(customerid);
	}
	public boolean createCustomer(Customer customer) {
		return customerDAO.createCustomer(customer);
	}

	public List<Customer> getAllCustomerAdmin(){

		return customerDAO.getAllCustomerAdmin();
	}


}
