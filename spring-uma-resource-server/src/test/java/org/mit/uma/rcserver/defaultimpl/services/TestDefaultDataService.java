package org.mit.uma.rcserver.defaultimpl.services;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.fail;




import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.junit.Before;
//import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

//import org.mockito.InjectMocks;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
//import org.mit.uma.rcserver.defaultimpl.services.DefaultDataService;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.defaultimpl.models.DefaultAuthServerDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceSet;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.defaultimpl.models.DefaultUserDetails;
import org.mit.uma.rcserver.models.UserDetails;

//@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(
  {
	"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml",
	"file:src/main/webapp/WEB-INF/application-context.xml"
  }
)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDefaultDataService {
	
	@Autowired
	private DataService dataService;		

	@Test
	public void TestInsertAuthServerDetails() {
		DefaultAuthServerDetails entity = this._createServerEntity();
		AuthServerDetails result = dataService.saveAuthServerDetails(entity);
		assertNotNull(result.getAuthServerId());
		System.out.print("id +" + result.getAuthServerId());
	}
		

	@Test
	public void TestInsertUserDetails() {
		DefaultUserDetails entity = this._createUserEntity();
		UserDetails result = dataService.saveUserDetails(entity);
		assertNotNull(result.getId());
		System.out.print("id +" + result.getId());
	}
	
	@Before
	public void createUser() {
		DefaultUserDetails entity = this._createUserEntity();
		dataService.saveAuthServerDetails(entity.getAuthServer());
		dataService.saveUserDetails(entity);		
	}
	
	@Test
	public void TestDeleteUserDetails() {
		String userId = "testUser";
		UserDetails user = dataService.removeUser(userId);
		System.out.print("Removed: " + user.getUserId());
		// check that is not in db anymore
		user = dataService.getUserDetails(userId, null);
		assertNull(user.getId());
	}
	
	@Before
	public void prepareDB() {
		DefaultUserDetails entity = this._createUserEntity();
		dataService.saveAuthServerDetails(entity.getAuthServer());
		dataService.saveUserDetails(entity);		
	}

	@Test
	public void TestInsertResourceSet() {
		UserDetails owner = dataService.getUserDetails("testUser", null);
		// create root resource set
		ResourceSet root = _createResourceSet("", owner);
		ResourceSet childRs = _createResourceSet("child-set",owner);
		// create resource
		ResourceDetails resource = _createResourceDetails("rout-child", owner);
		ResourceDetails resource1 = _createResourceDetails("child-set-child", owner);
		//add resource to root
		try {
			root.addResource(resource);
			childRs = root.addResourceSet(childRs);
			resource1 = childRs.addResource(resource1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		// save resource set
		dataService.saveResource(root);

		assertNotNull(root.getResourceId());
		assertTrue(root.getResources().size() > 0);
		for (ResourceDetails d : root.getResources().values()) {
			assertNotNull(d.getResourceId());
		}

		// test removal
		root = dataService.getResourceSetByPath("");
		assertNotNull(root);
		for (ResourceDetails d : root.getResources().values()) {
			assertNotNull(d.getResourceId());
			dataService.removeResource(d);
		}

		dataService.removeUser(owner.getUserId());
	}
	
//	@Test
//	public void TestgetResourceSetByPath() {
//		String resourceSetPath="resource/user2";
//		ResourceSet resourceSet = dataService.getResourceSetByPath(resourceSetPath);
//		System.out.println("***" + resourceSet.getOwnerId());
//	}
	
	DefaultAuthServerDetails _createServerEntity() {
		DefaultAuthServerDetails entity = new DefaultAuthServerDetails();		
		// api
		entity.setIssuer("http://test_issuer");		
		entity.setUserEndpoint("http://test/user_endpoint");		
		entity.setResourceSetRegistrationEndpoint("http://test/user_registration_endpoint");
		entity.setVersion("1.0");
		
		// registration
		entity.setClientId("clientId");
		entity.setClientSecret("clientSecret");
		
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			entity.setClientSecretExpiresAt(dateParser.parse("2014-01-01 17:00:00"));		
			entity.setClientIdIssuedAt(dateParser.parse("2014-01-01 17:00:00"));		
		} catch(ParseException pe) {
            System.out.println("ERROR: Failed to parse timestamps");
        }
		entity.setRegistrationAccessToken("regAccessToken");		
		entity.setRegistrationClientUri("regClientUri");
		entity.setClientName("clientName");
		entity.setTokenEndpointAuthMethod("tokenEndpointAuthMethod");
		entity.setScope("scope");		
		
		Set<String> redirectUris = new HashSet<String>(Arrays.asList("uri1", "uri2"));
		entity.setRedirectUris(redirectUris);	
		
		Set<String> grantTypes = new HashSet<String>(Arrays.asList("grant1", "grant2"));
		 entity.setGrantTypes(grantTypes);		
		 
		Set<String> responseType = new HashSet<String>(Arrays.asList("response1", "response2"));
		entity.setResponseTypes(responseType);
		
		Set<String> defaultAcrValues = new HashSet<String>(Arrays.asList("default_acr1", "default_acr2"));
		entity.setDefaultAcrValues(defaultAcrValues);
				
		return entity;
	}

	DefaultUserDetails _createUserEntity() {
		DefaultUserDetails entity = new DefaultUserDetails();

		entity.setUserId("testUser");
		entity.setAccessToken("pat");
		
		DefaultAuthServerDetails authServer = _createServerEntity();
		entity.setAuthServerEntity(authServer);
		
		return entity;
	}
	
	DefaultResourceSet _createResourceSet(String name, UserDetails owner) {
		DefaultResourceSet resourceSet = new DefaultResourceSet();
		resourceSet.setResourceName(name);
		resourceSet.setResourcePath("");
		resourceSet.setOwnerId(owner.getId());
		resourceSet.setProtectionId("protectionId");
		resourceSet.setResourceDescription("resourceDescription");
		resourceSet.setProtectionRevision("revisionId");
		resourceSet.setResourceUri("resourceUri");
		
		return resourceSet;
	}
	
	DefaultResourceDetails _createResourceDetails(String name, UserDetails owner) {
		DefaultResourceDetails resource = new DefaultResourceDetails();
		resource.setOwnerId(owner.getId());
		resource.setResourceName(name);
		resource.setResourceDescription("resourceDescription");
		resource.setResourceUri("resourceUri");
		
		return resource;
	}
	
}
