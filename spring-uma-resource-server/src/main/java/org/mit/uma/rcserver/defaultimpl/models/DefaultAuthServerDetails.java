package org.mit.uma.rcserver.defaultimpl.models;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Date;

import org.springframework.util.StringUtils;

import javax.persistence.Basic;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.mit.uma.rcserver.models.AuthServerApi;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;


@Entity
@Table(name = "am_details")
@NamedQueries({
	@NamedQuery(name = "AmDetailsEntity.findAll", query = "SELECT c FROM DefaultAuthServerDetails c"),
	@NamedQuery(name = "AmDetailsEntity.getByServerUrl", query = "select c from DefaultAuthServerDetails c where c.issuer = :issuer")
})
public class DefaultAuthServerDetails implements AuthServerDetails {

	private Long authServerId;
	
	// api details
//	private String serverUrl;
	private String issuer;
	private String tokenEndpoint;
	private String resourceSetRegistrationEndpoint;    
	private String version;
    private String userEndpoint;
    private String registrationEndpoint;
    private String authorizationEndpoint;
    private String resourcePermissionsEndpoint;
	// registration details
    private String clientId;
    private String clientSecret;
	private Date clientSecretExpiresAt;
	private Date clientIdIssuedAt;
	private String registrationAccessToken;
	private String registrationClientUri;
	private String clientName;
	private String tokenEndpointAuthMethod;
	private Set<String> scope;
	private Set<String> redirectUris;
	private Set<String> grantTypes;
	private Set<String> responseTypes;
	private Set<String> defaultAcrValues;
		
	public DefaultAuthServerDetails() {
		
	}
	
	public DefaultAuthServerDetails(
			AuthServerDetails registrationDetails) {
		this.authServerId = registrationDetails.getAuthServerId();
		// api details
//		this.serverUrl = registrationDetails.getServerUrl();
		this.issuer = registrationDetails.getIssuer();
		this.resourceSetRegistrationEndpoint = registrationDetails.getResourceSetRegistrationEndpoint();    
		this.version = registrationDetails.getVersion();
	    this.userEndpoint = registrationDetails.getUserEndpoint();
	    this.registrationEndpoint = registrationDetails.getRegistrationEndpoint();
	    this.authorizationEndpoint = registrationDetails.getAuthorizationEndpoint();
		
		// registration details
		this.clientId = registrationDetails.getClientId();
		this.clientSecret = registrationDetails.getClientSecret();
		this.clientSecretExpiresAt = registrationDetails.getClientSecretExpiresAt();
		this.clientIdIssuedAt = registrationDetails.getClientIdIssuedAt();
		this.registrationAccessToken = registrationDetails.getRegistrationAccessToken();
		this.registrationClientUri = registrationDetails.getRegistrationClientUri();
		this.redirectUris = registrationDetails.getRedirectUris();
		this.clientName = registrationDetails.getClientName();
		this.tokenEndpointAuthMethod = registrationDetails.getTokenEndpointAuthMethod();
		setScope(registrationDetails.getScope());
		this.grantTypes = registrationDetails.getGrantTypes();
		this.responseTypes = registrationDetails.getResponseTypes();
		this.defaultAcrValues = registrationDetails.getDefaultAcrValues();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="auth_server_id")
	@Override
	public Long getAuthServerId() { 
		return authServerId;
	}

	@Override
	public void setAuthServerId(Long authServerId) { 
		this.authServerId = authServerId;
	}

	// api details
	@Basic
	@Column(name="resource_set_registration_endpoint")
	@Override
	public String getResourceSetRegistrationEndpoint() {
		return resourceSetRegistrationEndpoint;
	}
	
	@Override
	public void setResourceSetRegistrationEndpoint(String resourceSetRegistrationEndpoint) {
		this.resourceSetRegistrationEndpoint = resourceSetRegistrationEndpoint;
	}

	@Basic
	@Column(name="issuer")
	@Override
	public String getIssuer() {
		// TODO Auto-generated method stub
		return issuer;
	}

	@Override
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Basic
	@Column(name="token_endpoint")
	@Override
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	@Override
	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}

	@Basic
	@Column(name="user_endpoint")
	@Override
	public String getUserEndpoint() {
		// TODO Auto-generated method stub
		return userEndpoint;
	}

	@Override
	public void setUserEndpoint(String userEndpoint) {
		this.userEndpoint = userEndpoint;
	}

