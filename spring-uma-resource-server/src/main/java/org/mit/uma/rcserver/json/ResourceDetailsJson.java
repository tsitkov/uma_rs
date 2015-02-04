package org.mit.uma.rcserver.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceDetailsJson implements ResourceDetails {

	@JsonProperty("composite")	
	private boolean isResourceSet;
	
	@JsonProperty("name")	
	private String resourceName;

	@JsonProperty("description")		
	private String resourceDescription;
	
	@JsonProperty("uri")			
	private String resourceUri;
	
	@JsonProperty("action")
	private String action;

	public ResourceDetailsJson() {	
	}

	public ResourceDetailsJson(ResourceDetails resourceDetails) {
		this.isResourceSet = resourceDetails.isResourceSet();
		this.resourceName = resourceDetails.getResourceName();
		this.resourceDescription = resourceDetails.getResourceDescription();
		this.resourceUri = resourceDetails.getResourceUri();
	}
	
	@JsonIgnore	
	@Override
	public Long getResourceId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResourceId(Long parentId) {
	}

	@JsonIgnore
	@Override
	public ResourceSet getParent() {
		return null;
	}

	@Override
	public String getResourceName() {
		return resourceName;
	}

	@Override
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@JsonIgnore
	@Override
	public String getResourcePath() {
		return null;
	}

	@Override
	public void setResourcePath(String resourcePath) {
	}

	@Override
	public String getResourceDescription() {
		return resourceDescription;
	}

	@Override
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	@Override
	public String getResourceUri() {
		return resourceUri;
	}

	@Override
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	@JsonIgnore
	@Override
	public boolean isResourceSet() {
		// TODO Auto-generated method stub
		return isResourceSet;
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
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
}
