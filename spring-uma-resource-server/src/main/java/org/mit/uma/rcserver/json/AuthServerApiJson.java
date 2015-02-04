
package org.mit.uma.rcserver.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


//import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.AuthServerApi;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthServerApiJson implements AuthServerApi {
	
	@JsonProperty("issuer")
	private String issuer;
	
	@JsonProperty("token_endpoint")
	private String tokenEndpoint;
	
	@JsonProperty("resource_set_registration_endpoint")
	private String resourceSetRegistrationEndpoint;
    
	@JsonProperty("version")
	private String version;
    
	@JsonProperty("registration_endpoint")
    private String registrationEndpoint;
	
	@JsonProperty("authorization_request_endpoint")
    private String authorizationEndpoint;

	@JsonProperty("user_endpoint")
    private String userEndpoint;

	@JsonProperty("permission_registration_endpoint")
	private String resourcePermissionsEndpoint;

	
	@Override
	public String getResourceSetRegistrationEndpoint() {
        return resourceSetRegistrationEndpoint;
    }
	
	@Override
	public void setResourceSetRegistrationEndpoint(String regEndPoint) {
        this.resourceSetRegistrationEndpoint = regEndPoint;
    }
    
	@Override	
    public String getVersion() {
    	return version;
    }
	
	@Override	
    public void setVersion(String version) {
		this.version = version;
    }

	@Override
	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	@Override
	public void setAuthorizationEndpoint(String authorizationEndpoint) {
		this.authorizationEndpoint =  authorizationEndpoint;
	}

	@Override
	public String getRegistrationEndpoint() {
		return registrationEndpoint;
	}

	@Override
	public void setRegistrationEndpoint(String registrationEndpoint) {
		this.registrationEndpoint = registrationEndpoint;		
	}
	
	@Override    
    public String getUserEndpoint() {
    	return userEndpoint;
    }

	@Override    
    public void setUserEndpoint(String userEndPoint) {
    	this.userEndpoint = userEndPoint;
    }
	
	@Override
	public String getResourcePermissionsEndpoint() {
		return resourcePermissionsEndpoint;
	}

	@Override
	public void setResourcePermissionsEndpoint(
			String resourcePermissionsEndpoint) {
		this.resourcePermissionsEndpoint = resourcePermissionsEndpoint;
	}

	@Override
	public String getIssuer() {
		return issuer;
	}

	@Override
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Override
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	@Override
	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}
	
	@Override
	public void copy( AuthServerApi authDetails) {
		authDetails.setIssuer(issuer);
		authDetails.setTokenEndpoint(this.tokenEndpoint);
		authDetails.setRegistrationEndpoint(registrationEndpoint);
		authDetails.setAuthorizationEndpoint(authorizationEndpoint);
    	authDetails.setResourceSetRegistrationEndpoint(resourceSetRegistrationEndpoint);
    	authDetails.setUserEndpoint(userEndpoint);
    	authDetails.setResourcePermissionsEndpoint(resourcePermissionsEndpoint);
		authDetails.setVersion(version);
    }
    
}
