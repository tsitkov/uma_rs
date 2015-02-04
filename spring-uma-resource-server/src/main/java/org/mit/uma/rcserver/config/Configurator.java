package org.mit.uma.rcserver.config;

public class Configurator {
	
	String thisServerName;
	String thisServerUrl;
	String thisServerLoginUrl;
	String thisServerRedirectUrl;
	String testAuthServerUrl;
	String authServerApiEndpoint;
	
	public String getThisServerName() {
		return thisServerName;
	}
	
	public void setThisServerName(String thisServerName) {
		this.thisServerName = thisServerName;
	}

	public String getThisServerUrl() {
		return thisServerUrl;
	}
	
	public void setThisServerUrl(String thisServerUrl) {
		this.thisServerUrl = thisServerUrl;
	}

	public String getAuthServerApiEndpoint() {
		return authServerApiEndpoint;
	}
	
	public void setAuthServerApiEndpoint(String authServerApiEndPoint) {
		this.authServerApiEndpoint = authServerApiEndPoint;
	}
	
	public String getTestAuthServerUrl() {
		return testAuthServerUrl;
	}
	
	public void setTestAuthServerUrl(String testAuthServerUrl) {
		this.testAuthServerUrl = testAuthServerUrl;
	}
	
	public String getLoginUrl() {
		return thisServerLoginUrl;
	}

	public void setLoginUrl(String thisServerLoginUrl) {
		this.thisServerLoginUrl = thisServerLoginUrl;
	}

	public String getDefaultRedirectUrl() {
		return thisServerRedirectUrl;
	}

	public void setDefaultRedirectUrl(String thisServerRedirectUrl) {
		this.thisServerRedirectUrl = thisServerRedirectUrl;
	}

}
