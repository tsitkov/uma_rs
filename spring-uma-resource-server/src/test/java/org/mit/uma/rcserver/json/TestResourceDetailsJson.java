package org.mit.uma.rcserver.json;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TestResourceDetailsJson {

	private final String resourceDetailsMessage = "{" +
			  "\"composite\":\"true\"" + ",\n" +
			  "\"description\":\"test-description\"" +",\n" +
			  "\"name\":\"resource-name\"" +",\n" +
			  "\"uri\":\"http://localhost\"" +"\n" +
		    "}";
	
    @Test
	public void TestResourceDetailsProcessor() {
		ObjectMapper mapper = new ObjectMapper();

		System.out.print(resourceDetailsMessage + "\n");
		
		try {
			ResourceDetailsJson response = mapper.readValue(resourceDetailsMessage, new TypeReference<ResourceDetailsJson>() { });
						
			assertTrue("test-description".equals(response.getResourceDescription()));
			assertTrue("http://localhost".equals(response.getResourceUri()));
			
			Set<ResourceDetailsJson> collection = new HashSet<ResourceDetailsJson>();
			collection.add(response);
			
			Set<ResourceDetailsJson> collectionFromJsonArray = JsonUtil.fromJsonArray(JsonUtil.toString(collection), ResourceDetailsJson.class);
			System.out.println("From json array");
			System.out.println(JsonUtil.toString(collectionFromJsonArray));			
			
			//System.out.println(JsonUtil.toString(response) + "\n");
			//System.out.println(JsonUtil.toString(collection) + "\n");
			
		} catch (JsonGenerationException e) { 
			e.printStackTrace();
		} catch (JsonMappingException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	
	}

	
}
