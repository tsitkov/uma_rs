var ClientRouter = Backbone.Router.extend({
	routes: {
		"resource": "resourceRequest",
		"*other": "defaultRoute" 		
	},

	"resourceRequest": function(path) {
		$(".table").append(testView.render().el);
	},
	
	"defaultRoute": function() {
		
	}
	
})


/*******************************************************************************************************
 *                                          Models                                                     *
 *******************************************************************************************************/
var ProtectedResource = Backbone.Model.extend({
	
	defaults: {
		path: null,
		authServerUrl: null,
		rpt: null,
		ticket: null,
		authorized: false 
	},

	urlRoot: "/resource1",
		
	query: function(url, headers, success, error) {
		return $.ajax({url: url,
					   headers: headers,
//					   dataType: 'html', 
//					   type: 'get',
//					   contentType: 'application/x-www-form-urlencoded; charset=UTF-8', 
//					   data: decodeURIComponent($.param(this.toJSON())), 
//					   processData: false,
					   success: success, 
					   error: error
		});
	},	
})


var Resource = Backbone.Model.extend({
	defaults: {
		name: null,
		description: null,
		uri: null,
		composite: false,
		action: null
	},
	
	urlRoot: null
	
})

var ResourceCollection = Backbone.Collection.extend({	
	model: Resource,

	initialize: function(models, options) {
		 _.extend(this, _.pick(options, "path"));
	},

	save: function(url) {
		return $.ajax({url: url,
			beforeSend: function(xhrObj){
			    xhrObj.setRequestHeader("Content-Type","application/json");
			    xhrObj.setRequestHeader("Accept","application/json");
//			    xhrObj.setRequestHeader("WWW-Authenticate", "Bearer " + "test_rpt");
			},
			type: 'POST',
			data: JSON.stringify(this.toJSON()), 
			success: null,
			error: null
		});
  },
  
  fetch: function(url) {
	  	return $.ajax({url: url,
		  		   beforeSend: function(xhrObj){
		  			   xhrObj.setRequestHeader("Content-Type","application/json");
		  			   xhrObj.setRequestHeader("Accept","application/json");
		  		   },
		  		   context: this,
		  		   dataType: 'json',
		  		   type: 'GET',
		  		   success: function(data, status, xhr) {
		  			   if (data != null) {
		  				   this.path = "/" + data.path;
		  				   var resources = data.resources;
		  				   if (resources.length) {
		  					   this.add(resources);
		  				   }
		  			   }
		  		   },
		  		   error: null 
	  	});
  }
 
})

/*******************************************************************************************************
 *                                          Views                                                      *
 *******************************************************************************************************/

var ResourceView = Backbone.View.extend({
	tagName: "tr",
	
	initialize: function () {
		this.listenTo(this.model, "destroy", this.remove);
		this.listenTo(this.model, "change:action", this.change);
	},
	
	"events": {
		"click input:checkbox": "click" 
	},
	
	template: _.template($("#resource-template").html()),
	
	render: function() {
		this.$el.html( this.template(this.model.toJSON()));
		return this;
	},
	
	remove: function() {
	      this.$el.remove();
	},

	change: function() {
		if (this.model.get("action") == "delete") {
			this.$el.remove();
		}
	},
	
	click: function() {
		var x = this.$el.find("input:checkbox");
		this.model.set("selected", this.$el.find("input:checkbox").is(":checked"));	
	}
	
})

var NewResourceView = Backbone.View.extend({
	tagName: "div",
//	el: "#addResourceForm",
	
	initialize: function(options) {
		_.extend(this, _.pick(options, "client"));
	},
	
	"events": {
		"click input:checkbox": "click",
		"submit": "submit"
	},
	
	template: _.template($("#new-resource-form-template").html()),

	render: function() {
		this.$el.html(this.template());
		this.client.toggle();
		this.$el.addClass("container")
		return this;
	},
	
	click: function(event) {
		this.$el.find(".form-group").has("#resourceUri").toggle();
	},
	
	submit: function(event) {
		event.preventDefault()
		model = new Resource();
		var resourcePath = $(event.currentTarget).find("#resourcePath").val();
		var isResourceSet = $(event.currentTarget).find("#isResourceSet").is(":checked");
		var resourceUri = null;
		if(!isResourceSet) {
			resourceUri =$(event.currentTarget).find("#resourceUri").val();
		}
		var resourceDescription =$(event.currentTarget).find("#resourceDescription").val();
		var model = new Resource({
			name: resourcePath,
			uri: resourceUri,
			description: resourceDescription,
			composite: isResourceSet,
			action: "new"
		});
		this.client.collection.add(model);
		this.remove();
	},
	
	remove: function() {
		this.$el.remove();
		this.client.toggle();
	}
})

