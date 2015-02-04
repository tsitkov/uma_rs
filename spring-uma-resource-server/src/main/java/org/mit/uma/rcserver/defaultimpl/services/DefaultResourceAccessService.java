package org.mit.uma.rcserver.defaultimpl.services;

import java.util.HashMap;
import java.util.Map;

import org.mit.uma.rcserver.client.HttpClient;
import org.mit.uma.rcserver.json.ResourcePermissionsResponse;
import org.mit.uma.rcserver.json.ResourceRegistrationResponse;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.services.ResourceAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
public class DefaultResourceAccessService implements ResourceAccessService {

	@Autowired
	HttpClient httpClient;
	
	@Override
	public ResourceSet protect(ResourceSet resourceSet, UserDetails user) {
		// protect resource here
		Map<String,String> request = new HashMap<String,String>();
		request.put("name", resourceSet.getResourceName());
		request.put("scopes", resourceSet.getDefaultScopes());
		request.put("credentials", user.getAccessToken());
		String resourceRegistrationEndpoint = user.getAuthServer().getResourceSetRegistrationEndpoint() +
				"/resource_set/" + resourceSet.getResourceId().toString();
		ResourceRegistrationResponse response = httpClient.resourceRegistrationRequest(resourceRegistrationEndpoint, request); 
		resourceSet.setProtectionId(response.getProtectionId());
		resourceSet.setProtectionRevision(response.getRevision());
		
		return resourceSet;
	}
	
	@Override
	public ResourcePermissionsResponse getTicket(ResourceSet resourceSet, UserDetails user) {
		Map<String,String> request = new HashMap<String,String>();
		request.put("resource_set_id", resourceSet.getProtectionId());
		request.put("scopes", resourceSet.getDefaultScopes());
		request.put("credentials", user.getAccessToken());
		String resourcePermissionsEndpoint = user.getAuthServer().getResourcePermissionsEndpoint();
		ResourcePermissionsResponse ticket = httpClient.resourcePermissionsRequest(resourcePermissionsEndpoint, request);
		if (ticket.getTicket() == null) {
			throw new AuthenticationServiceException("Failed to obtain ticket");
		}
		
		return ticket;

	}

}
