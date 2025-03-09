<%@page import="com.megacitycab.model.UserRole"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/views/validation/loginCheck.jsp" />

<% 
User loggedInUser = (User) session.getAttribute("loggedInUser");
if (loggedInUser == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MegaCity Cab Booking </title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/header.css">
</head>
<body>
<div>
    <header>
        <nav>
            <ul class="nav-list">
            	<li><a href="<c:url value='/HomeController'/>"> MEGACITY Cabs <br><%= (loggedInUser != null) ? loggedInUser.getName() : "Guest" %>!</a></li>
                <li><a href="<c:url value='/HomeController'/>"> Home</a></li>
                <% if (loggedInUser == null || !loggedInUser.getRole().toString().equals("DRIVER")) { %>
           			 <li><a href="<c:url value='/BookCab'/>">Book Cab</a></li>
        			<% } %>
                <li><a href="<c:url value='/BookingController/list'/>">Manage Booking</a></li>
                <% if (loggedInUser.getRole().toString().equals("ADMIN")) { %>
           			 <li><a href="<c:url value='/AddCabController'/>">Add Cab</a></li>
        			<% } %>
        		<% if (loggedInUser.getRole().toString().equals("ADMIN")) { %>
           			 <li><a href="<c:url value='/ProcessBillController'/>">Manage Bills</a></li>
        			<% } %>
                <li><a href="<c:url value='/AboutUs.jsp'/>">About Us</a></li>
                <li><a href="help.jsp">Help</a></li>
                <li><a href="<c:url value='/logout'/>" > Logout</a></li>
            </ul>
        </nav>
    </header>
    </div>
</body>
</html>