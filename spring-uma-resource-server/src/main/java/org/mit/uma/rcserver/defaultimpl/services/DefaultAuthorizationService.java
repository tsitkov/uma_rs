package org.mit.uma.rcserver.defaultimpl.services;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.mit.uma.rcserver.client.HttpClient;
import org.mit.uma.rcserver.config.Configurator;
import org.mit.uma.rcserver.json.PatResponse;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.services.AuthorizationService;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.services.UserDetailsService;


@Service
public class DefaultAuthorizationService implements AuthorizationService {

	@Autowired
	private Configurator config;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	DataService dataService;

	
	@Autowired
	HttpClient httpClient;
	
	static final Logger logger = Logger.getLogger(DefaultAuthorizationService.class);  
	
	@Override
	public void processRegistrationRequest(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		//TO DO: what if two users request registration with the same user Id simultaneously
		// we need some kind of lock here
		
		// request contains two attributes: user_name and issuer
		String userName = request.getParameter("user_name");
		assert(!StringUtils.isEmpty(userName));
		
		String issuer = request.getParameter("issuer");
		assert(!StringUtils.isEmpty(issuer));
		// first check if submitted user name is available
		UserDetails user = null;
		try {
			user = userDetailsService.createUserDetails(userName, issuer);
		} catch( IllegalStateException exeption) {
			throw new AuthenticationServiceException("user with user name " + userName +" is already registered");
			// user with this name is already registered
		}
		// the next step is to request authorization code
		Map<String, String> query = buildAuthorizationQuery(user.getAuthServer());
		// we need to deal with nonce and state attributes
		HttpSession session = request.getSession();	
		logger.debug("Session id=" + session.getId());

		session.setAttribute("nonce", query.get("nonce"));
		session.setAttribute("state", query.get("state"));
		session.setAttribute("issuer", user.getAuthServer().getIssuer());
		session.setAttribute("user_name", user.getUserId());

		System.out.println("Setting session: ");
	
		// build actual query
		try {
			assert(user.getAuthServer() != null);
			System.out.print("auth endporint" + user.getAuthServer().getAuthorizationEndpoint() + "\n");
			
			URIBuilder uriBuilder = new URIBuilder(user.getAuthServer().getAuthorizationEndpoint());
			for (Map.Entry<String, String> entry : query.entrySet()) {
				uriBuilder.addParameter(entry.getKey(), entry.getValue());
			}
			// send query to authorization server
			String codeRequest = uriBuilder.build().toString();
			System.out.print("code request\n");
			System.out.print(codeRequest + "\n");
			response.sendRedirect(codeRequest);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Failed to build request");
		}
		
	}

	@Override
	public void processLoginRequest(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		// request must contain 3 attributes: 
		// 		source="user-login"
		//		issuer="https://auth.com"
		//      target: path to resource
		String source = request.getParameter("path");
		assert(!StringUtils.isEmpty(source) && source.equals("user-login"));

		String resourcePath = request.getParameter("target");
		assert(!StringUtils.isEmpty(resourcePath));
		
		System.out.println("target " + resourcePath);
		

		String issuer = request.getParameter("issuer");
		assert(!StringUtils.isEmpty(issuer));
		
		AuthServerDetails authServer = dataService.getAuthServerDetails(issuer);

		// build query for AAT
		// the next step is to request authorization code
		Map<String, String> query = buildAuthorizationQuery(authServer);
		// we need to deal with nonce and state attributes
		HttpSession session = request.getSession();		
		logger.debug("Session id=" + session.getId());

		session.setAttribute("nonce", query.get("nonce"));
		session.setAttribute("state", query.get("state"));
		session.setAttribute("issuer", issuer);
		session.setAttribute("target", resourcePath);

		try {
			URIBuilder uriBuilder = new URIBuilder(authServer.getAuthorizationEndpoint());
			for (Map.Entry<String, String> entry : query.entrySet()) {
				uriBuilder.addParameter(entry.getKey(), entry.getValue());
			}
			// send query to authorization server
			String codeRequest = uriBuilder.build().toString();
			response.sendRedirect(codeRequest);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Failed to build request");
		}	
	}

	@Override
	public PatResponse processPatRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	
		System.out.println("processPatRequest");
		
		String code = request.getParameter("code");
		assert(!StringUtils.isEmpty(code));
		String state = request.getParameter("state");

		HttpSession session = request.getSession(false);
		assert(session != null);

		System.out.println("***" + session);
		System.out.println("Session id=" + session.getId());
		
//		Enumeration<String> keys = session.getAttributeNames();
//		while (keys.hasMoreElements())
//		{
//		  String key = (String)keys.nextElement();
//		  System.out.println(key + ": " + session.getAttribute(key));
//		}

	
		Object o = session.getAttribute("state");
		
		System.out.print("code: " + code + "\n");
		System.out.print("state: " + o.toString() + "\n");
				
