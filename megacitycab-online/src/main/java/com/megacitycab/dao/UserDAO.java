package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

public interface UserDAO {
	 boolean addUser(User user);
	 User getUserById(int userId);
	 User getUserByEmail(String email);
	 List<User> getAllUsers();
	 boolean updateUser(User user);
	 boolean deleteUser(int userId);
	
	User login(String email,String password);
	
	
}


