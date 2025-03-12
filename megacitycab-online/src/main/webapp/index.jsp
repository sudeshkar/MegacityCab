<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/Login.css">
</head>
<body>
<div class="logo">
<img alt="cab_image" src="<%= request.getContextPath() %>/images/cab.jpeg">
<h1>The Mega Cab Booking</h1>
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
    session.removeAttribute("loginMessage"); 
    session.removeAttribute("messageType");
}
%>
<button type="button" onclick="window.location.href ='AboutUs.jsp';">About Us</button>
</div>







<div class="main">  	
		

			<div class="login">
				<form class="form" action="<%= request.getContextPath() %>/LoginController" method="post">
					<label for="chk" aria-hidden="true">Megacity Cab Log in</label>
					<input class="input" type="email" name="email" placeholder="Email" required="" >
					<input class="input" type="password" name="pswd" placeholder="Password" required="">
					<button type="submit">Log in</button>
				</form>
				
				<div class="register">
					<form action="<%= request.getContextPath() %>/RegisterController" method="get">
					<p>if you don't have an account </p>
					<button type="submit" >RegisterNow</button>
					</form>
					
					</div> 
			</div>

      
</div>


	
</body>

</html>
