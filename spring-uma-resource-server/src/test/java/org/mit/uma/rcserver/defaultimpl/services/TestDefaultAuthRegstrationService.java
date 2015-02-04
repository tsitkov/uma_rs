package org.mit.uma.rcserver.defaultimpl.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mit.uma.rcserver.config.Configurator;
import org.mit.uma.rcserver.json.AuthServerApiJson;
import org.mit.uma.rcserver.models.AuthServerApi;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.AuthServerRegistrationDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultAuthServerDetails;
import org.mit.uma.rcserver.services.AuthRegistrationService;

@ContextConfiguration(
  {
	"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml",
	"file:src/main/webapp/WEB-INF/application-context.xml"
  }
)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDefaultAuthRegstrationService {

	@Autowired
	Configurator config;
	
	@Autowired
	AuthRegistrationService regService;

	@Test
	public void testApiDiscovery() {
		String authServerUrl = config.getTestAuthServerUrl();
		AuthServerApi api = regService.getAuthServerAPIs(authServerUrl);
		assertNotNull(api.getResourceSetRegistrationEndpoint());
		assertNotNull(api.getUserEndpoint());
		assertNotNull(api.getVersion());
	}
	
	@Test
	public void testRegistration() {
		AuthServerApi api = regService.getAuthServerAPIs(config.getTestAuthServerUrl());
		AuthServerRegistrationDetails registrationDetails = regService.register(api);		
		assertNotNull(registrationDetails.getClientId());
		assertNotNull(registrationDetails.getClientSecret());		
	}	
}
