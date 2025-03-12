package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.model.PaymentStatus;
import com.megacitycab.service.BookingService;

public class BillDAOImplementation implements BillDAO {

    private Connection connection;
    private BookingService bookingService;

    public BillDAOImplementation(Connection connection) {
        this.bookingService = BookingService.getInstance();
    	try {
			this.connection = DBConnectionFactory.getConnection();
		} catch (SQLException e) {
			System.out.println("DBConnecting failed in BillDAOImplementation");
			e.printStackTrace();
		}
    }

    @Override
    public void addBill(Bill bill) {
        String query = "INSERT INTO bill (bookingNumber, baseAmount, discountAmount, taxAmount, totalFare, billDate, paymentStatus) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bill.getBooking().getBookingNumber());
            statement.setDouble(2, bill.getBaseAmount());
            statement.setDouble(3, bill.getDiscountAmount());
            statement.setDouble(4, bill.getTaxAmount());
            statement.setDouble(5, bill.getTotalFare());
            statement.setTimestamp(6, Timestamp.valueOf(bill.getBillDate()));
            statement.setString(7, bill.getPaymentStatus().toString());

            int rowsInserted = statement.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Bill successfully added." : "Failed to add bill.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bill getBillByNumber(int billNumber) throws SQLException {
        String query = "SELECT * FROM bill WHERE billNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, billNumber);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    Booking booking = bookingService.getBookingById(rs.getInt("bookingNumber"));

                    Bill bill = new Bill(
                    	rs.getInt("billNumber"),
                    	booking,
                    	rs.getDouble("baseAmount"),
                    	rs.getDouble("discountAmount"),
                    	rs.getDouble("taxAmount"),
                    	rs.getDouble("totalFare"),
                        rs.getTimestamp("billDate").toLocalDateTime(),
                        PaymentStatus.valueOf(rs.getString("paymentStatus"))
                    );
                    return bill;
                }
            }
        }
        return null;
    }

    @Override
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {
                Booking booking = bookingService.getBookingById(rs.getInt("bookingNumber"));

                Bill bill = new Bill(
                		rs.getInt("billNumber"),
                    	booking,
                    	rs.getDouble("baseAmount"),
                    	rs.getDouble("discountAmount"),
                    	rs.getDouble("taxAmount"),
                    	rs.getDouble("totalFare"),
                        rs.getTimestamp("billDate").toLocalDateTime(),
                        PaymentStatus.valueOf(rs.getString("paymentStatus"))
                );
                bills.add(bill);
            }
        }
        return bills;
    }

    @Override
    public boolean updatePaymentStatus(int billNumber, PaymentStatus status) throws SQLException {
        String query = "UPDATE bill SET paymentStatus = ? WHERE billNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.toString());
            statement.setInt(2, billNumber);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateBill(Bill bill) throws SQLException {
        String query = "UPDATE bill SET bookingNumber = ?, baseAmount = ?, discountAmount = ?, taxAmount = ?, " +
                       "totalFare = ?, billDate = ?, paymentStatus = ? WHERE billNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bill.getBooking().getBookingNumber());
            statement.setDouble(2, bill.getBaseAmount());
            statement.setDouble(3, bill.getDiscountAmount());
            statement.setDouble(4, bill.getTaxAmount());
            statement.setDouble(5, bill.getTotalFare());
            statement.setTimestamp(6, Timestamp.valueOf(bill.getBillDate()));
            statement.setString(7, bill.getPaymentStatus().toString());
            statement.setInt(8, bill.getBillNumber());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteBill(int billNumber) throws SQLException {
        String query = "DELETE FROM bill WHERE billNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, billNumber);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
	public List<Bill> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill WHERE billDate BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Booking booking = bookingService.getBookingById(rs.getInt("bookingNumber"));

                    Bill bill = new Bill(
                    		rs.getInt("billNumber"),
                        	booking,
                        	rs.getDouble("baseAmount"),
                        	rs.getDouble("discountAmount"),
                        	rs.getDouble("taxAmount"),
                        	rs.getDouble("totalFare"),
                            rs.getTimestamp("billDate").toLocalDateTime(),
                            PaymentStatus.valueOf(rs.getString("paymentStatus"))
                    );
                    bills.add(bill);
                }
            }
        }
        return bills;
    }

    @Override
	public double getTotalRevenue() throws SQLException {
        String query = "SELECT SUM(totalFare) as totalRevenue FROM bill WHERE paymentStatus = 'PAID'";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("totalRevenue");
            }
        }
        return 0.0;
    }

    @Override
    public Bill getBillByBookingNumber(int bookingNumber) throws SQLException {
        String query = "SELECT * FROM bill WHERE bookingNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingNumber);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    Booking booking = bookingService.getBookingById(bookingNumber);

                    return new Bill(
                    		rs.getInt("billNumber"),
                        	booking,
                        	rs.getDouble("baseAmount"),
                        	rs.getDouble("discountAmount"),
                        	rs.getDouble("taxAmount"),
                        	rs.getDouble("totalFare"),
                            rs.getTimestamp("billDate").toLocalDateTime(),
                            PaymentStatus.valueOf(rs.getString("paymentStatus"))
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Bill> getBillsByPaymentStatus(PaymentStatus status) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill WHERE paymentStatus = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.toString());
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Booking booking = bookingService.getBookingById(rs.getInt("bookingNumber"));

                    Bill bill = new Bill(
                    		rs.getInt("billNumber"),
                        	booking,
                        	rs.getDouble("baseAmount"),
                        	rs.getDouble("discountAmount"),
                        	rs.getDouble("taxAmount"),
                        	rs.getDouble("totalFare"),
                            rs.getTimestamp("billDate").toLocalDateTime(),
                            PaymentStatus.valueOf(rs.getString("paymentStatus"))
                    );
                    bills.add(bill);
                }
            }
        }
        return bills;
    }

}
