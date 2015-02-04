package org.mit.uma.rcserver.services;


import org.mit.uma.rcserver.models.UserDetails;

public interface UserDetailsService {
	
	public UserDetails getUserDetails(String userId);
	public UserDetails getUserDetailsById(Long id);
	public UserDetails createUserDetails(String userId, String authServerHost);

}
