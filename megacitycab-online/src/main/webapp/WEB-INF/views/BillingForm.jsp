<%@page import="com.megacitycab.model.Booking"%>
<%@page import="java.util.List"%>
<%@page import="org.json.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />
<jsp:include page="/WEB-INF/views/validation/loginCheck.jsp" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cab Billing</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/Billform.css">
    <script src="<%= request.getContextPath() %>/js/billform.js"></script>
    <style>
        
    </style>
    <script>
    const bookingList = <% 
    List<Booking> bookingList = (List<Booking>) request.getAttribute("BookingList");
    if (bookingList != null && !bookingList.isEmpty()) {
        out.print(new JSONArray(bookingList).toString());
    } else {
        out.print("[]");
    }
    %>;
    </script>
</head>
<body>
    <div class="page-layout">
        <div class="form-container">
            <div class="container">
                <h2>Cab Billing Form</h2>
                <form action="ProcessBillController" method="post" id="billingForm">
                    <div class="form-group">
                        <label for="bookingId">Booking ID:</label>
                        <select id="bookingId" name="bookingId" onchange="loadBookingDetails()" required>
                            <option value="">Select a Booking</option>
                            <%
                                if (bookingList != null && !bookingList.isEmpty()) {
                                    for (Booking booking : bookingList) {
                            %>
                                        <option value="<%= booking.getBookingNumber() %>"><%= booking.getBookingNumber() %></option>
                            <%
                                    }
                                } else {
                            %>
                                        <option value="" disabled>No bookings available</option>
                            <%
                                }
                            %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="pickupLocation">Pickup Location:</label>
                        <input type="text" id="pickupLocation" name="pickupLocation">
                    </div>

                    <div class="form-group">
                        <label for="destination">Drop Location:</label>
                        <input type="text" id="destination" name="destination">
                    </div>

                    <div class="form-group">
                        <label for="distance">Distance (KM):</label>
                        <input type="number" id="distance" name="distance" min="0" step="0.1" oninput="calculateFare()" required>
                    </div>

                    <div class="form-group">
                        <label for="baseRate">Base Rate (Rs):</label>
                        <input type="number" id="baseRate" name="baseRate" value="1200" readonly>
                    </div>

                    <div class="form-group">
                        <label for="perKmRate">Per KM Rate (Rs):</label>
                        <input type="number" id="perKmRate" name="perKmRate" value="120" readonly>
                    </div>

                    <div class="form-group">
                        <label for="peakHourMultiplier">Peak Hour Multiplier:</label>
                        <input type="number" id="peakHourMultiplier" name="peakHourMultiplier" value="1.5" readonly>
                    </div>

                    <div class="form-group">
                        <label for="isPeakHour">Is Peak Hour?</label>
                        <input type="checkbox" id="isPeakHour" name="isPeakHour" onchange="calculateFare()">
                    </div>

                    <div class="form-group">
                        <label for="discountAmount">Discount Amount (Rs):</label>
                        <input type="number" id="discountAmount" name="discountAmount" value="0" min="0" oninput="calculateFare()">
                    </div>

                    <div class="form-group">
                        <label for="totalFare">Total Fare (Rs):</label>
                        <input type="number" id="totalFare" name="totalFare" readonly>
                    </div>

                    <button type="submit" class="btn">Generate Bill</button>
                </form>
            </div>
        </div>
        
        <div class="table-container">
            <h2>Available Bookings</h2>
            <table class="bookings-table" id="bookingsTable">
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Customer</th>
                        <th>Pickup</th>
                        <th>Destination</th>
                        <th>Date/Time</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (bookingList != null && !bookingList.isEmpty()) {
                            for (Booking booking : bookingList) {
                    %>
                                <tr data-booking-id="<%= booking.getBookingNumber() %>">
                                    <td><%= booking.getBookingNumber() %></td>
                                    <td><%= booking.getCustomer() != null ? booking.getCustomer().getName() : "N/A" %></td>
                                    <td><%= booking.getPickupLocation() != null ? booking.getPickupLocation() : "N/A" %></td>
                                    <td><%= booking.getDestination() != null ? booking.getDestination() : "N/A" %></td>
                                    <td><%= booking.getBookingDateTime() != null ? booking.getBookingDateTime() : "N/A" %></td>
                                </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        // Enhanced JavaScript for table row selection and form population
        document.addEventListener('DOMContentLoaded', function() {
            const tableRows = document.querySelectorAll('#bookingsTable tbody tr');
            const bookingSelect = document.getElementById('bookingId');
            
            // Add click event to table rows
            tableRows.forEach(row => {
                row.addEventListener('click', function() {
                    // Remove selected class from all rows
                    tableRows.forEach(r => r.classList.remove('selected-row'));
                    
                    // Add selected class to clicked row
                    this.classList.add('selected-row');
                    
                    // Get booking ID from data attribute
                    const bookingId = this.getAttribute('data-booking-id');
                    
                    // Set the select value to the clicked booking
                    if (bookingId && bookingSelect) {
                        bookingSelect.value = bookingId;
                        
                        // Trigger the change event to load booking details
                        const event = new Event('change');
                        bookingSelect.dispatchEvent(event);
                    }
                });
            });
        });
    </script>

<%@ include file="/footer.jsp" %>
</body>
</html>