package org.mit.uma.rcserver.models;

public interface AuthServerApi {

	public String getIssuer();
	public void setIssuer(String issuer);
	
	public String getAuthorizationEndpoint();
	public void setAuthorizationEndpoint(String authorizationEndpoint);

	public String getRegistrationEndpoint();
	public void setRegistrationEndpoint(String authorizationEndpoint);

	public String getTokenEndpoint();
    public void setTokenEndpoint(String tokenEndpoint);

	public String getUserEndpoint();
    public void setUserEndpoint(String userEndpoint);
	
	public String getResourceSetRegistrationEndpoint();
    public void setResourceSetRegistrationEndpoint(String regEndPoint);
    
    public String getResourcePermissionsEndpoint();
    public void setResourcePermissionsEndpoint(String resourcePermissionsEndpoint);
    
    public String getVersion();
    public void setVersion(String version);
    
    public void copy(AuthServerApi authServerApi);
        
}
