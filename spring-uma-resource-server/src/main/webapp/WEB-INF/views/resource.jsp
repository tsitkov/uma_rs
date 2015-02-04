<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html lang="en">
  <head>
	<o:header title="resource-page">
		<script type="text/javascript">
		</script>
	</o:header>
  </head>	
  <body>

	<o:topbar/>

  	<div class="action-bar">
    	<div class="btn-toolbar" role="toolbar" aria-label="...">
    		<div class="btn-group" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" id="addResourceButton">New</button>
  				<button type="button" class="btn btn-default" id="importButton">Import</button>  		
  			</div>
    		<div class="btn-group" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" id="deleteButton">Delete</button>  	
  			</div>
  		</div>
  	</div>

 	<div class="container">
 		<div class="resource-container">
			<table class="table">	
    		</table>
  		</div>
  	</div>
	
  	<o:footer/>
  	
  	<o:resource-entry-template/>
  	
  	<!-- Custom scripts -->
  	<script type="text/javascript" src="js/client.js"></script>
  		 	
	
  </body>
</html>