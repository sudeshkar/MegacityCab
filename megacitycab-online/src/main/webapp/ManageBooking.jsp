<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.sql.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.megacitycab.model.Cab"%>
<%@page import="com.megacitycab.model.Driver"%>
<%@page import="com.megacitycab.model.Customer"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@page import="com.megacitycab.model.Booking"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("/index.jsp");
    return;
}

User LoggedInUser = SessionUtils.getLoggedInUser(request);
String userRole = LoggedInUser.getRole().toString();
%>
<%
List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
List<Customer> customers = (List<Customer>) request.getAttribute("customers");
List<Driver> drivers = (List<Driver>) request.getAttribute("drivers");
List<Cab> cabs = (List<Cab>) request.getAttribute("cabs");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Bookings</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ManageBooking.css">
<script>
    function calculateFare() {
        var distance = parseFloat(document.getElementById("distance").value);
        var baseFare = 150;  
        var ratePerKm = 120; 

        if (!isNaN(distance) && distance > 0) {
            var estimatedFare = baseFare + (distance * ratePerKm);
            document.getElementById("estimatedFare").value = estimatedFare; 
        } else {
            alert("Please enter a valid distance.");
        }
    }
</script>
</head>
<body onload="initAutocomplete()">
<h2>Manage Bookings</h2>

<% if ("ADMIN".equals(userRole)) { %>
    <!-- Add Booking Form -->
    <h3>Add New Booking</h3>
    <form action="<%= request.getContextPath() %>/BookingController/AddBooking" method="POST">
        <input type="hidden" name="action" value="addBooking">
        <table>
            <tr>
                <td><label for="customerID">Customer:</label></td>
                <td>
                    <select id="customerID" name="customerID" required onchange="fetchCustomerDetails()">
                        <option value="">Select Customer</option>
                        <% for (Customer customer : customers) { %>
                            <option value="<%= customer.getCustomerID() %>"><%= customer.getName() %></option>
                        <% } %>
                    </select>
                    <div id="customerDetails"></div>
                </td>
            </tr>
            <tr>
                <td><label for="pickupLocation">Pickup Location:</label></td>
                <td><input type="text" id="locationInput" placeholder="Enter location" name="pickupLocation" required onblur="calculateDistance()">
                <ul id="suggestions"></ul></td>
            </tr>
            <tr>
                <td><label for="destination">Destination:</label></td>
                <td><input type="text" id="locationInput" name="destination" required onblur="calculateFare()" placeholder="Enter location"></td>
            </tr>
            <tr>
                <td><label for="bookingDateTime">Date & Time:</label></td>
                <td><input type="datetime-local" id="bookingDateTime" name="bookingDateTime" required 
                           min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>"></td>
            </tr>
            <tr>
                <td><label for="distance">Distance (km):</label></td>
                <td><input type="text" id="distance" name="distance" placeholder="Enter Distance(km)" onblur="calculateFare()" required></td>
            </tr>
            <tr>
                <td><label for="estimatedFare">Estimated Fare:</label></td>
                <td><input type="text" id="estimatedFare" name="estimatedFare" readonly></td>
            </tr>
            <tr>
                <td><label for="status">Status:</label></td>
                <td>
                    <label><input type="radio" name="status" value="PENDING" checked> ⏳ Pending</label>
                    <label><input type="radio" name="status" value="CONFIRMED"> ✅ Confirmed</label>
                    <label><input type="radio" name="status" value="CANCELLED"> ❌ Cancelled</label>
                    <label><input type="radio" name="status" value="COMPLETED"> 🚗 Completed</label>
                </td>
            </tr>
            <tr>
                <td><label for="cabID">Cab:</label></td>
                <td>
                    <select id="cabID" name="cabID" required>
                        <option value="">Select Cab</option>
                        <% for (Cab cab : cabs) { %>
                            <option value="<%= cab.getCabID() %>"><%= cab.getVehicleNumber() %> - <%= cab.getModel() %></option>
                        <% } %> 
                    </select>
                </td>
            </tr>
            <tr>
                <td><label for="driverID">Driver:</label></td>
                <td>
                    <select id="driverID" name="driverID" required>
                        <option value="">Select Driver</option>
                        <% for (Driver driver : drivers) { %>
                            <option value="<%= driver.getDriverID()%>"><%= driver.getName() %> - <%= driver.getPhoneNumber() %></option>
                        <% } %> 
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <button type="submit">Add Booking</button>
                </td>
            </tr>
        </table>
    </form>
<% } %>

