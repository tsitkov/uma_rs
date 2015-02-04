package org.mit.uma.rcserver.models;

public interface AuthServerDetails extends AuthServerApi, AuthServerRegistrationDetails {

	public Long getAuthServerId();
	public void setAuthServerId(Long clientId);
	
	public AuthServerApi getApiDetails();
	public void setApiDetails(AuthServerApi api);

	public AuthServerRegistrationDetails getRegistrationDetails();
	public void setRegistrationDetails(AuthServerRegistrationDetails registrationDetails);
	
}