var ResourceCollectionView = Backbone.View.extend({
	tagName: "tbody",

	initialize: function () {
		this.listenTo(this.collection, "add", this.add);
	},
	
	render: function() {
		this.collection.each( function (resource) {
			var resourceView = new ResourceView({model: resource});
			this.$el.append(resourceView.render().el);
		}, this);
		return this;
	},

	add: function(resource) {
		this.$el.append(new ResourceView({model: resource}).render().el);
	},
	
})

var ResourcePageView = Backbone.View.extend({
	tagName: "div",
	
	initialize: function (options) {
		_.extend(this, _.pick(options, "client"));
	},

	events: {
		"click #deleteButton": "delete",
		"click #addResourceButton": "add",
		"click #saveButton": "save"
	},
	
	"delete": function() {
//		var toRemove = this.collection.where({selected: true})
//		this.collection.remove(toRemove);
//		while(toRemove.length > 0) {
//			toRemove.pop().destroy();
//		}
//		toRemove.forEach(function(resource){
//			resource.action="delete";
//		});
//		alert("removing")

		this.collection.each(function(resource){
			if(resource.get("selected")) {
				resource.unset("selected");
				resource.set({"action":"delete"});
			}
		})
	},
	
	"add": function() {
		var form = new NewResourceView({client: this});
		$(document.body).append(form.render().el);		
	},
	
	"save": function() {
//		var url = "/resource/test";
		var url = this.collection.path;
		$.when(this.collection.save(url), this
		).then(function(data, context) {
			alert("You successfully update your resource set");
			context.remove();
		}, function(data,context) {
			alert("Something went wrong");
			context.remove();
		})
	},
	
	template: _.template($("#resource-page-template").html()),
	
	render: function() {
		var tmplParam = {path:this.collection.path};
		this.$el.html(this.template(tmplParam));
		this.client.toggle();
		var resourceCollectionView = new ResourceCollectionView({"collection": this.collection});
		this.$el.find(".table").append(resourceCollectionView.render().el);
		return this;
	},
	
	remove: function() {
		this.$el.remove();
		this.client.toggle();
	},
	
	toggle: function() {
		$(document.body).find(".action-bar").toggle();
		$(document.body).find(".resource-container").toggle();				
	}

})



var DeleteButtonView = Backbone.View.extend ({	
	el: "#deleteButton",
	
	events: {
		"click": "click"
	},
	
	click: function() {
		alert("delete");
		var toRemove = this.collection.where({selected: true})
//		this.collection.remove(toRemove);
//		while(toRemove.length > 0) {
//			toRemove.pop().destroy();
//		}
		toRemove.forEach(function(resource){
			resource.action="delete";
		});
		return this;
	},

})

var AddResourceButtonView = Backbone.View.extend({
	el: "#addResourceButton",
		
	events: {
		"click": "click"
	},
	
	click: function() {
		var form = new NewResourceView({client: this});
		$(document.body).append(form.render().el);
	},
	
	toggle: function() {
		$(document.body).find(".action-bar").toggle();
		$(document.body).find(".resource-container").toggle();		
	}
})

