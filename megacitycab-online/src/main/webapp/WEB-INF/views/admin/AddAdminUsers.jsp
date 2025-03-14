<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%List<User> users = (List<User>) request.getAttribute("users"); 
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Admin</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/AddAdminUser.css">
<script type="text/javascript">
     window.onload = function() {
         var message = "<%= request.getAttribute("Message") != null ? request.getAttribute("Message") : "" %>";
         if (message) {
             alert(message);  
         }
     };
</script>

</head>
<body>
<h1>ADD ADMIN Users</h1>
 <p>
        <% 
            String message = (String) request.getAttribute("Message");
            if (message != null) {
                out.print(message);
            }
        %>
</p>
 
 
    <form action="<%= request.getContextPath() %>/AddAdminController" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>
        <label type="password">Password</label>
        <input type="password" id="password" name="password" required><br><br>
        
        
 		

        <button type="submit">Add Admin</button>
    </form>
 <h1>Existing ADMIN Users</h1>
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

<a href="<%= request.getContextPath()%>/HomeController">Back to home</a>
</body>
</html>