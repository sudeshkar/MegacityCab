package com.megacitycab.model;

import java.time.LocalDateTime;

public class Bill {
	private int billNumber;
	private Booking booking;
	private double baseAmount;
	private double discountAmount;
	private	 double taxAmount;
	private double totalFare;
	private LocalDateTime billDate;
	private PaymentStatus paymentStatus;

	//For new Bill
	public Bill(Booking booking,double baseAmount, double discountAmount, double taxAmount, double totalFare) {
        this.booking = booking;
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.totalFare = totalFare;
        this.billDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
    }
	//  (for retrieving stored bills)
    public Bill(int billNumber, Booking booking, double baseAmount, double discountAmount,
                double taxAmount, double totalFare, LocalDateTime billDate, PaymentStatus paymentStatus) {
        this.billNumber = billNumber;
        this.booking = booking;
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.totalFare = totalFare;
        this.billDate = billDate;
        this.paymentStatus = paymentStatus;
    }


	public int getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}
	public Booking getBooking() {
		return booking;
	}


	public void setBooking(Booking booking) {
		this.booking = booking;
	}


	public double getBaseAmount() {
		return baseAmount;
	}


	public void setBaseAmount(double baseAmount) {
		this.baseAmount = baseAmount;
	}


	public double getDiscountAmount() {
		return discountAmount;
	}


	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}


	public double getTaxAmount() {
		return taxAmount;
	}


	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}


	public double getTotalFare() {
		return totalFare;
	}


	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}


	public LocalDateTime getBillDate() {
		return billDate;
	}


	public void setBillDate(LocalDateTime billDate) {
		this.billDate = billDate;
	}


	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
