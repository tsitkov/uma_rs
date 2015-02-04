package org.mit.uma.rcserver.models;

import org.mit.uma.rcserver.models.ResourceSet;

public interface ResourceDetails {
	
	public Long getResourceId();
	public void setResourceId(Long parentId);
	
	public ResourceSet getParent();

	public Long getOwnerId();
	public void setOwnerId(Long ownerId);
	
	public String getResourceName();
	public void setResourceName(String resourceName);

	public String getResourcePath();
	public void setResourcePath(String resourcePath);
	
	public String getResourceDescription();
	public void setResourceDescription(String resourceDescription);
	
	public String getResourceUri();
	public void setResourceUri(String resourceUri);

	public boolean isResourceSet();
}
