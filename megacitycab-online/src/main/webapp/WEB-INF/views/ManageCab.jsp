<%@page import="com.megacitycab.model.Cab"%>
<%@page import="com.megacitycab.model.Driver"%>
<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/header.jsp" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
if (!SessionUtils.isUserLoggedIn(request)) {
    response.sendRedirect("index.jsp");
    return;
}

User LoggedInUser = SessionUtils.getLoggedInUser(request);
String userRole = LoggedInUser.getRole().toString();

// Check if user is admin
if (!"ADMIN".equals(userRole)) {
    response.sendRedirect("index.jsp");
    return;
}

List<Cab> cabs = (List<Cab>) request.getAttribute("cabs");
List<Driver> drivers = (List<Driver>) request.getAttribute("drivers");
Cab cabToEdit = (Cab) request.getAttribute("cabToEdit");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Cab</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/ManageCab.css">
<script>
    function confirmDelete(cabID) {
        return confirm("Are you sure you want to delete this cab?");
    }
    
    function validateForm() {
        var vehicleNumber = document.getElementById("vehicleNumber").value;
        var model = document.getElementById("model").value;
        var capacity = document.getElementById("capacity").value;
        var currentLocation = document.getElementById("currentLocation").value;
        
        if (vehicleNumber.trim() === "") {
            alert("Vehicle number is required");
            return false;
        }
        if (model.trim() === "") {
            alert("Model is required");
            return false;
        }
        if (isNaN(capacity) || capacity <= 0) {
            alert("Capacity must be a positive number");
            return false;
        }
        if (currentLocation.trim() === "") {
            alert("Current location is required");
            return false;
        }
        return true;
    }
