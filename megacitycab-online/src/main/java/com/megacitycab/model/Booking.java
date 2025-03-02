package com.megacitycab.model;

import java.time.LocalDateTime;

public class Booking {
	private int bookingNumber;
	private Customer customer;
	private LocalDateTime bookingDateTime;
	private String pickupLocation;
	private String destination;
	private double distance;
	private BookingStatus status;
	private Cab cab;
	private Driver driver;
	
	//for new booking
	public Booking(Customer customer, LocalDateTime bookingDateTime, 
            String pickupLocation, String destination, double distance 
            , Cab cab, Driver driver) {
		 this.customer = customer;
		 this.bookingDateTime = bookingDateTime;
		 this.pickupLocation = pickupLocation;
		 this.destination = destination;
		 this.distance = distance;
		 this.status = BookingStatus.PENDING; 
		 this.cab = cab;
		 this.driver = driver;
		}
	
	public Booking(int bookingNumber, Customer customer, String pickupLocation, 
            String destination, double distance, Cab cab, Driver driver) {
			 this.bookingNumber = bookingNumber;
			 this.customer = customer;
			 this.bookingDateTime = LocalDateTime.now(); 
			 this.status = BookingStatus.PENDING; 
			 this.pickupLocation = pickupLocation;
			 this.destination = destination;
			 this.distance = distance;
			 this.cab = cab;
			 this.driver = driver;
			}
	public Booking(Customer customer, String pickupLocation, 
            String destination, double distance, Cab cab, Driver driver) {
			 this.customer = customer;
			 this.bookingDateTime = LocalDateTime.now(); 
			 this.status = BookingStatus.PENDING; 
			 this.pickupLocation = pickupLocation;
			 this.destination = destination;
			 this.distance = distance;
			 this.cab = cab;
			 this.driver = driver;
			}
	
	
	//for fetching existing booking from DB 
	public Booking(int bookingNumber, Customer customer, LocalDateTime bookingDateTime, 
            String pickupLocation, String destination, double distance, 
            BookingStatus status, Cab cab, Driver driver) {
		 this.bookingNumber = bookingNumber;
		 this.customer = customer;
		 this.bookingDateTime = bookingDateTime;
		 this.pickupLocation = pickupLocation;
		 this.destination = destination;
		 this.distance = distance;
		 this.status = status; 
		 this.cab = cab;
		 this.driver = driver;
		}
	public Booking() {
		
	}
	
	

	public int getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(int bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(LocalDateTime bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public Cab getCab() {
		return cab;
	}

	public void setCab(Cab cab) {
		this.cab = cab;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}






}
