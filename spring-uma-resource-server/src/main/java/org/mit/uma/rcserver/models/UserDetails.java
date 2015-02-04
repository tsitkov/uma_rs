package org.mit.uma.rcserver.models;

import org.mit.uma.rcserver.models.AuthServerDetails;

public interface UserDetails {

	public Long getId();
	public void setId(Long name);
	
	public String getUserId();
	public void setUserId(String userId);
		
	public AuthServerDetails getAuthServer();
	public void setAuthServer(AuthServerDetails authServerDetails);
	
	public String getAccessToken();
	public void setAccessToken(String pat);
	
	public String getRefreshToken();
	public void setRefreshToken(String refreshToken);
	
}
