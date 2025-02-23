<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="BookCab.css">
</head>
<body>
<div class="container">
<form action="bookCab">
	<div class="form-group">
                <label for="name">Your Name</label>
                <input type="text" id="name" name="name" placeholder="Enter your name" required>
            </div>

            <!-- Pickup Location Field -->
            <div class="form-group">
                <label for="pickup">Pickup Location</label>
                <input type="text" id="pickup" name="pickup" placeholder="Enter pickup location" required>
            </div>

            <!-- Drop-off Location Field -->
            <div class="form-group">
                <label for="dropoff">Drop-off Location</label>
                <input type="text" id="dropoff" name="dropoff" placeholder="Enter drop-off location" required>
            </div>

            <!-- Date and Time of Pickup -->
            <div class="form-group">
                <label for="pickup-time">Pickup Time</label>
                <input type="datetime-local" id="pickup-time" name="pickup-time" required>
            </div>

            <!-- Cab Type Selection -->
            <div class="form-group">
                <label for="cab-type">Cab Type</label>
                <select id="cab-type" name="cab-type" required>
                    <option value="MINI">MINI</option>
                    <option value="SEDAN">SEDAN</option>
                    <option value="SUV">SUV</option>
                    <option value="LUXURY'">LUXURY</option>
                </select>
            </div>


            <!-- Submit Button -->
            <div class="form-group">
                <input type="submit" value="Book Cab">
            </div>

</form>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>