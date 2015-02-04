package org.mit.uma.rcserver.views;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.mit.uma.rcserver.server.RestfulServer;
import org.mit.uma.rcserver.services.DataService;
//import org.mit.uma.rcserver.defaultimpl.models.DefaultAuthServerDetails;
import org.springframework.security.access.prepost.PreAuthorize;

//import org.mit.uma.rcserver.defaultimpl.services.DefaultDataService;
//import org.mit.uma.rcserver.json.AuthServerApiJson;
//import org.mit.uma.rcserver.json.RegistrationResponse;

import org.mit.uma.rcserver.server.HttpServer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.mit.uma.rcserver.json.JsonUtil;
import org.mit.uma.rcserver.json.ResourceDetailsJson;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;
//import org.mit.uma.rcserver.AuthorizationRequest;
import org.mit.uma.rcserver.services.AuthRegistrationService;
import org.mit.uma.rcserver.services.AuthorizationService;
import org.mit.uma.rcserver.services.ResourceAccessService;
import org.mit.uma.rcserver.services.UserDetailsService;
import org.mit.uma.rcserver.security.RSAuthenticationToken;

@Controller
public class SpringHttpServer {
	 
	@Autowired
	private AuthRegistrationService registrationSrv;
	
	@Autowired
	private AuthorizationService authorizationSrv;

	@Autowired
	private DataService dataService;			

	@Autowired
	private UserDetailsService userDetailsSrv;

	@Autowired
	ResourceAccessService resourceAccessService;
	
	static final Logger logger = Logger.getLogger(SpringHttpServer.class);  

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String getRegistrationData() {
		return "registration";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// authorizationSrv.processRegistrationRequest(request, response);
		return new ModelAndView("/login");
	}
	
