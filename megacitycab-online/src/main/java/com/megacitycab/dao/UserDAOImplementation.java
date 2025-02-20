package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.mysql.jdbc.Driver;

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
		String sql= "SELECT * FROM users WHERE id= ?";
		String customerSql = "SELECT * FROM customer WHERE userID = ?";
		String driverSql = "SELECT * FROM driver WHERE userID = ?";
		try(PreparedStatement ps=conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String role = rs.getString("role");
				if ("CUSTOMER".equals(role)) {
					try(PreparedStatement customerPs =conn.prepareStatement(customerSql)) {
						customerPs.setInt(1, userId);
						ResultSet customerRs = customerPs.executeQuery();
						 if (customerRs.next()) {
	                            return new Customer(
	                                customerRs.getInt("customerID"),
	                                rs.getInt("userID"),
	                                rs.getString("userName"),
	                                rs.getString("password"),
	                                rs.getString("email"),
	                                UserRole.valueOf(rs.getString("role")),
	                                customerRs.getString("address"),
	                                customerRs.getString("mobileNumber"),
	                                customerRs.getString("phoneNumber"),
	                                customerRs.getTimestamp("registrationDate").toLocalDateTime(),
	                                CustomerStatus.valueOf(customerRs.getString("status"))
	                            );
	                        }
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}else if("DRIVER".equals(role)) {
					 try(PreparedStatement driverPs =conn.prepareStatement(driverSql)) {
						 driverPs.setInt(1, userId);
						 ResultSet driverRs = driverPs.executeQuery();
						 
//						 if (driverRs.next()) {
//							return new Driver( 
//									driverRs.getInt("driverID"),
//		                            rs.getInt("userID"),
//		                            rs.getString("userName"),
//		                            rs.getString("password"),
//		                            rs.getString("email"),
//		                            UserRole.valueOf(rs.getString("role")),
//		                            driverRs.getString("licenseNumber"),
//		                            driverRs.getString("contactNumber"),
//		                            driverRs.getString("phoneNumber"),
//		                            driverRs.getString("address"),
//		                            DriverStatus.valueOf(driverRs.getString("status"))
//		                        );
//							
//						}
						 
						
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
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
		String query="SELECT * FROM Users WHERE email= ? AND password=?";
				
		try{
			Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				UserRole role = UserRole.fromString(rs.getString("role"));
				Timestamp lastLogindate = rs.getTimestamp("lastLoginDate");
				return new User(rs.getInt("userID"),rs.getString("userName"),rs.getString("email"),role, lastLogindate);
				
			}
			
			if (rs.next()) {
	            return new Customer(
	            		rs.getInt("customerID"),
	            		rs.getInt("name"),
	            		rs.getString("name"),
	            		rs.getString("password"),
	            		rs.getString("email"),
	                UserRole.valueOf(rs.getString("role")),
	                rs.getString("address"),
	                rs.getString("mobileNumber"),
	                rs.getString("phoneNumber"),
	                rs.getTimestamp("registrationDate").toLocalDateTime(),
	                CustomerStatus.valueOf(rs.getString("status"))
	            );
	        }
	        
		}catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
