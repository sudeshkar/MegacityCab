package com.megacitycab.service;


import java.sql.SQLException;
import java.util.List;

import com.megacitycab.dao.CabDAO;
import com.megacitycab.dao.CabDAOImplementation;
import com.megacitycab.model.Cab;

public class CabService {
	private static CabService instance;
	private CabDAO cabDAO;

	private CabService() throws SQLException{

		this.cabDAO = new CabDAOImplementation();

	}

	public static CabService getInstance()
	{
		if (instance == null) {
            synchronized (CabService.class) {
                if (instance == null) {
                    try {
						instance = new CabService();
					} catch (SQLException e) {

						e.printStackTrace();
					}
                }
            }
        }

        return instance;

	}

	public List<Cab> getAllCabs(){
		return cabDAO.getAllCabs();
	}

	public Cab getCabByCabID(int cabid) {
		return cabDAO.getCabById(cabid);
	}

	public boolean addCab(Cab cab) {
		return cabDAO.addCab(cab);
	}

	public boolean updateCab(Cab cab) {
		return cabDAO.updateCab(cab);
	}
	public boolean hasActiveCabBookings(int cabid) {
		return cabDAO.hasActiveCabBookings(cabid);
	}

	public boolean deleteCab(int cabid) {
		return cabDAO.deleteCab(cabid);
	}


}
