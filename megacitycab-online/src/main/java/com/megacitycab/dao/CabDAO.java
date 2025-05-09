package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.Cab;


public interface CabDAO {
	boolean addCab(Cab cab);
	Cab getCabById(int userId);
	 List<Cab> getAllCabs();
	 boolean updateCab(Cab Cab);
	 boolean deleteCab(int Cabid);
	 public boolean hasActiveCabBookings(int cabID);
	 Cab getCabByDriverID(int driverid);
}
