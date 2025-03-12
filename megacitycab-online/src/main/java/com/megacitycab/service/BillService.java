package com.megacitycab.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.megacitycab.dao.BillDAO;
import com.megacitycab.dao.BillDAOImplementation;
import com.megacitycab.dao.DBConnectionFactory;
import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.model.PaymentStatus;


public class BillService {
	private static BillService instance;
	private BillDAO billDAO;
	private static final double TAX_RATE = 0.10;
	private static final double BASE_FARE = 1200;
	
	
	private BillService() throws SQLException {
		Connection conn = DBConnectionFactory.getConnection();
		this.billDAO = new BillDAOImplementation(conn);
	}
	
	public static BillService getInstance()
	{
		if (instance == null) {
            synchronized (BillService.class) {
                if (instance == null) {
                    try {
						instance = new BillService();
					} catch (SQLException e) {

						e.printStackTrace();
					}
                }
            }
        }

        return instance;
	}
	
	public void addBill(Bill bill) throws SQLException {
		  billDAO.addBill(bill);
	}
	public Bill getBillByNumber(int billNumber) throws SQLException{
		
		return billDAO.getBillByNumber(billNumber);
	}
	
	public List<Bill> getAllBills() throws SQLException{
		return billDAO.getAllBills();
	}
	
	public boolean UpdatePaymentStatus(int billNumber, PaymentStatus status) throws SQLException {
		return billDAO.updatePaymentStatus(billNumber, status);
	}
	public boolean updateBill(Bill bill) throws SQLException {
		return billDAO.updateBill(bill);
	}
	public boolean deleteBill(int billNumber) throws SQLException {
		return billDAO.deleteBill(billNumber);
	}
	public List<Bill> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException{
		return billDAO.getBillsByDateRange(startDate, endDate);
	}
	public double getTotalRevenue() throws SQLException {
		return billDAO.getTotalRevenue();
	}
	
	public Bill getBillByBookingNumber(int bookingNumber) throws SQLException{
		return billDAO.getBillByBookingNumber(bookingNumber);
	}
	
	public List<Bill> getBillsByPaymentStatus(PaymentStatus status) throws SQLException {
		return billDAO.getBillsByPaymentStatus(status);
	}
	
	public Bill Createbill(Booking booking,double discountAmount) {
		double taxAmount = BASE_FARE * TAX_RATE;
		double firstkm =10;
		double distance = booking.getDistance()- firstkm;
		double amountPerKm =120;
		double TotalAmount = amountPerKm*distance;
        double totalFare = BASE_FARE - discountAmount + taxAmount+TotalAmount;

         
        if (totalFare < 0) {
            totalFare = 0;
            discountAmount = BASE_FARE;
        }
        return new Bill(booking,BASE_FARE, discountAmount, taxAmount, totalFare);
	}
	
}
