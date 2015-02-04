package org.mit.uma.rcserver.services;

import org.mit.uma.rcserver.models.AuthServerApi;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;


public interface AuthRegistrationService {
	
	public AuthServerApi getAuthServerAPIs(String authServerUrl);
	public AuthServerRegistrationDetails register(AuthServerApi authServerApi);

}
