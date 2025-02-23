package com.megacitycab.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.megacitycab.dao.DBConnectionFactory;
import com.megacitycab.dao.UserDAO;
import com.megacitycab.dao.UserDAOImplementation;
import com.megacitycab.model.User;

public class LoginService {
    private static LoginService instance;
    private UserDAO userDAO;

    private LoginService() throws SQLException {
    	Connection connection = DBConnectionFactory.getConnection();
    	this.userDAO = new UserDAOImplementation(connection);
    }
    public static LoginService getInstance() throws SQLException {
        if (instance == null) {
            synchronized (LoginService.class) {
                if (instance == null) {
                    instance = new LoginService();
                }
            }
        }
        return instance;
    }
    
    public User login(User user){
    	return userDAO.login(user.getEmail(), user.getPassword());
    }
    
}
