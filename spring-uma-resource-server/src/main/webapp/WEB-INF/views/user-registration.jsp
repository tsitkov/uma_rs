<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
 		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	</head>
 	<body>

	<p> Registration form </p> 		
 		<div>
 			<form action="/openid_connect_login" method="get">
   				<div>
   					User name: <input type="text" name="user_name">
				</div>
				<div>
					Authorization Server URL: <input type="text" name="issuer">
   				</div>
   				 <input type="hidden" name="source" value="user_registration">
				<div>
   					<input type="submit" value="Register">		
				</div>
 			</form>
 		</div>


 	</body>
</html>




