package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

public class CustomerDAOImplementation implements CustomerDAO{
	
	private UserDAO userDAO = new UserDAOImplementation();
	private Connection connection;
	public CustomerDAOImplementation(Connection conn) {
		this.connection =conn;
		 
	}
	
	public CustomerDAOImplementation() {

	}
	public CustomerDAOImplementation(Connection conn,UserDAO userDAO) {
		this.connection =conn;
		this.userDAO = userDAO;
		 
	}
	public CustomerDAOImplementation(UserDAO userDAO ) {
        this.userDAO = new UserDAOImplementation(connection); 
    }
	

	@Override
	public boolean addCustomer(Customer customer) {
		User user = userDAO.getUserById(customer.getUserID());
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
	    String sql = "SELECT * FROM customer WHERE customerID = ?";
	    Customer customer = null;
	    
	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, customerID);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                // Fetch associated user
	                User user = userDAO.getUserById(rs.getInt("userID"));
	                if (user != null) {
	                    
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
	                else {
	                	System.out.println("User not found for userID: ");
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return customer;  
	}


	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> customers = new ArrayList<Customer>();
		
		List<User> allUsers = userDAO.getAllUsers();
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


}
