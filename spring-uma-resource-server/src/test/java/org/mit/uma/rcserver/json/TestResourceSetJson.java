package org.mit.uma.rcserver.json;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceSet;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.ResourceDetails;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;


public class TestResourceSetJson {
	
    @Test
	public void TestResourceDetailsProcessor() {
		ObjectMapper mapper = new ObjectMapper();

    	ResourceSet resourceSet = _createResourceSet("root");
    	ResourceDetails child1 = this._createResourceDetails("child1");
    	ResourceDetails child2 = this._createResourceDetails("child2");
    	try {
    		resourceSet.addResource(child1);
    		resourceSet.addResource(child2);
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
    	}

    	ResourceSetJson resourceSetJson = new ResourceSetJson(resourceSet);
    	String jsonString = JsonUtil.toString(resourceSetJson);
    	try {
    		ResourceSetJson transformed = mapper.readValue(jsonString, ResourceSetJson.class); 
        	System.out.println(JsonUtil.toString(transformed));
    	} catch (JsonGenerationException e) { 
			e.printStackTrace();
		} catch (JsonMappingException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}

    }

	

	

	DefaultResourceSet _createResourceSet(String name) {
		DefaultResourceSet resourceSet = new DefaultResourceSet();
		resourceSet.setResourceName(name);
		resourceSet.setResourcePath("");
		resourceSet.setOwnerId(0L);
		resourceSet.setProtectionId("protectionId");
		resourceSet.setResourceDescription("resourceDescription");
		resourceSet.setProtectionRevision("revisionId");
		resourceSet.setResourceUri("resourceUri");
		
		return resourceSet;
	}
	
	DefaultResourceDetails _createResourceDetails(String name) {
		DefaultResourceDetails resource = new DefaultResourceDetails();
		resource.setOwnerId(0L);
		resource.setResourceName(name);
		resource.setResourceDescription("resourceDescription");
		resource.setResourceUri("resourceUri");
		
		return resource;
	}
	
}
