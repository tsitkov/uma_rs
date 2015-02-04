package org.mit.uma.rcserver.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PatResponse {

	@JsonProperty("access_token")
	String accessToken;
		
	@JsonProperty("token_type")
	String tokenType;
	
	@JsonProperty("refresh_token")
	String refreshToken;
	
	@JsonDeserialize(using = CurrentTimePlusDelta.class)	
	@JsonProperty("expires_in")
	Date tokenExpiresAt;
	
	@JsonProperty("scope")
	String scope;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getTokenExpiresAt() {
		return tokenExpiresAt;
	}

	public void setTokenExpiresAt(Date tokenExpiresAt) {
		this.tokenExpiresAt = tokenExpiresAt;
	}
	
	
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
