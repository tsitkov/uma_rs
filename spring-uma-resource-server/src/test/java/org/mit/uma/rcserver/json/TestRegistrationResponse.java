package org.mit.uma.rcserver.json;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mit.uma.rcserver.json.RegistrationResponse;


public class TestRegistrationResponse {

	private final String responseMessage = "{" +
		  "\"client_id\":\"clientId\"" + ",\n" +
		  "\"client_secret\":\"clientSecret\"" +",\n" +
		  "\"client_secret_expires_at\":\"0\"" +",\n" +
		  "\"client_id_issued_at\":\"1417052848\"" +",\n" +
		  "\"registration_access_token\":\"registrationAccessToken\"" +",\n" +
		  "\"registration_client_uri\":\"registrationClientUri\"" +",\n" +
          "\"redirect_uris\": [\"uri1\",\"ur2\"]" +",\n" +
          "\"client_name\":\"clientName\"" +",\n" +
          "\"token_endpoint_auth_method\":\"tokenEndpointAuthMethod\"" +",\n" +
          "\"scope\":\"scope1 docs.kantarainitiative.org/uma/scopes/prot.json\"" +",\n" +
          "\"grant_types\":[\"grant1\",\"grant2\"]" +",\n" +
          "\"response_types\":[\"response_type1\",\"response_type2\"]" +",\n" +
          "\"default_acr_values\":[\"default_acr1\",\"default_acr1\"]" + "\n" +
        "}";

    @Test
	public void TestRegistrationResponseProcessor() {
		ObjectMapper mapper = new ObjectMapper();

		try {
			RegistrationResponse response = mapper.readValue(responseMessage, new TypeReference<RegistrationResponse>() { });
			
			System.out.print("RESPONSE:\n" + JsonUtil.toString(response) + "\n");
					
			System.out.print(response.getClientSecretExpiresAt() + "\n");
			System.out.print(response.getClientIdIssuedAt() + "\n");
			
			
			assertTrue("clientId".equals(response.getClientId()));
			assertTrue(response.getResponseTypes().size() == 2);
			
		} catch (JsonGenerationException e) { 
			e.printStackTrace();
		} catch (JsonMappingException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	
	}

}
