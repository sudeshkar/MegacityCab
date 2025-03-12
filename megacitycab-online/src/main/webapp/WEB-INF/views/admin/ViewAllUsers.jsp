<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("/index.jsp");
    return;
}

 
if (!"ADMIN".equals(loggedInUser.getRole().toString())) { // Adjusted for UserRole enum
    response.sendRedirect("/index.jsp");
    return;
}

List<User> users = (List<User>) request.getAttribute("users");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>All Users</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/table.css">
</head>
<body>
<h2>All Users</h2>

<% if (users == null || users.isEmpty()) { %>
    <p>No users found.</p>
<% } else { %>
    <table border="1">
        <thead>
            <tr>
                <th>User ID</th>
                <th>Name</th>  
                <th>Email</th>
                <th>Role</th>
                <th>Last Login Date</th>
            </tr>
        </thead>
        <tbody>
            <% for (User user : users) { 
                String lastLogin = user.getLastLogindate() != null 
                    ? user.getLastLogindate().toLocalDateTime().format(formatter) 
                    : "Never";
            %>
            <tr>
                <td><%= user.getUserID() %></td>
                <td><%= user.getName() %></td> 
                <td><%= user.getEmail() %></td>
                <td><%= user.getRole() %></td>
                <td><%= lastLogin %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

<a href="<%= request.getContextPath() %>/home.jsp">Back to home</a>

<%@ include file="/WEB-INF/views/footer.jsp" %>
</body>
</html>