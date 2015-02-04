<%@ attribute name="js" required="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <!--
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    -->
    <script src="js/3rd-party/underscore-min.js"></script>
    <script src="js/3rd-party/jquery-1.11.2.min.js"></script>
    <script src="js/3rd-party/backbone-min.js"></script>
    <script src="css/bootstrap-3.3.1/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
	-->
    <script>
		$(document).ready(
			function() {
				var titleId = "#" + $(document).find("title").text();
    			highlightCurrent(titleId)
		});

    	function highlightCurrent(current) {
			$(current).addClass("active");
		}
    </script>
    