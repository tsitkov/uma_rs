package org.mit.uma.rcserver.server;

public interface RestfulServer {
	public void register(String usr, String authServerUri);
	public void getResorceSetContent(String usr, String ResourceSetUri);
}
