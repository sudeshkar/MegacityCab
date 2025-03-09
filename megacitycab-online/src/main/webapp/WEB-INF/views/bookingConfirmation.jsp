<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@page import="com.megacitycab.model.Booking"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("index.jsp");
    return;
}
int bookingNumberParam = (int)request.getAttribute("bookingNumber");
String customerEmailParam = (String)request.getAttribute("customerEmail");
User user = SessionUtils.getLoggedInUser(request);
Booking booking = (Booking)request.getSession().getAttribute("booking");
%>
<%
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    String formattedDateTime = booking.getBookingDateTime().format(formatter);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Booking Confirmation</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/BookingConfirmation.css">
</head>
<body>
    <div class="container">
        <h1>Booking Confirmation</h1>
        <div class="confirmation-box">
            <h2>Thank you for your booking!</h2>
            <p>Your booking has been confirmed with the following details:</p>
            
            <div class="booking-details">
                <p><strong>Booking Number:</strong><%= bookingNumberParam %></p>
                <p><strong>Pickup Location:</strong> <%= booking.getPickupLocation() %></p>
                <p><strong>Destination:</strong> <%= booking.getDestination() %></p>
                <p><strong>Date/Time:</strong> <%= formattedDateTime %></p>
                <p><strong>Distance:</strong> <%= booking.getDistance() %> km</p>
                <p><strong>Cab Type:</strong> <%= booking.getCab().getCategory().toString()%></p>
                <p><strong>Driver:</strong> <%= booking.getDriver().getName() %></p>
            </div>
            
            <p>A confirmation email has been sent to <%= customerEmailParam %>.</p>
            
            <div class="button-group">
                <a href="<c:url value='/home.jsp'/>" class="btn">Back to Home</a>
                <a href="<c:url value='/BookingController/list'/>" class="btn">View All Bookings</a>
            </div>
        </div>
    </div>
</body>
</html>