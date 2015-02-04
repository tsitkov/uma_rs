package org.mit.uma.rcserver.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceSetJson implements ResourceSet {

	@JsonProperty("description")		
	private String resourceDescription;
		
	@JsonProperty("path")
	private String resourcePath;
	
	@JsonProperty("resources")
	private Collection<ResourceDetailsJson> resources;
	
	ResourceSetJson() {
		
	}
	
	// copy constructor
	public ResourceSetJson(ResourceSet resourceSet) {
		this.resourceDescription = resourceSet.getResourceDescription();
		this.resourcePath = resourceSet.getResourcePath();
		if (resourceSet.getResources() != null) {
			this.resources = new ArrayList<ResourceDetailsJson>(); 
			for (ResourceDetails r: resourceSet.getResources().values()) {
				this.resources.add(new ResourceDetailsJson(r));
			}
		}
	}
	
	@JsonIgnore
	@Override
	public Long getResourceId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setResourceId(Long parentId) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	@Override
	public ResourceSet getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore	
	@Override
	public Long getOwnerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOwnerId(Long ownerId) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	@Override
	public String getResourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResourceName(String resourceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	@Override
	public String getResourceDescription() {
		return resourceDescription;
	}

	@Override
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	@JsonIgnore
	@Override
	public String getResourceUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResourceUri(String resourceUri) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	@Override
	public boolean isResourceSet() {
		// TODO Auto-generated method stub
		return false;
	}

	@JsonIgnore
	@Override
	public String getDefaultScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	@Override
	public String getProtectionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProtectionId(String protectionId) {
		// TODO Auto-generated method stub
		
	}

	@JsonIgnore
	@Override
	public String getProtectionRevision() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProtectionRevision(String revisionId) {
		// TODO Auto-generated method stub
		
	}
	
	@JsonIgnore
	@Override
	public Map<String, ? extends ResourceDetails> getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceDetails addResource(ResourceDetails resourceDetails)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	public Collection<ResourceDetailsJson> getJsonResources() {
		return resources;
	}

	public void setJsonResources(Collection<ResourceDetailsJson> resources) {
		this.resources = resources;
	}

	
	@Override
	public ResourceSet addResourceSet(ResourceDetails resourceDetails)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceSet addResourceSet(ResourceSet resourceSet)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceResource(ResourceDetails resourceDetails)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeResource(ResourceDetails resourceDetails)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