<table border="1">
    <tr>
        <th>Booking ID</th>
        <th>Pickup Location</th>
        <th>Drop Location</th>
        <th>Date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <%
    if (bookings != null) {
        bookings.sort((b1, b2) -> b2.getBookingDateTime().compareTo(b1.getBookingDateTime()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        for (Booking booking : bookings) {
            LocalDateTime bookingDateTime = booking.getBookingDateTime();
            String formattedDate = (bookingDateTime != null) ? bookingDateTime.format(formatter) : "Not Available";
    %>
    <tr>
        <td><%= booking.getBookingNumber() %></td>
        <td><%= booking.getPickupLocation() %></td>
        <td><%= booking.getDestination() %></td>
        <td><%= formattedDate %></td>
        <td><%= booking.getStatus() %></td>
        <td>
            <% if ("CUSTOMER".equals(userRole)) { %>
                <% if (booking.getStatus().toString().equals("PENDING")) { %>
                    <form action="<%= request.getContextPath() %>/CancelBookingServlet" method="POST" style="display:inline;">
                        <input type="hidden" name="bookingNumber" value="<%= booking.getBookingNumber() %>">
                        <button type="submit" onclick="return confirm('Are you sure you want to cancel this booking?');">Cancel</button>
                    </form>
                <% } else if (booking.getStatus().toString().equals("COMPLETED")) { %>
                    <form action="<%= request.getContextPath() %>/ViewBookingDetailController" method="get" style="display:inline;">
                        <input type="hidden" name="bookingNumber" value="<%= booking.getBookingNumber() %>">
                        <button type="submit">View Details</button>
                    </form>
                <% } %>
            <% } else if ("DRIVER".equals(userRole)) { %>
                <form action="<%= request.getContextPath() %>/AcceptBookingController" method="POST" style="display:inline;">
                    <input type="hidden" name="bookingID" value="<%= booking.getBookingNumber() %>">
                    <button type="submit" onclick="return confirm('Are you sure you want to Accept this booking?');">Accept</button>
                </form>
                <form action="<%= request.getContextPath() %>/RejectBookingController" method="POST" style="display:inline;">
                    <input type="hidden" name="bookingID" value="<%= booking.getBookingNumber() %>">
                    <button type="submit" onclick="return confirm('Are you sure you want to cancel this booking?');">Reject</button>
                </form>
            <% } else if ("ADMIN".equals(userRole)) { %>
                <form action="<%= request.getContextPath() %>/UpdateBookingController" method="POST">
                    <input type="hidden" name="bookingID" value="<%= booking.getBookingNumber() %>">
                    <select name="status" required>
                        <option value="PENDING" <%= "PENDING".equals(booking.getStatus().toString()) ? "selected" : "" %>>⏳ Pending</option>
                        <option value="CONFIRMED" <%= "CONFIRMED".equals(booking.getStatus().toString()) ? "selected" : "" %>>✅ Confirmed</option>
                        <option value="CANCELLED" <%= "CANCELLED".equals(booking.getStatus().toString()) ? "selected" : "" %>>❌ Cancelled</option>
                        <option value="COMPLETED" <%= "COMPLETED".equals(booking.getStatus().toString()) ? "selected" : "" %>>🚗 Completed</option>
                    </select>
                    <button type="submit" name="action" value="update" onclick="return confirm('Are you sure you want to Update this booking?');">Update Status</button>
                    <button type="submit" name="action" value="delete" onclick="return confirm('Are you sure you want to delete this booking?');">Delete Booking</button>
                </form>
            <% } %>
        </td>
    </tr>
    <% 
        } // Close for loop
    } else { // Close if (bookings != null)
    %>
    <tr>
        <td colspan="6">No bookings found</td>
    </tr>
    <% } // Close else %>
</table>

<% List<Booking> nonPendingBookings = (List<Booking>) request.getAttribute("nonPendingBookings"); %>
<% if ("DRIVER".equals(userRole)) { %>
    <h2>Bookings Accepted and Rejected List</h2>
    <table border="1">
        <tr>
            <th>Booking ID</th>
            <th>Pickup Location</th>
            <th>Drop Location</th>
            <th>Date</th>
            <th>Status</th>
        </tr>
        <%
        if (nonPendingBookings != null) {
            nonPendingBookings.sort((b1, b2) -> b2.getBookingDateTime().compareTo(b1.getBookingDateTime()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
            for (Booking booking : nonPendingBookings) {
                LocalDateTime bookingDateTime = booking.getBookingDateTime();
                String formattedDate = (bookingDateTime != null) ? bookingDateTime.format(formatter) : "Not Available";
        %>
        <tr>
            <td><%= booking.getBookingNumber() %></td>
            <td><%= booking.getPickupLocation() %></td>
            <td><%= booking.getDestination() %></td>
            <td><%= formattedDate %></td>
            <td><%= booking.getStatus() %></td>
        </tr>
        <% 
            } // Close for loop
        } else { // Close if (nonPendingBookings != null)
        %>
        <tr>
            <td colspan="5">No bookings found</td>
        </tr>
        <% } // Close else %>
    </table>
<% } // Close if ("DRIVER".equals(userRole)) %>

<%@ include file="footer.jsp" %>
</body>
</html>