</script>
</head>
<body>
<h2>Manage Cabs</h2>

    
    <% 
    String message = (String) session.getAttribute("message");
    if (message != null) {
    %>
    <div class="message"><%= message %></div>
    <% 
        session.removeAttribute("message");
    } 
    
    String error = (String) session.getAttribute("error");
    if (error != null) {
    %>
    <div class="error"><%= error %></div>
    <% 
        session.removeAttribute("error");
    } 
    %>
	<!-- Add/Edit Cab Form -->
    <div class="form-container">
        <h3><%= cabToEdit != null ? "Edit Cab" : "Add New Cab" %></h3>
        <form action="<%= request.getContextPath() %>/AddCabController" method="POST" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="<%= cabToEdit != null ? "updateCab" : "addCab" %>">
            <% if (cabToEdit != null) { %>
                <input type="hidden" name="cabID" value="<%= cabToEdit.getCabID() %>">
            <% } %>
            <table>
                <tr>
                    <td><label for="vehicleNumber">Vehicle Number:</label></td>
                    <td><input type="text" id="vehicleNumber" name="vehicleNumber" 
                        value="<%= cabToEdit != null ? cabToEdit.getVehicleNumber() : "" %>" required></td>
                </tr>
                <tr>
                    <td><label for="model">Model:</label></td>
                    <td><input type="text" id="model" name="model" 
                        value="<%= cabToEdit != null ? cabToEdit.getModel() : "" %>" required></td>
                </tr>
                <tr>
                    <td><label for="category">Category:</label></td>
                    <td>
                        <select id="category" name="category" required>
                            <option value="">Select Category</option>
                            <option value="MINI" <%= cabToEdit != null && "MINI".equals(cabToEdit.getCategory().toString()) ? "selected" : "" %>>Mini</option>
                            <option value="SEDAN" <%= cabToEdit != null && "SEDAN".equals(cabToEdit.getCategory().toString()) ? "selected" : "" %>>Sedan</option>
                            <option value="SUV" <%= cabToEdit != null && "SUV".equals(cabToEdit.getCategory().toString()) ? "selected" : "" %>>SUV</option>
                            <option value="LUXURY" <%= cabToEdit != null && "LUXURY".equals(cabToEdit.getCategory().toString()) ? "selected" : "" %>>Luxury</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="capacity">Capacity:</label></td>
                    <td><input type="number" id="capacity" name="capacity" min="1" max="10" 
                        value="<%= cabToEdit != null ? cabToEdit.getCapacity() : "4" %>" required></td>
                </tr>
                <tr>
                    <td><label for="currentLocation">Current Location:</label></td>
                    <td><input type="text" id="currentLocation" name="currentLocation" 
                        value="<%= cabToEdit != null ? cabToEdit.getCurrentLocation() : "" %>" required></td>
                </tr>
                <tr>
                    <td><label for="status">Status:</label></td>
                    <td>
                        <select id="status" name="status" required>
                            <option value="AVAILABLE" <%= cabToEdit != null && "AVAILABLE".equals(cabToEdit.getCabStatus().toString()) ? "selected" : "" %>>Available</option>
                            <option value="BUSY" <%= cabToEdit != null && "BUSY".equals(cabToEdit.getCabStatus().toString()) ? "selected" : "" %>>Busy</option>
                            <option value="MAINTENANCE" <%= cabToEdit != null && "MAINTENANCE".equals(cabToEdit.getCabStatus().toString()) ? "selected" : "" %>>Maintenance</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="driverID">Driver:</label></td>
                    <td>
                        <select id="driverID" name="driverID">
                            <option value="">No Driver Assigned</option>
                            <% if (drivers != null) {
                                for (Driver driver : drivers) { %>
                                    <option value="<%= driver.getDriverID() %>" 
                                        <%= cabToEdit != null && cabToEdit.getDriver().getDriverID() == driver.getDriverID() ? "selected" : "" %>>
                                        <%= driver.getName() %> - <%= driver.getPhoneNumber() %>
                                    </option>
                                <% } 
                            } %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="submit"><%= cabToEdit != null ? "Update Cab" : "Add Cab" %></button>
                        <% if (cabToEdit != null) { %>
                            <a href="<%= request.getContextPath() %>/AddCabController?action=viewCabs" class="cancel-btn">Cancel</a>
                        <% } %>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <!-- Cabs List -->
    <div class="table-container">
        <h3>All Cabs</h3>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Vehicle Number</th>
                <th>Model</th>
                <th>Category</th>
                <th>Capacity</th>
                <th>Location</th>
                <th>Status</th>
                <th>Last Updated</th>
                <th>Driver</th>
                <th>Actions</th>
            </tr>
            <% if (cabs != null && !cabs.isEmpty()) {
                for (Cab cab : cabs) { 
                    String driverName = "Not Assigned";
                    if (drivers != null && cab.getDriver().getDriverID() > 0) {
                        for (Driver driver : drivers) {
                            if (driver.getDriverID() == cab.getDriver().getDriverID()) {
                                driverName = driver.getName();
                                break;
                            }
                        }
                    }
            %>
                <tr>
                    <td><%= cab.getCabID() %></td>
                    <td><%= cab.getVehicleNumber() %></td>
                    <td><%= cab.getModel() %></td>
                    <td><%= cab.getCategory() %></td>
                    <td><%= cab.getCapacity() %></td>
                    <td><%= cab.getCurrentLocation() %></td>
                    <td>
                        <% if ("AVAILABLE".equals(cab.getCabStatus().toString())) { %>
                            <span class="status-available">Available</span>
                        <% } else if ("BUSY".equals(cab.getCabStatus().toString())) { %>
                            <span class="status-busy">Busy</span>
                        <% } else { %>
                            <span class="status-maintenance">Maintenance</span>
                        <% } %>
                    </td>
                    <td><%= cab.getLastUpdated() != null ? cab.getLastUpdated() : "N/A" %></td>
                    <td><%= driverName %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/AddCabController?action=editCab&cabID=<%= cab.getCabID() %>" class="edit-btn">Edit</a>
                        <a href="<%= request.getContextPath() %>/AddCabController?action=deleteCab&cabID=<%= cab.getCabID() %>" 
                           onclick="return confirmDelete(<%= cab.getCabID() %>)" class="delete-btn">Delete</a>
                    </td>
                </tr>
            <% } 
            } else { %>
                <tr>
                    <td colspan="10">No cabs found</td>
                </tr>
            <% } %>
        </table>
    </div>
</div>

<%@ include file="/footer.jsp" %>


</body>
</html>