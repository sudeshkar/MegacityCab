<%@page import="com.megacitycab.model.User"%>
<%@ page import="com.megacitycab.utils.SessionUtils" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/Login.css">
</head>
<body>
<%
        User loggedInUser = SessionUtils.getLoggedInUser(request);
        if (loggedInUser != null) {
             
            response.sendRedirect(request.getContextPath() + "/HomeController");
            return;
        }
    %>
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
<%
String errorMessage = (String) session.getAttribute("errorMessage");
 
if (errorMessage != null) {
%>
<div class="notification <%= messageType %>">
    <%= errorMessage %>
</div>
<script>
    setTimeout(function() {
        document.getElementById("notification").style.display = "none";
    }, 3000);  
</script>
<%
    session.removeAttribute("errorMessage"); 
    session.removeAttribute("messageType"); 
}
%>
<a href="<%= request.getContextPath() %>/AboutUs.jsp" style="text-decoration: none;">
    <button>About Us</button>
</a>
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
