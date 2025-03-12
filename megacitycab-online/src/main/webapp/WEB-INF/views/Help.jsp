<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
        body {
            font-family: Arial, sans-serif;
            margin: auto;
            background-color: #f4f4f4;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        h2 {
            color: #007BFF;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .section {
            margin-bottom: 20px;
        }
        p {
            line-height: 1.6;
            color: #555;
      
        }
        a {
            color: #007BFF;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>

</head>
<body>
<div class="container">
        <h1>Mega City Cab Help - Cab Booking System</h1>
        
        <div class="section">
            <h2>Getting Started</h2>
            <p>Welcome to our Cab Booking System! This help page will guide you through the process of booking a cab, managing your bookings, and troubleshooting common issues.</p>
        </div>

        <div class="section">
            <h2>How to Book a Cab</h2>
            <p>
                1. <strong>Login</strong>: Use your registered email and password to log in.<br>
                2. <strong>Enter Details</strong>: Provide your pickup location, destination, and preferred time.<br>
                3. <strong>Choose a Cab</strong>: Select from available cab options (e.g., Economy, Premium).<br>
                4. <strong>Confirm Booking</strong>: Review your details and click "Book Now".<br>
                5. <strong>Payment</strong>: Pay online or choose cash on delivery if available.
            </p>
        </div>

        <div class="section">
            <h2>Canceling a Booking</h2>
            <p>
                To cancel a booking:<br>
                - Go to "My Bookings" in your account.<br>
                - Select the booking you want to cancel.<br>
                - Click "Cancel Booking" and confirm.<br>
                Note: Cancellation fees may apply based on the time of cancellation.
            </p>
        </div>

        <div class="section">
            <h2>Frequently Asked Questions (FAQs)</h2>
            <p>
                <strong>Q: How do I track my cab?</strong><br>
                A: Once your booking is confirmed, go to "My Bookings" to view the live location of your cab.<br><br>
                <strong>Q: What if my cab is late?</strong><br>
                A: Contact our support team via the "Contact Us" page or call our helpline at +1-800-CAB-HELP.
            </p>
        </div>

        <div class="section">
            <h2>Contact Support</h2>
            <p>
                Need more help? Reach out to us:<br>
                Email: <a href="mailto:support@cabbookingsystem.com">support@cabbookingsystem.com</a><br>
                Phone: +1-800-CAB-HELP<br>
                
            </p>
        </div>

        <p style="text-align: center;">
            <a href="<c:url value='/logout'/>">Logout</a> |
            <a href="<c:url value='/HomeController'/>">Back to Home</a>
        </p>
    </div>
</body>
<%@ include file="/WEB-INF/views/footer.jsp" %>
</html>