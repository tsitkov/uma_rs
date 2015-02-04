package org.mit.uma.rcserver.models;

import java.util.Date;
import java.util.Set;

public interface AuthServerRegistrationDetails {

	public String getClientId();
	public void setClientId(String clientId);
	
	public String getClientSecret();
	public void setClientSecret(String clientSecret);
		
	public Date getClientSecretExpiresAt();
	public void setClientSecretExpiresAt(Date secretExpirationTime);
	
	public Date getClientIdIssuedAt();
	public void setClientIdIssuedAt(Date clientIdIssuedAt);
	
	public String getRegistrationAccessToken();
	public void setRegistrationAccessToken(String regAccessToken);
	
	public String getRegistrationClientUri();
	public void setRegistrationClientUri(String regClientUri);
	
	public Set<String> getRedirectUris();
	public void setRedirectUris(Set<String> redirectUris);
	
	public String getClientName();
	public void setClientName(String clientName);
	
	public String getTokenEndpointAuthMethod();
	public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod);
	
//	public Set<String> getScope();
//	public void setScope(Set<String> scope);
	public String getScope();
	public void setScope(String scope);
	
	public Set<String> getGrantTypes();
	public void setGrantTypes(Set<String> grantTypes);
	
	public Set<String> getResponseTypes();
	public void setResponseTypes(Set<String> responseType);
	
	public Set<String> getDefaultAcrValues();
	public void setDefaultAcrValues(Set<String> defaultAcrValues);

    public void copy(AuthServerDetails authServerRegistrationDetails);

}
