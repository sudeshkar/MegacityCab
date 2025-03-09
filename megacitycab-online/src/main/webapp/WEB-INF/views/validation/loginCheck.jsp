<%@page import="com.megacitycab.model.User"%>
<%@page import="com.megacitycab.utils.SessionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
if (!SessionUtils.isUserLoggedIn(request)) {
	response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}
else{
	User user= SessionUtils.getLoggedInUser(request);
	if(user==null){
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}
}



%>

</body>
</html>