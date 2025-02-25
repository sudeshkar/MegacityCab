package com.megacitycab.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.megacitycab.dao.CabDAO;
import com.megacitycab.dao.CabDAOImplementation;
import com.megacitycab.dao.DBConnectionFactory;

public class CabService {
	private static CabService instance;
	private CabDAO cabDAO;
	
	private CabService() throws SQLException{
		Connection connection = DBConnectionFactory.getConnection();
		this.cabDAO = new CabDAOImplementation();
		
	}
	
	public static CabService getInstance()throws SQLException
	{
		if (instance == null) {
            synchronized (CabService.class) {
                if (instance == null) {
                    instance = new CabService();
                }
            }
        }
		
        return instance;
		
	}
	
	

}
