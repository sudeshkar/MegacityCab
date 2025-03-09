<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Page</title>
<link rel="stylesheet" href="Register.css" />
</head>
<body>
<div class="container">
        <h2>User Registration</h2>
        <form action="<c:url value='/RegisterController' />" method="POST">
            <div class="form-group">
                <label>User Type<span class="required">*</span></label>
                <div class="radio-group">
                    <input type="radio" id="customer" name="userType" value="customer" required>
                    <label for="customer">Customer</label>
                    <input type="radio" id="driver" name="userType" value="driver">
                    <label for="driver">Driver</label>
                </div>
            </div>

            <div class="form-group">
                <label for="username">UserName<span class="required">*</span></label>
                <input type="text" id="username" name="username" required>
            </div>

            <div class="form-group">
                <label for="password">Password<span class="required">*</span></label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="form-group">
                <label for="email">Email<span class="required">*</span></label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="phone">Phone Number<span class="required">*</span></label>
                <input type="tel" id="phone" name="phone" required>
            </div>

            
            <div id="driverFields" style="display: none;">
                <div class="form-group">
                    <label for="license">License Number<span class="required">*</span></label>
                    <input type="text" id="license" name="license">
                </div>

                <div class="form-group">
                    <label for="vehicleType">Vehicle Type<span class="required">*</span></label>
                    <select id="vehicleType" name="vehicleType">
                        <option value="">Select Vehicle Type</option>
                        <option value="car">MINIVAN</option>
                        <option value="bike">SEDAN</option>
                        <option value="van">SUV</option>
                        <option value="van">LUXURY</option>
                        <option value="van">VAN</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="experience">Address<span class="required">*</span></label>
                    <input type="text" id="address" name="address">
                </div>
            </div>

            
            <div id="customerFields" style="display: none;">
                <div class="form-group">
                    <label for="address">Address<span class="required">*</span></label>
                    <input type="text" id="address" name="address">
                </div>

            </div>

            <button type="submit" class="submit-btn">Register</button>
        </form>
        <form action="<%= request.getContextPath() %>/LoginController" method="get">
        <p>if you have an account?</p>
        <button type="submit" action="Clogin" >Login</button>
        </form>
    </div>

    <script>
        // Show/hide fields based on user type selection
        document.querySelectorAll('input[name="userType"]').forEach(radio => {
            radio.addEventListener('change', function() {
                document.getElementById('driverFields').style.display = 
                    this.value === 'driver' ? 'block' : 'none';
                document.getElementById('customerFields').style.display = 
                    this.value === 'customer' ? 'block' : 'none';
            });
        });
    </script>

</body>
</html>