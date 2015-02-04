<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
  <head>
	<o:header title="register-page">
		<script type="text/javascript">
		</script>
	</o:header>
  </head>	
  <body>

	<o:topbar/>

    <div class="container">

      <div class="starter-template">
        <h1>Registration Page Template</h1>
        <p class="lead">Some kind of image should be placed here.</p>
      </div>
 
      <!-- Sign in form -->
      
      <form class="form-signin well" id=registrationForm>
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="text" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
        
        <label for="authorizationServer" class="sr-only">URL</label>
        <input type="url" id="serverUrl" class="form-control" placeholder="Authorization Server URL" required>
        
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>
	</div>

	<o:footer/>

	<!--  custom scripts -->
	<script type="text/javascript" src="js/registration.js"></script>

  </body>
</html>