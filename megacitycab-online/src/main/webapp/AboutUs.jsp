<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>About Us - MegaCity Cabs</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: black;
            margin: 0;
            padding: 0;
        }
        button{
        margin: auto;
        
        }
        .container {
            max-width: 1100px;
            margin: auto;
            padding: 20px;
            text-align: center;
        }
        .about-section {
            background: white;
            color: black;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .about-section h1 {
            font-size: 32px;
            color: #333;
        }
        .about-section p {
            font-size: 18px;
            color: #555;
            line-height: 1.6;
        }
        .about-section img {
            max-width: 100%;
            height: auto;
            border-radius: 10px;
            margin-top: 20px;
        }
        .team-section {
            margin-top: 40px;
        }
        .team-member {
            display: inline-block;
            width: 30%;
            margin: 10px;
            text-align: center;
        }
        .team-member img {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            margin-bottom: 10px;
        }
        .team-member h3 {
            font-size: 18px;
        }
        
    </style>
</head>
<body>
<div class="container">
    <div class="about-section">
        <h1>About MegaCity Cabs</h1>
        <p>Welcome to MegaCity Cabs! We are dedicated to providing fast, reliable, and affordable transportation services in the city. Our goal is to make your travel experience smooth, safe, and hassle-free.</p>
        <img src="<%= request.getContextPath()%>/images/M1.jpg" alt="MegaCity Cabs" width="400px">
    </div>

    <div class="about-section">
        <h2>Our Mission</h2>
        <p>To revolutionize urban transportation by offering a seamless and comfortable cab booking experience, ensuring punctuality and customer satisfaction.</p>
    </div>

    <div class="about-section">
        <h2>Why Choose Us?</h2>
        <ul style="text-align: left; display: inline-block;">
            <li>✔ Reliable and on-time pickups</li>
            <li>✔ Professional and experienced drivers</li>
            <li>✔ Affordable rates with transparent pricing</li>
            <li>✔ 24/7 customer support</li>
            <li>✔ Easy online booking</li>
        </ul>
    </div>

    <div class="team-section">
        <h2>Meet Our Team</h2>
        <div class="team-member">
            <h3>SathiesKumar Sudeshkar- <br> CEO</h3>
        </div>
        <div class="team-member">
            
            <h3>SathiesKumar Sudeshkar - Operations Manager</h3>
        </div>
        <div class="team-member">
           
            <h3>SathiesKumar Sudeshkar - Customer Support Lead</h3>
        </div>
    </div>
    <a href="<%= request.getContextPath() %>/index.jsp">
    <button>Back to Home</button>
</a>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>