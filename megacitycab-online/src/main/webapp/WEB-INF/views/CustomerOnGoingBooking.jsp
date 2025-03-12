<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.megacitycab.model.Booking"%>
<%@page import="java.util.List"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/header.jsp" %>
<% if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("/index.jsp");
    return;
} %>

<%
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OnGoing Activity</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/table.css">
</head>
<body>
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
        if (bookings == null || bookings.isEmpty()) {
    %>
    <tr>	
        <td colspan="6">No bookings found</td>
    </tr>
    <%
        } else {
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
        	
            <form action="<%= request.getContextPath() %>/ViewBookingDetailController" method="POST" style="display:inline;">
                <input type="hidden" name="bookingID" value="<%= booking.getBookingNumber() %>">
                <button type="submit" >View Driver Details</button>
            </form>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
<%@ include file="/footer.jsp" %>