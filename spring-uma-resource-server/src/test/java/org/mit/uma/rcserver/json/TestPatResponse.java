package org.mit.uma.rcserver.json;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestPatResponse {

	private final String responseMessage = "{" +
			  "\"access_token\":\"accessToken\"" + ",\n" +
			  "\"token_type\":\"Bearer\"" + ",\n" +
			  "\"refresh_token\":\"refreshToken\"" + ",\n" +
			  "\"expires_in\":\"3599\"" + ",\n" +
			  "\"scope\":\"scope\"" + "\n" +
	        "}";
	
    @Test
	public void TestRegistrationResponseProcessor() {
		ObjectMapper mapper = new ObjectMapper();

		System.out.print(responseMessage + "\n");
		
		try {
			PatResponse response = mapper.readValue(responseMessage, new TypeReference<PatResponse>() { });
						
			assertTrue("accessToken".equals(response.getAccessToken()));
			assertTrue("Bearer".equals(response.getTokenType()));
			
			System.out.print(JsonUtil.toString(response) + "\n");
			System.out.print(response.getTokenExpiresAt() + "\n");
			
		} catch (JsonGenerationException e) { 
			e.printStackTrace();
		} catch (JsonMappingException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	
	}

}
