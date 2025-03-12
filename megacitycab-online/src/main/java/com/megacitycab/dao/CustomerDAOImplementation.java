package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.UserService;

public class CustomerDAOImplementation implements CustomerDAO{

	private UserService userService;

	public CustomerDAOImplementation (){
		userService = UserService.getInstance();
	}



	@Override
	public boolean addCustomer(Customer customer) {
		User user = userService.getUserById(customer.getUserID());
	    if (user == null || !user.getRole().toString().equalsIgnoreCase("Customer")) {
	        return false;
	    }
	    String sql = "INSERT INTO customer (name, address, mobileNumber, phoneNumber, registrationDate, status, userID) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setString(1, customer.getName());
	        ps.setString(2, customer.getAddress());
	        ps.setString(3, customer.getMobilenumber());
	        ps.setString(4, customer.getPhonenumber());
	        ps.setTimestamp(5, Timestamp.valueOf(customer.getRegistrationDate()));
	        ps.setString(6, customer.getStatus().toString());
	        ps.setInt(7, customer.getUserID());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean createCustomer(Customer customer) {
	    String sql = "INSERT INTO customer (name, address, mobileNumber, phoneNumber, registrationDate, status, userID) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setString(1, customer.getName());
	        ps.setString(2, customer.getAddress());
	        ps.setString(3, customer.getMobilenumber());
	        ps.setString(4, customer.getPhonenumber());
	        ps.setTimestamp(5, Timestamp.valueOf(customer.getRegistrationDate()));
	        ps.setString(6, customer.getStatus().toString());
	        ps.setInt(7, customer.getUserID());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	@Override
	public Customer getCustomerById(int customerID) {
		 String sql = "SELECT u.userID, u.userName, u.password, u.email, u.role, " +
                 "c.customerID, c.address, c.mobileNumber, c.phoneNumber, c.registrationDate, c.status " +
                 "FROM users u JOIN customer c ON u.userID = c.userID WHERE c.customerID = ?";

		 Customer customer = null;

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, customerID);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {


	            	customer = new Customer(
							rs.getInt("customerID"),
							rs.getInt("userID"),
							rs.getString("userName"),
							rs.getString("password"),
							rs.getString("email"),
							UserRole.valueOf(rs.getString("role")),
							rs.getString("address"),
							rs.getString("mobileNumber"),
							rs.getString("phoneNumber"),
							rs.getTimestamp("registrationDate").toLocalDateTime(),
							CustomerStatus.valueOf(rs.getString("status").toUpperCase())
							);


	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (customer== null) {
			System.out.println("Customer was Null");
		}
	    else {
	    	System.out.println("Customer fetched");
	    }
	    return customer;
	}


	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> customers = new ArrayList<>();

		List<User> allUsers = userService.getAllUser();
		if (allUsers.isEmpty()) {
	        System.out.println("No users found in userDAOImplementation");
	    } else {
	        System.out.println("Successfully retrieved AllUserList :"+ allUsers.size());
	    }
		String sql = "SELECT * FROM customer WHERE userID = ?";
		try (	Connection connection = DBConnectionFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			for(User user : allUsers)
			{
				ps.setInt(1, user.getUserID());
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()) {
						Customer customer = new Customer(
								rs.getInt("customerID"),
								user.getUserID(),
								user.getName(),
								user.getPassword(),
								user.getEmail(),
								UserRole.valueOf(user.getRole().toString()),
								rs.getString("address"),
								rs.getString("mobileNumber"),
								rs.getString("phoneNumber"),
								rs.getTimestamp("registrationDate").toLocalDateTime(),
								CustomerStatus.valueOf(rs.getString("status").toUpperCase())
								);
                        	customers.add(customer);
					}else {
						System.out.println("No customer found for userID: " + user.getUserID());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (customers.isEmpty()) {
			System.out.println("Customer fetch failed in CustomerDAOImplementation");
		}
		return customers;

	}

	@Override
	public boolean updateCustomer(Customer customer) {
	    String sql = "UPDATE customer SET address = ?, mobileNumber = ?, phoneNumber = ?, status = ? WHERE customerID = ?";

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setString(1, customer.getAddress());
	        ps.setString(2, customer.getMobilenumber());
	        ps.setString(3, customer.getPhonenumber());
	        ps.setString(4, customer.getStatus().toString());
	        ps.setInt(5, customer.getCustomerID());

	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	@Override
	public boolean deleteCustomer(int customerID) {
	    String sql = "DELETE FROM customer WHERE customerID = ?";

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, customerID);
	        int rowsAffected = ps.executeUpdate();

	        return rowsAffected > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	@Override
	public Customer getCustomerByuserID(User user) {
		String sql = "SELECT * FROM customer WHERE userID = ?";
	    Customer customer = null;
	    int userid = user.getUserID();

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, userid);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                // Fetch associated user

	                customer = new Customer();
					customer.setCustomerID(rs.getInt("customerID"));
					customer.setUserID(rs.getInt("userID"));
					customer.setName(rs.getString("name"));
					customer.setAddress(rs.getString("address"));
					customer.setMobilenumber(rs.getString("mobileNumber"));
					customer.setPhonenumber(rs.getString("phoneNumber"));
					customer.setRegistrationDate(rs.getTimestamp("registrationDate").toLocalDateTime());
					customer.setStatus(CustomerStatus.valueOf(rs.getString("status")));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (customer == null) {
	        throw new NullPointerException("Customer not found for user ID: " + userid);
	    }
	    return customer;
	}

	@Override
	public Customer getCustomerByuserID(int userid) {
		String sql = "SELECT * FROM customer WHERE userID = ?";
	    Customer customer = null;

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, userid);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                // Fetch associated user

	                customer = new Customer();
					customer.setCustomerID(rs.getInt("customerID"));
					customer.setUserID(rs.getInt("userID"));
					customer.setName(rs.getString("name"));
					customer.setAddress(rs.getString("address"));
					customer.setMobilenumber(rs.getString("mobileNumber"));
					customer.setPhonenumber(rs.getString("phoneNumber"));
					customer.setRegistrationDate(rs.getTimestamp("registrationDate").toLocalDateTime());
					customer.setStatus(CustomerStatus.valueOf(rs.getString("status")));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (customer == null) {
	        throw new NullPointerException("Customer not found for user ID: " + userid);
	    }
	    return customer;
	}

	@Override
	public List<Customer> getAllCustomerAdmin() {
		List<Customer> customers = new ArrayList<>();

		String sql = "SELECT u.userID, u.userName, u.password, u.email, u.role, c.customerID, c.address, c.mobileNumber, c.phoneNumber, c.registrationDate, c.status " +
                "FROM users u JOIN customer c ON u.userID = c.userID";
		try (Connection connection = DBConnectionFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){

				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						Customer customer = new Customer(
								rs.getInt("customerID"),
								rs.getInt("userID"),
								rs.getString("userName"),
								rs.getString("password"),
								rs.getString("email"),
								UserRole.valueOf(rs.getString("role")),
								rs.getString("address"),
								rs.getString("mobileNumber"),
								rs.getString("phoneNumber"),
								rs.getTimestamp("registrationDate").toLocalDateTime(),
								CustomerStatus.valueOf(rs.getString("status").toUpperCase())
								);
                        	customers.add(customer);
					}
				}


		} catch (Exception e) {
			e.printStackTrace();
		}

		if (customers.isEmpty()) {
			System.out.println("Customer fetch failed in CustomerDAOImplementation");
		}
		return customers;
	}


}
