package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;


public class UserDAOImplementation implements UserDAO {
	private Connection conn;
	public UserDAOImplementation(Connection conn) {
		this.conn=conn;
	}

	@Override
	public boolean addUser(User user) {
		String sql = "INSERT INTO users (userName, password, email, role,lastLoginDate) VALUES (?, ?, ?, ?,?)";
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getRole().toString());
			ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        String customerSql = "SELECT * FROM customer WHERE userID = ?";
        String driverSql = "SELECT * FROM driver WHERE userID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                
                if ("CUSTOMER".equals(role)) {
                    try (PreparedStatement customerPs = conn.prepareStatement(customerSql)) {
                        customerPs.setInt(1, userId);
                        ResultSet customerRs = customerPs.executeQuery();
                        
                        if (customerRs.next()) {
                        	return new Customer(
            	        		    rs.getInt("customerID"),
            	        		    rs.getInt("customer_userID"),
            	        		    rs.getString("customer_name"),
            	        		    rs.getString("customer_password"),
            	        		    rs.getString("customer_email"),
            	        		    UserRole.valueOf(rs.getString("customer_role")),
            	        		    rs.getString("customer_address"),
            	        		    rs.getString("customer_mobile"),
            	        		    CustomerStatus.valueOf(rs.getString("status")),
            	        		    rs.getTimestamp("customer_registrationDate").toLocalDateTime()
            	        		);
                        }
                    }
                } else if ("DRIVER".equals(role)) {
                    try (PreparedStatement driverPs = conn.prepareStatement(driverSql)) {
                        driverPs.setInt(1, userId);
                        ResultSet driverRs = driverPs.executeQuery();
                        
                        if (driverRs.next()) {
                            
                            return new Driver(
                                driverRs.getInt("driverID"),
                                rs.getInt("userID"),
                                rs.getString("userName"),
                                rs.getString("password"),
                                rs.getString("email"),
                                UserRole.valueOf(rs.getString("role")),
                                driverRs.getString("licenseNumber"),
                                driverRs.getString("contactNumber"),
                                driverRs.getString("phoneNumber"),
                                driverRs.getString("address"),
                                DriverStatus.valueOf(driverRs.getString("status"))
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public User getUserByEmail(String email) {
	    String sql = "SELECT * FROM users WHERE email = ?";
	    String customerSql = "SELECT * FROM customer WHERE userID = ?";
	    String driverSql = "SELECT * FROM driver WHERE userID = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            String role = rs.getString("role");
	            int userId = rs.getInt("userID");
	            
	            if ("CUSTOMER".equals(role)) {
	                try (PreparedStatement customerPs = conn.prepareStatement(customerSql)) {
	                    customerPs.setInt(1, userId);
	                    ResultSet customerRs = customerPs.executeQuery();
	                    
	                    if (customerRs.next()) {
	                        return new Customer(
	        	        		    rs.getInt("customerID"),
	        	        		    rs.getInt("customer_userID"),
	        	        		    rs.getString("customer_name"),
	        	        		    rs.getString("customer_password"),
	        	        		    rs.getString("customer_email"),
	        	        		    UserRole.valueOf(rs.getString("customer_role")),
	        	        		    rs.getString("customer_address"),
	        	        		    rs.getString("customer_mobile"),
	        	        		    CustomerStatus.valueOf(rs.getString("status")),
	        	        		    rs.getTimestamp("customer_registrationDate").toLocalDateTime()
	        	        		);
	                    }
	                }
	            } else if ("DRIVER".equals(role)) {
	                try (PreparedStatement driverPs = conn.prepareStatement(driverSql)) {
	                    driverPs.setInt(1, userId);
	                    ResultSet driverRs = driverPs.executeQuery();
	                    
	                    if (driverRs.next()) {
	                        return new Driver(
	                            driverRs.getInt("driverID"),
	                            rs.getInt("userID"),
	                            rs.getString("userName"),
	                            rs.getString("password"),
	                            rs.getString("email"),
	                            UserRole.valueOf(rs.getString("role")),
	                            driverRs.getString("licenseNumber"),
	                            driverRs.getString("contactNumber"),
	                            driverRs.getString("phoneNumber"),
	                            driverRs.getString("address"),
	                            DriverStatus.valueOf(driverRs.getString("status"))
	                        );
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<>();
	    String sql = "SELECT * FROM users";
	    String customerSql = "SELECT * FROM customer WHERE userID = ?";
	    String driverSql = "SELECT * FROM driver WHERE userID = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            String role = rs.getString("role");
	            int userId = rs.getInt("userID");
	            
	            if ("CUSTOMER".equals(role)) {
	                try (PreparedStatement customerPs = conn.prepareStatement(customerSql)) {
	                    customerPs.setInt(1, userId);
	                    ResultSet customerRs = customerPs.executeQuery();
	                    
	                    Customer customer = new Customer(
	    	        		    rs.getInt("customerID"),
	    	        		    rs.getInt("customer_userID"),
	    	        		    rs.getString("customer_name"),
	    	        		    rs.getString("customer_password"),
	    	        		    rs.getString("customer_email"),
	    	        		    UserRole.valueOf(rs.getString("customer_role")),
	    	        		    rs.getString("customer_address"),
	    	        		    rs.getString("customer_mobile"),
	    	        		    CustomerStatus.valueOf(rs.getString("status")),
	    	        		    rs.getTimestamp("customer_registrationDate").toLocalDateTime()
	    	        		);
	                    users.add(customer);
	                    }
	                
	            } else if ("DRIVER".equals(role)) {
	                try (PreparedStatement driverPs = conn.prepareStatement(driverSql)) {
	                    driverPs.setInt(1, userId);
	                    ResultSet driverRs = driverPs.executeQuery();
	                    
	                    if (driverRs.next()) {
	                        Driver driver = new Driver(
	                            driverRs.getInt("driverID"),
	                            rs.getInt("userID"),
	                            rs.getString("userName"),
	                            rs.getString("password"),
	                            rs.getString("email"),
	                            UserRole.valueOf(rs.getString("role")),
	                            driverRs.getString("licenseNumber"),
	                            driverRs.getString("contactNumber"),
	                            driverRs.getString("phoneNumber"),
	                            driverRs.getString("address"),
	                            DriverStatus.valueOf(driverRs.getString("status"))
	                        );
	                        users.add(driver);
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return users;
	}

	@Override
	public boolean updateUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(int userId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public User login(String email, String password) {
	    String query = "SELECT * FROM Users WHERE email= ? AND password=?";
	    
	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {
	        
	        statement.setString(1, email);
	        statement.setString(2, password);
	        ResultSet rs = statement.executeQuery();
	        
	        if (rs.next()) {
	        
	        	return UserFactory.createUser(rs);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}


}
