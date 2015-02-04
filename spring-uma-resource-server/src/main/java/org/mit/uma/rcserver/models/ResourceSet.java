package org.mit.uma.rcserver.models;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.mit.uma.rcserver.models.ResourceDetails;

public interface ResourceSet extends ResourceDetails {

	public enum DefaultScopes {
		READ,
		WRITE
	}
	
	public String getDefaultScopes();
			
	public String getProtectionId();
	public void setProtectionId(String protectionId);
	
	public String getProtectionRevision();
	public void setProtectionRevision(String revisionId);
	
//	public Set<? extends ResourceSet> getResourceSets();
	
//	public Set<? extends ResourceDetails> getResources();
	public Map<String, ? extends ResourceDetails> getResources();
	
	public ResourceDetails addResource(ResourceDetails resourceDetails) throws IOException;
	public ResourceSet addResourceSet(ResourceDetails resourceDetails) throws IOException;
	public ResourceSet addResourceSet(ResourceSet resourceSet) throws IOException;
	public void replaceResource(ResourceDetails resourceDetails) throws IOException;
	public void removeResource(ResourceDetails resourceDetails) throws IOException;
	
	public String toJson();
}
