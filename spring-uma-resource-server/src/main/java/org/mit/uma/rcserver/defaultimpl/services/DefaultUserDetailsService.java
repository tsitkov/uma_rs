package org.mit.uma.rcserver.defaultimpl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mit.uma.rcserver.models.AuthServerApi;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.services.AuthorizationService;
import org.mit.uma.rcserver.services.AuthRegistrationService;
import org.mit.uma.rcserver.services.UserDetailsService;


@Service
public class DefaultUserDetailsService implements UserDetailsService {

	@Autowired
	private DataService dataService;
	
	@Autowired
	private AuthRegistrationService authRegistrationService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Override
	public UserDetails getUserDetails(String userId) {
		return dataService.getUserDetails(userId, null);
	}

	@Override
	public UserDetails getUserDetailsById(Long id) {
		return dataService.getUserDetailsById(id);
	}

	@Override
	public UserDetails createUserDetails(String userId, String authServerUrl) {
		// convert server url to standardized issuer 
		AuthServerApi api = authRegistrationService.getAuthServerAPIs(authServerUrl);
		assert(api != null);
		String issuer = api.getIssuer();
		// make sure that this is a new user
		UserDetails userDetails = dataService.getUserDetails(userId, issuer);
		assert(userDetails != null);
		
		if (userDetails.getId() != null) {
			if (userDetails.getAccessToken() != null) {
				throw new IllegalStateException("User with username=" + userId + " is already registered");
			}
			// pat is not set we need to complete registration
			return userDetails;
		}
		/* Now we know that this is a new user. The first step is to check if we are 
		 * registered with his authorization Server.
		 */
		AuthServerDetails authServer = userDetails.getAuthServer();
		assert(authServer != null);
		
		if (authServer.getAuthServerId() == null) {
			// this is a new authorization Server, we need to register with it.
			authServer.setApiDetails(api);
			AuthServerRegistrationDetails registrationDetails = authRegistrationService.register(api);
			authServer.setRegistrationDetails(registrationDetails);
			// authorization server config info
			dataService.saveAuthServerDetails(authServer);
		}
		// populate user details
		dataService.saveUserDetails(userDetails);
		
		return userDetails;
	}

}
