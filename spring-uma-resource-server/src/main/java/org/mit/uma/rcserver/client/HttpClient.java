package org.mit.uma.rcserver.client;

import java.util.Map;

import org.mit.uma.rcserver.json.AuthServerApiJson;
import org.mit.uma.rcserver.json.PatResponse;
import org.mit.uma.rcserver.json.RegistrationRequest;
import org.mit.uma.rcserver.json.RegistrationResponse;
import org.mit.uma.rcserver.json.ResourcePermissionsResponse;
import org.mit.uma.rcserver.json.ResourceRegistrationResponse;

public interface HttpClient {
	
	public enum AuthorizationType {
		BASIC("Basic"),
		BEARER("Bearer");
		
		public String value;
		
		AuthorizationType(String value) {
			this.value = value;
		}		
	}
	
	AuthServerApiJson apiRequest(String apiEndPoint);
	RegistrationResponse registrationRequest(String registrationEndpoint, RegistrationRequest request);
	PatResponse patRequest(String tokenEndpoint, Map<String,String> request);
	ResourceRegistrationResponse resourceRegistrationRequest(String resourceRegistrationEndpoint, Map<String,String> request);
	ResourcePermissionsResponse resourcePermissionsRequest(String permissionEndpoint, Map<String,String> request);

	//	void clientAccessVerificationRequest();
	
}
