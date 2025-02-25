package com.megacitycab.dao;

import java.util.List;

import com.megacitycab.model.Cab;


public interface CabDAO {
	boolean addCab(Cab cab);
	Cab getCabById(int userId);
	Cab getCabByEmail(String email);
	 List<Cab> getAllCabs();
	 boolean updateCab(Cab Cab);
	 boolean deleteCab(int Cabid);
   
}