		if (StringUtils.isEmpty(o) || !state.equals(o.toString())) {
			throw new AuthenticationServiceException("State parameter mismatch on return. Expected " + o.toString() + " got " + state);			
		}
		
		Object issuer =  session.getAttribute("issuer");
		if(StringUtils.isEmpty(issuer)) {
			throw new AuthenticationServiceException("'issuer' attribute is not found");			
		}
		
		
		
		AuthServerDetails authServer = dataService.getAuthServerDetails(issuer.toString());
		System.out.println("issuer: " + issuer);
		System.out.println("secret: " + authServer.getClientSecret());
		
//		Object userName = session.getAttribute("user_name");
//		if (StringUtils.isEmpty(userName)) {
//			throw new AuthenticationServiceException("Session does have 'user_name' attibute");						
//		}

//		// pull out user details data
//		UserDetails user = userDetailsService.getUserDetails(userName.toString());
		// Now we are ready to construct pat request
		PatResponse pat = this.getPatToken(authServer, code);
		// Check expiration time stamp
    	Calendar calendar = Calendar.getInstance();
    	Date now = calendar.getTime();
    
    	if (now.after(pat.getTokenExpiresAt())) {
			throw new AuthenticationServiceException("Obtained expired PAT");						    		
    	}
//		user.setAccessToken(pat.getAccessToken());
//		user.setRefreshToken(pat.getRefreshToken());
//		
//		return user;
    	return pat;
	}
	
	String processRatRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String code = request.getParameter("code");
		assert(!StringUtils.isEmpty(code));
		String state = request.getParameter("state");

		HttpSession session = request.getSession(false);
		assert(session != null);
		
		System.out.println("***" + session);
		System.out.println("Session id=" + session.getId());

		Object o = session.getAttribute("state");
		
		System.out.print("code: " + code + "\n");
		System.out.print("state: " + o.toString() + "\n");
				
		if (StringUtils.isEmpty(o) || !state.equals(o.toString())) {
			throw new AuthenticationServiceException("State parameter mismatch on return. Expected " + o.toString() + " got " + state);			
		}
		
		Object userName = session.getAttribute("user_name");
		if (StringUtils.isEmpty(userName)) {
			throw new AuthenticationServiceException("Session does have 'user_name' attibute");						
		}
		// pull out user details data
		UserDetails user = userDetailsService.getUserDetails(userName.toString());
		// Now we are ready to construct pat request
		PatResponse pat = this.getPatToken(user.getAuthServer(), code);
		// Check expiration time stamp
    	Calendar calendar = Calendar.getInstance();
    	Date now = calendar.getTime();
    
    	if (now.after(pat.getTokenExpiresAt())) {
			throw new AuthenticationServiceException("Obtained expired PAT");						    		
    	}
		user.setAccessToken(pat.getAccessToken());
		user.setRefreshToken(pat.getRefreshToken());

		
		return null;
	}
	
//	protected Map<String, String> buildAuthorizationQuery(UserDetails user) {
	protected Map<String, String> buildAuthorizationQuery(AuthServerDetails authServer) {
		
		// This is url path where autorization code will be sent to. Must be in authorization server configuration.
		String redirectUri = config.getThisServerUrl() + config.getDefaultRedirectUrl();
		// this comes back in the id token
		String nonce = randomString();
		// this comes back in the auth request return
		String state = randomString();
//		String clientId = user.getAuthServer().getClientId();
		String clientId = authServer.getClientId();
		String scope = authServer.getScope();
//		String scope = user.getAuthServer().getScope();
		
		Map<String, String> query = new HashMap<String, String>();
		query.put("response_type", "code");
		query.put("client_id", clientId);
		query.put("scope", scope);			
		query.put("redirect_uri", redirectUri);
		query.put("nonce", nonce);
		query.put("state", state);

		return query;
	}

	public PatResponse getPatToken(AuthServerDetails authServer, String code) {

		assert(authServer != null);
		
		Logger.getRootLogger().setLevel(Level.DEBUG);
		logger.debug("I am in getPatToken");
		
        String tokenEndpoint = authServer.getTokenEndpoint();
		
        Map<String,String> request = new HashMap<String,String>();
		request.put("grant_type", "authorization_code");
        request.put("code", code);
        String redirectUri = config.getThisServerUrl() + config.getDefaultRedirectUrl();
        request.put("redirect_uri", redirectUri);
        request.put("credentials", authServer.getClientId() + ":" + authServer.getClientSecret());        
		PatResponse response = httpClient.patRequest(tokenEndpoint, request);
		return response;
		
	}
			
	private static String randomString() {
		String nonce = new BigInteger(50, new SecureRandom()).toString(16);

		return nonce;
	}

	@Override
	public void processResourceRequest(UserDetails user, String path, Set<String> scopes, String rpt) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(rpt)) {
			String issuer = user.getAuthServer().getIssuer();
		}
	}
	
//	private String _base64Encode(String msg) {
//		byte[] encodedAuth = Base64.encodeBase64(msg.getBytes());
//		return new String(encodedAuth);
//	}
	
}
