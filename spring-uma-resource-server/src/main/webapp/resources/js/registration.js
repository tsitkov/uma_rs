
var RegistrationFormView = Backbone.View.extend({	
	el: "#registrationForm",

	events: {
		"submit": "submit"
	},
	
	submit: function(event) {
		event.preventDefault();
		var userName = $(event.currentTarget).find("#inputEmail").val();
		var issuer = $(event.currentTarget).find("#serverUrl").val();
		var request = {"user_name":userName, "issuer":issuer, "source": "user_registration"};
		var query = decodeURIComponent($.param(request));
		window.location.replace("/openid_connect_login?" + query);

		/*
		$.when(this.register(request)
		).then(function() {
			alert("success");
		}, function() {
			alert("failure");
		})

*/		
	},
	
/*
	register: function(request) {
		return $.ajax({url: "/openid_connect_login",
			   type: 'OPTION',
			   contentType: 'application/x-www-form-urlencoded; charset=UTF-8', 
			   data: decodeURIComponent($.param(request)), 
		});
	},
*/		

})

/*******************************************************************************************************
 *                                          Globals                                                    *
 *******************************************************************************************************/

var registrationFormView = new RegistrationFormView();