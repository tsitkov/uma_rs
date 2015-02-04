package org.mit.uma.rcserver.services;

public interface ResourceManager {

	public boolean addResource(String userId, String uri);
	public boolean deleteResource(String userId, String uri);
	
}
