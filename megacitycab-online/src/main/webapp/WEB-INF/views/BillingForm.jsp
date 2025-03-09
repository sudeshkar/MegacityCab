<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />
<jsp:include page="/WEB-INF/views/validation/loginCheck.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cab Billing</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/Billform.css">
<script src="<%= request.getContextPath() %>/js/billform.js"></script>
</head>
<body>


<div class="container">
        <h2>Cab Billing Form</h2>
        <form action="ProcessBillController" method="post">
            <label for="bookingId">Booking ID:</label>
            <input type="text" id="bookingId" name="bookingId" required>

            <label for="baseRate">Base Rate:</label>
            <input type="number" id="baseRate" name="baseRate" value="50" readonly>

            <label for="perKmRate">Per KM Rate:</label>
            <input type="number" id="perKmRate" name="perKmRate" value="10" readonly>

            <label for="distance">Distance (KM):</label>
            <input type="number" id="distance" name="distance" oninput="calculateFare()" required>

            <label for="peakHourMultiplier">Peak Hour Multiplier:</label>
            <input type="number" id="peakHourMultiplier" name="peakHourMultiplier" value="1.5" readonly>

            <label for="isPeakHour">Is Peak Hour?</label>
            <input type="checkbox" id="isPeakHour" name="isPeakHour" onchange="calculateFare()">

            <label for="discountAmount">Discount Amount:</label>
            <input type="number" id="discountAmount" name="discountAmount" value="0" oninput="calculateFare()">

            <label for="totalFare">Total Fare:</label>
            <input type="number" id="totalFare" name="totalFare" readonly>

            <button type="submit" class="btn">Generate Bill</button>
        </form>
    </div>




<%@ include file="/footer.jsp" %>

</body>
</html>