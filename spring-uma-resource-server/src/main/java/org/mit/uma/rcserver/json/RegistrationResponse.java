package org.mit.uma.rcserver.json;

import java.util.Set;
//import java.util.HashSet;
//import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import org.mit.uma.rcserver.models.AuthServerDetails;
//import org.mit.uma.rcserver.models.AuthServerDetails;
//import org.springframework.util.StringUtils;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse implements  AuthServerRegistrationDetails {

	@JsonProperty("client_id")
	String clientId;

	@JsonProperty("client_secret")
	String clientSecret;
	
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="s")
	@JsonDeserialize(using = TimestampDeserializer.class)	
	@JsonProperty("client_secret_expires_at")
	Date clientSecretExpiresAt;

//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="s")
	@JsonDeserialize(using = TimestampDeserializer.class)	
	@JsonProperty("client_id_issued_at")
	Date clientIdIssuedAt;

	@JsonProperty("registration_access_token")
	String registrationAccessToken;

	@JsonProperty("registration_client_uri")
	String registrationClientUri;

	@JsonProperty("redirect_uris")
	Set<String> redirectUris;

	@JsonProperty("client_name")
	String clientName;

	@JsonProperty("token_endpoint_auth_method")
	String tokenEndpointAuthMethod;

	@JsonProperty("scope")
	String scope;
//	Set<String> scope;

	@JsonProperty("grant_types")
	Set<String> grantTypes;

	@JsonProperty("response_types")
	Set<String> responseTypes;

	@JsonProperty("default_acr_values")
	Set<String> defaultAcrValues;
	
/*
	@Override
	public Long getAuthServerId() { 
		return null;
	}

	@Override
	public void setAuthServerId(Long authServerId) { 
	}

	// api details this part of interface is not used
	@Override
	public String getIssuer() {
		return null;
	}

	@Override
	public void setIssuer(String issuer) {
	}


	@Override
	public String getTokenEndpoint() {
		return null;
	}

	@Override
	public void setTokenEndpoint(String tokenEndpoint) {
	}
	
	@Override
	public String getResourceSetRegistrationEndpoint() {
		return null;
	}
	
	@Override
	public void setResourceSetRegistrationEndpoint(String resourceSetRegistrationEndpoint) {
	}
	
	@Override
	public String getAuthorizationEndpoint() {
		return null;
	}

	@Override
	public void setAuthorizationEndpoint(String authorizationEndpoint) {
	}

	@Override
	public String getRegistrationEndpoint() {
		return null;
	}

	@Override
	public void setRegistrationEndpoint(String registrationEndpoint) {
	}


	@Override
	public String getServerUrl() {
		return null;
	}

	@Override
	public void setServerUrl(String serverUrl) {
	}

	
	@Override
	public String getVersion() {
		return null;
	}
	
	@Override
	public void setVersion(String version) {
	}

	@Override
	public String getUserEndpoint() {
		return null;
	}
	
	@Override
	public void setUserEndpoint(String userEndpoint) {
	}
*/

	
	// registration details
	@Override
	public String getClientId() { 
		return clientId;
	}
	
	@Override
	public void setClientId(String clientId) { 
		this.clientId = clientId;
	}
	
	@Override
	public String getClientSecret(){
		return clientSecret;
	}
	
	@Override
	public void setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
	}

	@Override
	public Date getClientSecretExpiresAt() {
		return clientSecretExpiresAt;
	}
	
	@Override
	public void setClientSecretExpiresAt(Date clientSecretExpiresAt) {
		if (clientSecretExpiresAt.getTime() <= 0) {
			this.clientSecretExpiresAt = null;
		} else {
			this.clientSecretExpiresAt = clientSecretExpiresAt;
		}
	}
	
	@Override	
	public Date getClientIdIssuedAt() {
		return clientIdIssuedAt;
	}

	@Override
	public void setClientIdIssuedAt(Date clientIdIssuedAt) {
		if (clientIdIssuedAt.getTime() <= 0) {
			this.clientIdIssuedAt = null;
		} else {
			this.clientIdIssuedAt = clientIdIssuedAt;
		}
	}

	@Override
	public String getRegistrationAccessToken() {
		return registrationAccessToken;
	}

	@Override
	public void setRegistrationAccessToken(String registrationAccessToken) {
		this.registrationAccessToken = registrationAccessToken;
	}

	@Override
	public String getRegistrationClientUri() {
		return registrationClientUri;
	}
	
	@Override
	public void setRegistrationClientUri(String registrationClientUri) {
		this.registrationClientUri = registrationClientUri;
	}

	@Override
	public Set<String> getRedirectUris() {
		return redirectUris;
	}

	@Override
	public void setRedirectUris(Set<String> redirectUris) {
		this.redirectUris = redirectUris;
	}

	@Override
	public String getClientName() {
		return clientName;
	}

	@Override
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public String getTokenEndpointAuthMethod() {
		return tokenEndpointAuthMethod;
	}
	
	@Override
	public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
		this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
	}
/*	
	@Override
	public Set<String> getScope() {
	 return scope;
	}
	
	@Override
	public void setScope(Set<String>  scope) {
		this.scope = scope;
	}
*/
	@Override
	public String getScope() {
	 return scope;
	}
	
	@Override
	public void setScope(String scope) {
		this.scope = scope;
	}
		
	@Override
	public Set<String> getGrantTypes() {
		return grantTypes;
	}
	
	@Override
	public void setGrantTypes(Set<String> grantTypes) {
		this.grantTypes = grantTypes;
	}
	
	@Override
	public Set<String> getResponseTypes() {
		return responseTypes;
	}

	@Override
	public void setResponseTypes(Set<String> responseTypes) {
		this.responseTypes = responseTypes;
	}

	@Override
	public Set<String> getDefaultAcrValues() {
		return defaultAcrValues;
	}

	@Override
	public void setDefaultAcrValues(Set<String> defaultAcrValues) {
		this.defaultAcrValues = defaultAcrValues;
	}

	@Override
	public void copy( AuthServerDetails authDetails) {
		/*
		 * Downcast registration details
		 */
		authDetails.setClientId(clientId);
		authDetails.setClientSecret(clientSecret);
		authDetails.setClientSecretExpiresAt(getClientSecretExpiresAt());
		authDetails.setClientIdIssuedAt(getClientIdIssuedAt());
		authDetails.setRegistrationAccessToken(registrationAccessToken);
		authDetails.setRegistrationClientUri(registrationClientUri);
		authDetails.setRedirectUris(redirectUris);
		authDetails.setClientName(clientName);
		authDetails.setTokenEndpointAuthMethod(tokenEndpointAuthMethod);
		authDetails.setScope(scope);
		authDetails.setGrantTypes(grantTypes);
		authDetails.setResponseTypes(responseTypes);
		authDetails.setDefaultAcrValues(defaultAcrValues);		
	}
}