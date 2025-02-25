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
			
			int rowsAffected = ps.executeUpdate(); 
	        return rowsAffected > 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public User getUserById(int userId) {
		String sql = "SELECT * FROM users WHERE userID = ?";
	    User user = null;
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, userId);  

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                
	                user = new User();
	                user.setUserID(rs.getInt("userID"));
	                user.setName(rs.getString("userName"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                user.setRole(UserRole.valueOf("role")); 
	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
        return user;
    }

	@Override
	public User getUserByEmail(String email) {
	    String sql = "SELECT * FROM users WHERE email = ?";
	    User user = null;
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, email);  

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                
	                user = new User();
	                user.setUserID(rs.getInt("userID"));
	                user.setName(rs.getString("userName"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                user.setRole(UserRole.valueOf("role")); 
	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return user;
	}

	@Override
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<>();
	    String sql = "SELECT * FROM users";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
	        

	    	while (rs.next()) {
	    		User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRole(UserRole.valueOf("role")); 
                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));;
	            
	            users.add(user);  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return users;
	}

	@Override
	public boolean updateUser(User user) {
	    String sql = "UPDATE users SET userName = ?, password = ?, email = ?, role = ?, lastLoginDate = ? WHERE userId = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setString(1, user.getName());
	        ps.setString(2, user.getPassword());
	        ps.setString(3, user.getEmail());
	        ps.setString(4, user.getRole().toString());  
	        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));  
	        ps.setInt(6, user.getUserID());  
	        
	         
	        int rowsAffected = ps.executeUpdate();
	        
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean deleteUser(int userId) {
	    String sql = "DELETE FROM users WHERE userId = ?";
	    
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, userId);
	        
	        
	        int rowsAffected = ps.executeUpdate();
	        
	        
	        return rowsAffected > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	@Override
	public User login(String email, String password) {
		String query = "SELECT u.*, c.customerID, c.address, c.mobileNumber, c.phoneNumber, c.registrationDate ," +
                "d.driverID, d.licenseNumber, d.contactNumber, d.phoneNumber, d.status " +
                "FROM Users u " +
                "LEFT JOIN Customer c ON u.userID = c.userID " +
                "LEFT JOIN Driver d ON u.userID = d.userID " +
                "WHERE u.email = ? AND u.password = ?";
	    
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
