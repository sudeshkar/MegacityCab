<html>
<head>
    <link rel="stylesheet" type="text/css" href="Login.css">
</head>
<body>
<div class="logo">
<img alt="cab_image" src="<%= request.getContextPath() %>/images/cab.jpeg">
<h1>The Mega Cab Booking</h1>
</div>

<div class="main">  	
		

			<div class="login">
				<form class="form">
					<label for="chk" aria-hidden="true">Megacity Cab Log in</label>
					<input class="input" type="email" name="email" placeholder="Email" required="">
					<input class="input" type="password" name="pswd" placeholder="Password" required="">
					<button>Log in</button>
				</form>
				<div class="register">
					<p>if you don't have an account </p>
					<button type="button" onclick="window.location.href ='Register';">RegisterNow</button>

					
					</div> 
			</div>

      
</div>


	
</body>

</html>
