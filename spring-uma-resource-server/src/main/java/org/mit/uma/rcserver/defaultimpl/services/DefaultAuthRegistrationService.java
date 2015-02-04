package org.mit.uma.rcserver.defaultimpl.services;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.mit.uma.rcserver.client.HttpClient;
import org.mit.uma.rcserver.config.Configurator;
import org.mit.uma.rcserver.models.AuthServerApi;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.json.JsonUtil;
import org.mit.uma.rcserver.json.RegistrationRequest;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.services.AuthRegistrationService;

@Service
public class DefaultAuthRegistrationService implements AuthRegistrationService {

	@Autowired
	private Configurator config;
	
	@Autowired
	private DataService dataSrv;
	
	@Autowired
	HttpClient httpClient;

	static final Logger logger = Logger.getLogger(DefaultAuthRegistrationService.class);  

	@Override
	public AuthServerRegistrationDetails register(AuthServerApi api) {

		logger.setLevel(Level.DEBUG);
		/*
		 * Registration is conducted into two steps:
		 * The first step is authorization server API discovery
		 */
		logger.debug("Getting authorization server API");

		if (api == null || api.getRegistrationEndpoint() == null) {
			throw new IllegalStateException("Failed to create registration request: registration endpoint is not set.");
		}			
		/*
		 * The second step is to conduct the very first handshake with
		 * an authorization server.
		 */
	    String registrationEndpoint = api.getRegistrationEndpoint();	    
	    RegistrationRequest request = _createRegistrationRequest();

	    // log registration request
		logger.debug("requestAsString :" + JsonUtil.toString(request) + "\n");
		AuthServerRegistrationDetails response = httpClient.registrationRequest(registrationEndpoint, request);
		
		return response;
	
	}
	
	@Override
	public AuthServerApi getAuthServerAPIs(String authServerUrl) {
		/*
		 * This is very simple RPC call and it only requires URI of authorization
		 * server and API end-point.
		 */
		String authServerApiEndPoint = config.getAuthServerApiEndpoint();
		String apiEndpoint = authServerUrl + authServerApiEndPoint;
		
		logger.debug("API Url " + apiEndpoint + "\n");
		AuthServerApi api = httpClient.apiRequest(apiEndpoint);
				
		if (api == null) {
			// we failed and need to throw an exception of some kind
			throw new IllegalStateException("Failed to obtain authorization server API: API endpoint: " + apiEndpoint);
		}
		return api;
		
	}
	
	private  RegistrationRequest _createRegistrationRequest() {
		RegistrationRequest request = new RegistrationRequest();
		String clientName = config.getThisServerName();
		request.setClientName(clientName);
		// set request scope
		ArrayList<String> scopes = new ArrayList<String>();
		for (ResourceSet.DefaultScopes scope : ResourceSet.DefaultScopes.values()) {
			scopes.add(scope.toString().toLowerCase());
		}
		//scope.add("docs.kantarainitiative.org/uma/scopes/prot.json");		
		request.setScope(scopes);
		String clientUri = config.getThisServerUrl();
		// set redirecURIs
		ArrayList<String> redirectUris = new ArrayList<String>();
		redirectUris.add(clientUri + config.getDefaultRedirectUrl());
		request.setRedirectUris(redirectUris);
		// set clientURI
		request.setClientUri(clientUri);
		
		return request;		
	}
}
