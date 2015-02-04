package org.mit.uma.rcserver.services;

//import java.util.Set;
import java.util.Collection;

import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;

public interface DataService {
	/* Data service should provide convenient API to work three tables:
	 * 	user_details
	 *  am_server_details
	 *  user_resources  
	 */
	
	/*
	 * The first group of methods provides access to Authorization Servers 
	 * details table.
	 */
	public Collection<? extends AuthServerDetails> findAllAuthServers();	
	public AuthServerDetails getAuthServerDetails(String host);
	public AuthServerDetails saveAuthServerDetails(AuthServerDetails config);
	
	/*
	 * The User detail table API
	 */
	public UserDetails getUserDetails(String userId, String host);
	public UserDetails getUserDetailsById(Long id);
	public UserDetails saveUserDetails(UserDetails userDetails);
	public UserDetails removeUser(String userId);
	
	/* 
	 * Resource manipulation API
	 */
	public ResourceSet getRootResourceSet(Long userId, boolean create);
	public ResourceSet getResourceSetById(Long id);
	public ResourceSet getResourceSetByPath(String resourceSetPath);
	//	public void addResource(ResourceSet parent, ResourceDetails resource);
	public ResourceDetails saveResource(ResourceDetails resource);	
	public ResourceDetails removeResource(ResourceDetails resource);
}
