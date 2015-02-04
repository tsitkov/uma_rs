package org.mit.uma.rcserver.services;

import org.mit.uma.rcserver.json.ResourcePermissionsResponse;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;

public interface ResourceAccessService {

	// Returns protected resource
	public ResourceSet protect(ResourceSet resourceSet, UserDetails user);
	// get ticket for resource set
	public ResourcePermissionsResponse getTicket(ResourceSet resourceSet, UserDetails user);
		
}
