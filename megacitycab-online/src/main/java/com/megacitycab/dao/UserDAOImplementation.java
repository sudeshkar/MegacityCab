package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;


public class UserDAOImplementation implements UserDAO {


	private Connection conn;
	public UserDAOImplementation(Connection conn) {
		this.conn=conn;
	}
	public UserDAOImplementation() {
		try {
			this.conn = DBConnectionFactory.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	@Override
	public boolean addUser(User user) {
	// Hashing the password before storing For Security
	String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

	String sql = "INSERT INTO users (userName, password, email, role, lastLoginDate) VALUES (?, ?, ?, ?, ?)";

	try (Connection conn = DBConnectionFactory.getConnection();
	PreparedStatement ps = conn.prepareStatement(sql)) {

	ps.setString(1, user.getName());
	ps.setString(2, hashedPassword);
	ps.setString(3, user.getEmail());
	ps.setString(4, user.getRole().toString());
	ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

	int rowsAffected = ps.executeUpdate();

	if (rowsAffected > 0) {
	System.out.println("User created successfully");
	return true;
	} else {
	System.out.println("User creation failed");
	return false;
	}

	} catch (Exception e) {
	System.err.println("Error creating user: " + e.getMessage());
	e.printStackTrace();
	return false;
	}
	}



	@Override
	public User getUserById(int userId) {
		String sql = "SELECT * FROM users WHERE userID = ?";
	    User user = null;
	    try (	Connection conn = DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, userId);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {

	                user = new User();
	                user.setUserID(rs.getInt("userID"));
	                user.setName(rs.getString("userName"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                String roleString = rs.getString("role");
	                try {
	                    UserRole role = UserRole.valueOf(roleString.toUpperCase());
	                    user.setRole(role);
	                } catch (IllegalArgumentException e) {
	                    System.err.println("Invalid role found: " + roleString);

	                }
	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));

	            }

	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    if (user == null) {
	        System.out.println("User not found for userID: " + userId);
	    } else {
	        System.out.println("User retrieved: " + user.getName());
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
	                user.setRole(UserRole.valueOf(rs.getString("role")));
	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return user;
	}
	@Override
	public List<User>  getUserByRole(UserRole role) {
		List<User> users = new ArrayList<>();
	    String sql = "SELECT * FROM users WHERE role = ?";
	    User user = null;
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, role.toString());

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {

	                user = new User();
	                user.setUserID(rs.getInt("userID"));
	                user.setName(rs.getString("userName"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                user.setRole(UserRole.valueOf(rs.getString("role")));
	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));
	                users.add(user);
	                
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return users;
	}

	@Override
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<>();
	    String sql = "SELECT * FROM users";

	    try (	Connection conn = DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {

	    	ResultSet rs = ps.executeQuery();
	    	while (rs.next()) {
	    		User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                String role = rs.getString("role");
                if (role != null) {
                    user.setRole(UserRole.valueOf(role.toUpperCase()));
                }
                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));

	            users.add(user);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if (users.isEmpty()) {
			System.out.println("user fetch no Found ");
		}
	    return users;
	}

	@Override
	public boolean updateUser(User user) {
	    // Input validation
	    if (user == null) {
	        System.err.println("Cannot update user: User object is null");
	        return false;
	    }
	    if (user.getRole() == null) {
	        System.err.println("Cannot update user: Role is null for user ID " + user.getUserID());
	        return false;
	    }

	    String sql = "UPDATE users SET userName = ?, password = ?, email = ?, role = ?, lastLoginDate = ? WHERE userId = ?";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, user.getName());
	        ps.setString(2, user.getPassword());
	        ps.setString(3, user.getEmail());
	        ps.setString(4, user.getRole().toString()); // Safe now due to null check
	        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
	        ps.setInt(6, user.getUserID());

	        int rowsAffected = ps.executeUpdate();
	        System.out.println("Rows affected: " + rowsAffected + " for user ID: " + user.getUserID());
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        System.err.println("Failed to update user with ID " + user.getUserID() + ": " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean deleteUser(int userId) {
	    String sql = "DELETE FROM users WHERE userId = ?";

	    try (	Connection conn= DBConnectionFactory.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {

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
	    String query = "SELECT * FROM users WHERE email = ?";
	    User user = null;

	    try (Connection connection = DBConnectionFactory.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, email);
	        ResultSet rs = statement.executeQuery();

	        if (rs.next()) {
	            String storedPassword = rs.getString("password");
	            boolean authenticated = false;


	            if (storedPassword.startsWith("$2")) {

	                try {
	                    authenticated = BCrypt.checkpw(password, storedPassword);
	                } catch (IllegalArgumentException e) {

	                    authenticated = password.equals(storedPassword);
	                }
	            } else {

	                authenticated = password.equals(storedPassword);


	                if (authenticated) {
	                    upgradePassword(rs.getInt("userID"), password, connection);
	                }
	            }

	            if (authenticated) {

	                user = new User();
	                user.setUserID(rs.getInt("userID"));
	                user.setName(rs.getString("userName"));
	                user.setEmail(rs.getString("email"));

	                String roleString = rs.getString("role");
	                if (roleString != null && !roleString.isEmpty()) {
	                    try {

	                        UserRole role = UserRole.valueOf(roleString.toUpperCase());
	                        user.setRole(role);
	                    } catch (IllegalArgumentException e) {
	                        System.err.println("Invalid role found in database: " + roleString);

	                        user.setRole(UserRole.CUSTOMER);
	                    }
	                } else {

	                    user.setRole(UserRole.CUSTOMER);
	                }

	                user.setLastLogindate(rs.getTimestamp("lastLoginDate"));

	                return user;
	            }
	        }

	        return null;

	    } catch (SQLException e) {
	        System.err.println("Database error during login: " + e.getMessage());
	        return null;
	    }
	}


	private void upgradePassword(int userId, String plainPassword, Connection connection) {
	    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	    String updateQuery = "UPDATE users SET password = ? WHERE userID = ?";

	    try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
	        stmt.setString(1, hashedPassword);
	        stmt.setInt(2, userId);
	        stmt.executeUpdate();
	        System.out.println("Upgraded password to BCrypt hash for user ID: " + userId);
	    } catch (SQLException e) {
	        System.err.println("Failed to upgrade password: " + e.getMessage());
	    }
	}
	@Override
	public int createUser(User user) {
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

	    String sql = "INSERT INTO users (userName, password, email, role, lastLoginDate) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, user.getName());
	        ps.setString(2, hashedPassword);
	        ps.setString(3, user.getEmail());
	        ps.setString(4, user.getRole().toString());
	        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

	        int rowsAffected = ps.executeUpdate();

	        if (rowsAffected > 0) {
	            // Getting UserID
	            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int userId = generatedKeys.getInt(1);
	                    System.out.println("User created successfully with ID: " + userId);
	                    return userId;
	                } else {
	                    System.out.println("User created but couldn't get ID");
	                    return -1;
	                }
	            }
	        } else {
	            System.out.println("User creation failed");
	            return -1;
	        }

	    } catch (Exception e) {
	        System.err.println("Error creating user: " + e.getMessage());
	        e.printStackTrace();
	        return -1;
	    }
	}




}
