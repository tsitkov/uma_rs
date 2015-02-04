<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<script id="resource-page-template" type="text/template">
  	<div class="action-bar">
    	<div class="btn-toolbar" role="toolbar" aria-label="...">
    		<div class="btn-group" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" id="addResourceButton">New</button>
  				<button type="button" class="btn btn-default" id="importButton">Import</button>  		
  			</div>
    		<div class="btn-group" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" id="deleteButton">Delete</button>  	
  			</div>
    		<div class="btn-group" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" id="saveButton">Save</button>  	
  			</div>
  		</div>
  	</div>

 	<div class="container">
 		<div class="resource-container">
			<table class="table">	
				<caption>
					<p class="lead"><\%= path%></p>				
				</caption>
    		</table>
  		</div>
  	</div>
</script>


<script id="resource-template" type="text/template">
	<td>
		<div class="checkbox">
			<label>
      			<input type="checkbox" />
			</label>
  		</div>
	</td>
	<td>
		<div class="media">
			<a class="media-left" href="<\%= uri %>">
      			<img src="images/txt.png" alt="txt type" />
    		</a>
    		<div class="media-body">
				<h4 class="media-heading"><\%=name %></h4>
				<p><\%=description%></p>
			</div>
		</div>
	</td>
</script>

<script id="new-resource-form-template" type="text/template">
    <div class="starter-template">
       	<h1>Add New Resource</h1>
       	<p class="lead">In order to register a new resource please provide requested information .</p>
     	</div>

	<div class="well">	
		<form class="form-horizontal" id="addResourceForm">
		  <div class="form-group">
		    <label for="inputResourcePath" class="col-sm-2 control-label">Resource Path</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="resourcePath" placeholder="Resource Path">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="inputResourceUri" class="col-sm-2 control-label">Resource URI</label>
		    <div class="col-sm-10">
		      <input type="text" class="form-control" id="resourceUri" placeholder="Resource URI">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="resourceDescription" class="col-sm-2 control-label">Resource Description</label>
		    <div class="col-sm-10">
			  <textarea class="form-control" id="resourceDescription" placeholder="Resource Description" rows="3"></textarea>
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <div class="checkbox">
		        <label>
		          <input type="checkbox" id="isResourceSet"> Resource Set
		        </label>
		      </div>
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-5 col-sm-2">
		      <button type="submit" class="btn btn-primary btn-block"> Add Resource </button>
		    </div>
		  </div>
		</form>
	</div>
</script>
