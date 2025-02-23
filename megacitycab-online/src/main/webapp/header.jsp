<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MegaCity Cab Booking</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/header.css">
</head>
<body>
<div>
    <header>
        <nav>
            <ul class="nav-list">
            	<li><a href="home.jsp"> MEGACITY Cabs</a></li>
                <li><a href="<c:url value='/home.jsp'/>"> Home</a></li>
                <li><a href="<c:url value='/BookCab.jsp'/>">Book a Cab</a></li>
                <li><a href="<c:url value='/BookingController/list'/>">Manage Booking</a></li>
                <li><a href="viewBookingStatus.jsp">Booking Status</a></li>
                <li><a href="">About Us</a></li>
                <li><a href="help.jsp">Help</a></li>
                <li><a href="<c:url value='/index.jsp'/>"> Logout</a></li>
            </ul>
        </nav>
    </header>
    </div>
</body>
</html>