<%@page import="com.megacitycab.model.Booking"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<%
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
    String userRole = (String) session.getAttribute("userRole");
    session.removeAttribute("userRole");
 
%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ManageBooking.css">
</head>
<body>
<h2>Manage Bookings</h2>

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
            for (Booking booking : bookings) {
    %>
    <tr>
        <td><%= booking.getBookingNumber() %></td>
        <td><%= booking.getPickupLocation() %></td>
        <td><%= booking.getDestination() %></td>
        <td><%= booking.getBookingDateTime() %></td>
        <td><%= booking.getStatus() %></td>
        <td>
            <% if ("CUSTOMER".equals(userRole)) { %>
                <a href="CancelBookingServlet?bookingID=<%= booking.getBookingNumber() %>">Cancel</a>
            <% } else if ("DRIVER".equals(userRole)) { %>
                <a href="AcceptBookingServlet?bookingID=<%= booking.getBookingNumber() %>">Accept</a> |
                <a href="RejectBookingServlet?bookingID=<%= booking.getBookingNumber() %>">Reject</a>
            <% } else if ("ADMIN".equals(userRole)) { %>
                <a href="UpdateBookingStatusServlet?bookingID=<%= booking.getBookingNumber() %>">Update Status</a>
            <% } %>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr>
        <td colspan="6">No bookings found</td>
    </tr>
    <%
        }
    %>
</table>

<%@ include file="footer.jsp" %>
</body>
</html>