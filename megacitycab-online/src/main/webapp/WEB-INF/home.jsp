<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/header.jsp" %>


<jsp:include page="/WEB-INF/views/validation/loginCheck.jsp" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/home.css">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
<%
String loginMessage = (String) session.getAttribute("loginMessage");
String messageType = (String) session.getAttribute("messageType");
if (loginMessage != null) {
%>
<div class="notification <%= messageType %>">
    <%= loginMessage %>
</div>
<script>
    setTimeout(function() {
        document.getElementById("notification").style.display = "none";
    }, 3000);  
</script>
<%
    session.removeAttribute("loginMessage"); // Remove after displaying
    session.removeAttribute("messageType");
}
%>
<div class="container">
<img alt="cabBG" src="<%= request.getContextPath() %>/images/cabbooking.jpg">
<div class="Wtext">
<div class="welcomeboard">
<h2>Welcome, <div class="Dusername"><%= loggedInUser.getName() %>!  </div> </div>
<h3> to MegaCity Cabs! 
Your Ride, Your Way.  </h3>
</h2>
<h4>MegaCity Cabs is here to provide you with fast, reliable, and affordable transportation in the city. Whether you're heading to work, catching a flight, or simply need to get from point A to point B, we’ve got you covered.</h4>
</div>
</div>
<%@ include file="/footer.jsp" %>
</body>
</html>