var ResourceLoginView = Backbone.View.extend({
	
	el: "#loginForm",
	
	initialize: function() {
		this.model = new ProtectedResource();
	},
	
	events: {
		"submit": "submit"
	},

	toggle: function() {
//		$(document.body).find(".container").toggle();
		$(document.body).find("#loginView").toggle();	
	},
	
	submit: function(event) {
		event.preventDefault();
		var path = "/" + $(event.currentTarget).find("#inputEmail").val();
		this.model.set("path", path);
		this.fetch(path, true);
		
		//		var resourceSet = this.login(this.model);
		
//		var resourceSet = new ResourceCollection([], {path: path});
//		
//		$.when(resourceSet.fetch(path), this, resourceSet
//		).then(function(data, context, collection) {
//			var resourceSetView = new ResourcePageView({"client":context, "collection":collection});	
//			$(document.body).append(resourceSetView.render().el);
//		}, function(data, context, collection) {
//			// Most likely this is because we are not authenticated yet
//			var issuer = data.getResponseHeader("as_url");
//			var request = {"issuer":issuer, "source":"user_login", "target":path};
//			var query = decodeURIComponent($.param(request));
//			window.location.replace("/openid_connect_login?" + query);
//		});
		
		
//		var resourceSet = myResourceSet;
//		var resourceSetView = new ResourcePageView({"client":this, "collection":resourceSet});	
//		$(document.body).append(resourceSetView.render().el);
		
//		var deleteButtonView = new DeleteButtonView({collection: resourceSet});
//		var addResourceButtonView = new AddResourceButtonView({collection: resourceSet})
	},

	fetch: function(path, authenticate) {
		var resourceSet = new ResourceCollection([], {path: path});
		
		$.when(resourceSet.fetch(path), this, resourceSet
		).then(function(data, context, collection) {
			var resourceSetView = new ResourcePageView({"client":context, "collection":collection});	
			$(document.body).append(resourceSetView.render().el);
		}, function(data, context, collection) {
			if (authenticate) {
				// Most likely this is because we are not authenticated yet
				var issuer = data.getResponseHeader("as_url");
				if (issuer.length > 0) {
					var query = [];
					query.push("issuer=" + encodeURIComponent(issuer));
					var target = "/login" + path;
					query.push("target=" + encodeURIComponent(target));
					query.push("source=" + encodeURIComponent("user_login"));
					window.location.replace("/openid_connect_login?" + query.join("&"));
					
//					var request = {"issuer":issuer, "source":"user_login", "target":target};
//					var query = decodeURIComponent($.param(request));
//					window.location.replace("/openid_connect_login?" + query);

				} else {
					alert("failed to fetch data");
				}
			}
		});		
	}, 

	login: function (context) {
		url = context.get("path")
		var headers = {"WWW-Authenticate": "Bearer " + "test_rpt"};
		$.when(context.query(url, headers), context
		).then(function(data, context) {
			alert("server response1");
			var rpt = "test-rpt";
			context.set("rpt", rpt);
			return context;
		}, function(data,context) {
			alert("first error");
			var headers = {"WWW-Authenticate": "Bearer " + "test_rpt"};
			context.query(url, headers);
		}
		).then(function() {
			
		}, function(data,context){
			allert("second error")
		})
		
		// get data from server
		var result = myResourceSet;
		
		return result;
	},
		
})


/*******************************************************************************************************
 *                                          Globals                                                    *
 *******************************************************************************************************/

var myResourceSet = new ResourceCollection ([
	{name:"test-path1", description:"This is test-desription1mmmmmmmmmmmmmmmmmmmmmmmmmm mmmmmmmmmmmmmmmmmmmmnnnnnnnnnnnnn nnnnnnnnnnnnnnnnnnnnnnnnnnnn nnnnnnnnnnnnnnnnnnnnnnnnnnnnn nnnnnnnnnnnnnnnnnnnnnn" +
			"nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", uri:"/test/uri1"},
	{name:"test-path2", description:"This is test-desription2", uri:"/test/uri2"},
	{name:"test-path3", description:"This is test-desription3", uri:"/test/uri3"},
	{name:"test-path4", description:"This is test-desription4", uri:"/test/uri4"}

])

// var testView = new ResourceCollectionView({collection: myResourceSet});

//var deleteButtonView = new DeleteButtonView({collection: myResourceSet});
//var addResourceButtonView = new AddResourceButtonView({collection: myResourceSet})
//var resourceLoginView = new ResourceLoginView();

//$(".table").append(testView.render().el);

Backbone.history.start({pushState: true})


