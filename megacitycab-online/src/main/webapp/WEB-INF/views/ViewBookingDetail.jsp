<%@page import="com.megacitycab.model.Bill"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.megacitycab.model.Booking"%>
<%@page import="com.megacitycab.model.Cab"%>
<%@page import="com.megacitycab.model.Driver"%>
<%@page import="com.megacitycab.model.Customer"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/header.jsp" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("/index.jsp");
    return;
}
Bill bill = (Bill) request.getAttribute("bill");
Booking booking = (Booking) request.getAttribute("booking");
if (booking == null) {
    out.println("<p style='color:red;'>Booking not found.</p>");
    return;
}

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
String formattedDate = booking.getBookingDateTime() != null ? booking.getBookingDateTime().format(formatter) : "Not Available";

if(bill !=null){
double baseFare = bill.getBaseAmount(); 
double ratePerKm = 120.0; 
double distance = booking.getDistance(); 
double totalFare = bill.getTaxAmount();
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Booking #<%= booking.getBookingNumber() %> Details</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/ViewBooking.css">
</head>
<body>
<h2>Booking #<%= booking.getBookingNumber() %> Details</h2>

<div class="booking-details">
<div class="card">
	<h3>Booking Information</h3>
    <p><strong>Customer Name:</strong> <%= booking.getCustomer().getName() %></p>
    <p><strong>Pickup Location:</strong> <%= booking.getPickupLocation() %></p>
    <p><strong>Destination:</strong> <%= booking.getDestination() %></p>
    <p><strong>Date & Time:</strong> <%= formattedDate %></p>
    <p><strong>Distance:</strong> <%= booking.getDistance() %> km</p>
    <p><strong>Status:</strong> <%= booking.getStatus() %></p>
    <p><strong>Cab:</strong> <%= booking.getCab().getVehicleNumber() %> - <%= booking.getCab().getModel() %></p>
    <p><strong>Driver:</strong> <%= booking.getDriver().getName() %> (<%= booking.getDriver().getPhoneNumber() %>)</p>
  <div class="card2">
  </div>
</div>
    
</div>
<%if(bill !=null || !booking.getStatus().toString().equals("CONFIRMED")) {%>
<div class="billing-details">
    <h3>Billing Details</h3>
    <p><strong>Base Fare:</strong> <%= bill.getBaseAmount() %> LKR</p>
    <p><strong>Tax :</strong> <%=bill.getTaxAmount() %></p>
    <p><strong>Total Fare:</strong> <%= bill.getTotalFare() %> LKR</p>
</div>
<%} %>
<form action="<%= request.getContextPath() %>/BookingController/list" method="get">
<button class=button type="submit"> Back to Manage Booking </button> 
</form>

<%@ include file="/footer.jsp" %>
</body>
</html>