<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
  <head>
	<o:header title="login-page">
		<script type="text/javascript">
		</script>
	</o:header>
  </head>	
  <body>

	<o:topbar/>

    <div class="container" id="loginView">

      <div class="starter-template">
        <h1>Login Page Template</h1>
        <p class="lead">Some kind of image should be placed here.</p>
      </div>
 
      <!-- Sign in form -->
      
      <form class="form-signin well" id="loginForm">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
		<security:authorize access="isAuthenticated()">
        	<input type="text" id="inputEmail" class="form-control" placeholder="Resource path" value="${path}" required autofocus>
		</security:authorize>
		<security:authorize access="isAnonymous()">
        	<input type="text" id="inputEmail" class="form-control" placeholder="Resource path" required autofocus>
		</security:authorize> 
        <!--
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
        -->
        
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>
      
      <div>
        <p class="text-center"> <br> First time user? <a href="/register" > Register with resource server </a></p>
      </div>
    </div>

	<o:footer/>
	
	<o:resource-entry-template/>
	
	<!--  custom scripts -->
	<script type="text/javascript" src="js/client.js"></script>
	<script type="text/javascript" src="js/login.js"></script>	
  </body>
</html>