/*	
	@Basic
	@Column(name="server_url")
	@Override
	public String getServerUrl() {
		return serverUrl;
	}

	@Override
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
*/
	
	@Basic
	@Column(name="version")
	@Override
	public String getVersion() {
		return version;
	}
	
	@Override
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Basic
	@Column(name="registration_endpoint")
	@Override
	public String getRegistrationEndpoint() {
		return registrationEndpoint;
	}	
	
	@Override
	public void setRegistrationEndpoint(String registrationEndpoint) {
		this.registrationEndpoint = registrationEndpoint;
	}

	@Basic
	@Column(name="authorization_endpoint")
	@Override
	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	@Override
	public void setAuthorizationEndpoint(String authorizationEndpoint) {
		this.authorizationEndpoint = authorizationEndpoint;
	}

	@Basic
	@Column(name="resource_permissions_endpoint")
	@Override
	public String getResourcePermissionsEndpoint() {
		// TODO Auto-generated method stub
		return resourcePermissionsEndpoint;
	}

	@Override
	public void setResourcePermissionsEndpoint(
			String resourcePermissionsEndpoint) {
		this.resourcePermissionsEndpoint = resourcePermissionsEndpoint;
	}
	
	// registration details
	@Basic
	@Column(name="client_id")
	@Override
	public String getClientId() { 
		return clientId;
	}
	
	@Override
	public void setClientId(String clientId) { 
		this.clientId = clientId;
	}
	
	@Basic
	@Column(name="client_secret")
	@Override
	public String getClientSecret(){
		return clientSecret;
	}
	
	@Override
	public void setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
	}

	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="client_secret_expires")
	@Override
	public Date getClientSecretExpiresAt() {
		return clientSecretExpiresAt;
	}
	
	@Override
	public void setClientSecretExpiresAt(Date clientSecretExpiresAt) {
		this.clientSecretExpiresAt = clientSecretExpiresAt;
	}
	
	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name="client_id_issued")
	@Override	
	public Date getClientIdIssuedAt() {
		return clientIdIssuedAt;
	}

	@Override
	public void setClientIdIssuedAt(Date clientIdIssuedAt) {
		this.clientIdIssuedAt = clientIdIssuedAt;
	}

	@Basic
	@Column(name="registration_access_token")
	@Override
	public String getRegistrationAccessToken() {
		return registrationAccessToken;
	}

	@Override
	public void setRegistrationAccessToken(String registrationAccessToken) {
		this.registrationAccessToken = registrationAccessToken;
	}

	@Basic
	@Column(name="registration_client_uri")
	@Override
	public String getRegistrationClientUri() {
		return registrationClientUri;
	}
	
	@Override
	public void setRegistrationClientUri(String registrationClientUri) {
		this.registrationClientUri = registrationClientUri;
	}

	@Basic
	@Column(name="client_name")
	@Override
	public String getClientName() {
		return clientName;
	}

	@Override
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Basic
	@Column(name="token_endpoint_auth_method")
	@Override
	public String getTokenEndpointAuthMethod() {
		return tokenEndpointAuthMethod;
	}
	
	@Override
	public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
		this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name="client_scope",
		joinColumns=@JoinColumn(name="auth_server_id"))
	@Column(name="scope")
	public Set<String> getScopeAsSet() {
		return scope;
	}
	
	public void setScopeAsSet(Set<String>  scope) {
		this.scope = scope;
	}
	
	@Transient
	@Override
	public String getScope() {
		return StringUtils.collectionToDelimitedString(scope, " ");
	}
	
	@Override
	public void setScope(String  scope) {
		this.scope = new HashSet<String>(Arrays.asList(scope.split(" ")));
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name="client_redirect_uri",
		joinColumns=@JoinColumn(name="auth_server_id"))
	@Column(name="redirect_uri")
	@Override
	public Set<String> getRedirectUris() {
		return redirectUris;
	}

	@Override
	public void setRedirectUris(Set<String> redirectUris) {
		this.redirectUris = redirectUris;
	}
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name="client_grant_type",
		joinColumns=@JoinColumn(name="auth_server_id"))
	@Column(name="grant_type")
	@Override
	public Set<String> getGrantTypes() {
		return grantTypes;
	}
	
	@Override
	public void setGrantTypes(Set<String> grantTypes) {
		this.grantTypes = grantTypes;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name="client_response_type",
		joinColumns=@JoinColumn(name="auth_server_id"))
	@Column(name="response_type")
	@Override
	public Set<String> getResponseTypes() {
		return responseTypes;
	}

	@Override
	public void setResponseTypes(Set<String> responseTypes) {
		this.responseTypes = responseTypes;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name="client_default_acr_value",
		joinColumns=@JoinColumn(name="auth_server_id"))
	@Column(name="default_acr_value")
	@Override
	public Set<String> getDefaultAcrValues() {
		return defaultAcrValues;
	}

	@Override
	public void setDefaultAcrValues(Set<String> defaultAcrValues) {
		this.defaultAcrValues = defaultAcrValues;
	}

	@Transient
	@Override
	public AuthServerApi getApiDetails() {
		return this;
	}

	@Override
	public void setApiDetails(AuthServerApi api) {
		api.copy(this);		
	}

	@Transient
	@Override
	public AuthServerRegistrationDetails getRegistrationDetails() {
		return this;
	}

	@Override
	public void setRegistrationDetails(
			AuthServerRegistrationDetails registrationDetails) {
		registrationDetails.copy(this);
	}

	@Override
	public void copy(AuthServerApi authServerApi) {
		throw new UnsupportedOperationException("Not implementd");
	}

	@Override
	public void copy(AuthServerDetails authServerRegistrationDetails) {
		throw new UnsupportedOperationException("Not implementd");
	}

}
