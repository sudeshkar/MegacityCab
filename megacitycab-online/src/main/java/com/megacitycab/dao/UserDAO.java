package com.megacitycab.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;


public class UserDAO {
	public User login(String email,String password) {
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}


}
