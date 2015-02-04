/*
 * login to resource server is equivalent of getting access to root resource set of given user with super privileges
 */

$( document ).ready(function() {
	var resourceLoginView = new ResourceLoginView();
	var path = "/" + resourceLoginView.$el.find("#inputEmail").val();
	if (path.length > 1) {
		resourceLoginView.fetch(path, authenticate=false);
	}
});
