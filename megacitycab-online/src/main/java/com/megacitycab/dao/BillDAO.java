package com.megacitycab.dao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.megacitycab.model.Bill;
import com.megacitycab.model.PaymentStatus;

public interface BillDAO {
void addBill(Bill bill) throws SQLException;

    Bill getBillByNumber(int billNumber) throws SQLException;

    Bill getBillByBookingNumber(int bookingNumber) throws SQLException;

    List<Bill> getAllBills() throws SQLException;

    List<Bill> getBillsByPaymentStatus(PaymentStatus status) throws SQLException;

    boolean updatePaymentStatus(int billNumber, PaymentStatus status) throws SQLException;

    boolean updateBill(Bill bill) throws SQLException;

    boolean deleteBill(int billNumber) throws SQLException;

    List<Bill> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException;

    double getTotalRevenue() throws SQLException;

}
