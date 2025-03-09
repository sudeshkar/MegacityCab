package com.megacitycab.service;

import java.util.List;

import com.megacitycab.dao.UserDAO;
import com.megacitycab.dao.UserDAOImplementation;
import com.megacitycab.model.User;

public class UserService {
	private static UserService instance;
	private UserDAO userDAO ;

	private UserService() {
		this.userDAO = new UserDAOImplementation();
	}

	public static UserService getInstance() {
		if (instance == null) {
			synchronized (UserService.class) {
				if (instance == null) {
					instance = new UserService();
				}
			}
		}
		return instance;
	}

	public boolean addUser(User user) {
		return userDAO.addUser(user);
	}
	public User getUserById(int userid) {
		return userDAO.getUserById(userid);
	}

	public User getUserByEmail(String email) {
		return userDAO.getUserByEmail(email);
	}
	public List<User> getAllUser() {
		return userDAO.getAllUsers();
	}
	public boolean updateUser(User user) {
		return userDAO.updateUser(user);
	}
	public boolean deleteUser(int userid) {
		return userDAO.deleteUser(userid);
	}
	public int createUser(User user) {
		return userDAO.createUser(user);
	}

}
