package org.mit.uma.rcserver.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {
	
	@JsonProperty("client_name")
	String clientName;

	@JsonProperty("scope")
	ArrayList<String> scope;

	@JsonProperty("redirect_uris")
	ArrayList<String> redirectUris;

	@JsonProperty("client_uri")
	String clientUri;
		
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public ArrayList<String> getScope() {
		return scope;
	}
	
	public void setScope(ArrayList<String> scope) {
		this.scope = scope;
	}

	public ArrayList<String> getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(ArrayList<String> redirectUris) {
		this.redirectUris = redirectUris;
	}

	public String getClientUri() {
		return clientUri;
	}

	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}

}