	@RequestMapping(value="/login/**", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model) {
		String path = request.getServletPath();
		assert(path.startsWith("/login"));
		
		String resourcePath = path.replaceFirst("/login[/]?", "");
		if (resourcePath.startsWith("resource/")) {
			System.out.println("settin path " + resourcePath);
			model.addAttribute("path", resourcePath);
		}
		return "login";
	}
	
//	@RequestMapping("/resource")
//	@PreAuthorize("hasRole('ROLE_USER')")
//	public String resource() {
//		// TODO Auto-generated method stub
//		return "resource";
//	}
	
//	@RequestMapping(value="/resource1", produces="application/json")
//	@RequestMapping(value="/resource1", consumes="application/json", produces="application/json")
//	public @ResponseBody String resource1(@RequestParam(value="path") String path) {
//	public @ResponseBody String resource1(@RequestBody String request) throws IOException {
//		// TODO Auto-generated method stub
//		// String path = request.getParameter("path");
//		
//	
//		System.out.println("**" + path);
//		
//		String response1 = "{" + 
//							"\"path\": \"/test/usr\"," +
//							"\"test\": \"mytest\"" +
//						  "}";	
//		return response1;
//	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/resource/**", method = RequestMethod.GET)
	public @ResponseBody String getResourceSetContent(HttpServletRequest request) {
		String path = request.getServletPath().substring(1);
		ResourceSet resourceSet = dataService.getResourceSetByPath(path);
	        
	        System.out.println("***" + resourceSet.getResourceDescription());
	        
	        String result = "";
	        if (resourceSet != null) {
	        	 result = resourceSet.toJson();
	        	 System.out.println("json");
	        	 System.out.println(result);
	        }
	        
//			String uri = request.getRequestURI();
//	        System.out.println("unmapped: " + uri);
//	        System.out.println("path: " + path);

	        return result;
	 }
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/resource/**", method = RequestMethod.POST, consumes="application/json")
	public String updateResourceSetContent(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response) {

		String path = request.getServletPath().substring(1);
		
		System.out.println("**** " + path);
		
		try {
			Set<ResourceDetailsJson> updates = JsonUtil.fromJsonArray(jsonData, ResourceDetailsJson.class);
	        ResourceSet resourceSet = dataService.getResourceSetByPath(path);
			UserDetails user = userDetailsSrv.getUserDetailsById(resourceSet.getOwnerId());
			
			
//		        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		        UserDetails user = null;
//		        if (authentication instanceof RSAuthenticationToken) {
//					RSAuthenticationToken token = (RSAuthenticationToken) authentication;
//					user = userDetailsSrv.getUserDetails(token.getUserId());
//				}

	        Map<String,String> errors = new HashMap<String,String>();
			Map<String, ? extends ResourceDetails> resources = resourceSet.getResources();
			for (ResourceDetailsJson resource : updates) {
	        	System.out.println(JsonUtil.toString(resource));
	        	
				
				String action = resource.getAction();
	        	if(StringUtils.isEmpty(action)) {
	        		continue;
	        	}

	        	String resourcePath = path + "/" + resource.getResourceName();

	        	if (action.equals("new")) {
	        		if (resources.containsKey(resourcePath)) {
	        			errors.put(path, "Attemp to create exsisting resource at " + resourcePath);
	        		} else {
	        			if (resource.isResourceSet()) {
	        				ResourceSet rs = resourceSet.addResourceSet(resource);
	        				
	        				System.out.println(rs.toJson());
	        				System.out.println(rs.getResourceName());
	        							
	        				dataService.saveResource(rs);

	        				System.out.println("id:" + rs.getResourceId());

	        				
	        				// protect new resource set
	        				resourceAccessService.protect(rs, user);
	        			} else {
	        				resourceSet.addResource(resource);
	        			}
	        		}
	        	} else if (action.equals("delete")) {
	        		// should we inform authorization server?
	        		ResourceDetails toRemove = resources.get(resourcePath);
	        		
	        		System.out.println("resourcePath " + resourcePath);
	        		System.out.println("* " + toRemove);
	        		
	        		if (toRemove != null) {
	        			dataService.removeResource(toRemove);
	        		} else {
	        			errors.put(resourcePath, "Attemp to delete non-exsisting resource at " + resourcePath);
	        		}
	        	} else if(action.equals("edit")) {
	        		ResourceDetails toUpdate = resources.get(resourcePath);
	        		if (toUpdate != null) {
	        			resourceSet.replaceResource(resource);
	        		} else {
	        			errors.put(resourcePath, "Attemp to update non-exsisting resource at " + resourcePath);		        			
	        		}
	        		// update existing resource
	        	} else {
        			errors.put(resourcePath, "Attempt to apply unknown action " + action + "to resource at " + resourcePath);
	        	}
	        }				
	        dataService.saveResource(resourceSet);
	        if (errors.size() > 0) {
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return JsonUtil.toString(errors);
	        }
			response.setStatus(HttpServletResponse.SC_OK);

		} catch (IOException e) {
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		}
        return null;
	}	
	
	@RequestMapping("/")
	public ModelAndView index( Model model) {
		return new ModelAndView("/login");
	}
	
	@RequestMapping("/root_resource_set")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String openResourceSet(Model model, Principal p) {
		System.out.println("login name: " + p.getName());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof RSAuthenticationToken) {
			System.out.println("True");
		}
		
		return "resource";
	}
	
//	@RequestMapping("/login")
//	public String login( Model model) {
//		return "login";
//	}
//	
//	@RequestMapping("/user_registration")
//	public String userRegister( Model model) {
//		return "user-registration";
//	}
	
//	// Registering resource server with authorization one
//	@RequestMapping("/register1")
//	public String hello(@RequestParam(value="as_url", required=true) String authServerUrl,
//						@RequestParam(value="userId", required=true) String userId,
//																	 Model model) throws JsonProcessingException {
//		UserDetails user = userDetailsSrv.createUserDetails(userId, authServerUrl);
//		AuthServerDetails authServer = user.getAuthServer();		
//		assert(authServer.getAuthServerId() != null);
//
//		model.addAttribute("redirect_uri", authServer.getRedirectUris());
//		model.addAttribute("response_type","code");
//		model.addAttribute("scope",authServer.getScope());
//		model.addAttribute("as_uri", "http://192.168.1.9:8080/uma-server/authorize");
//		
//		return "register";
//	 }

//	// This has to come from authorization server, gives us a code needed to get pat
//	@RequestMapping("/authz")
//	public String authorize(@RequestParam(value="code", required=true) String clientCode,
//			Model model) throws JsonProcessingException {
//			System.out.print("Client code: " + clientCode + "\n");
//			model.addAttribute("client_code", clientCode);
//			return "pat";
//	}
	
//	@RequestMapping("/authorize")
//	public String authorize(@RequestParam(value="client_id", required=true) String clientId,
//							@RequestParam(value="client_code", required=true) String clientCode,	
//							Model model) throws JsonProcessingException {
//			
//			logger.info("Entering authorization service");  				
//			return "authorize";
//	}


	
}
