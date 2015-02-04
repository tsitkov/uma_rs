package org.mit.uma.rcserver.services;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mit.uma.rcserver.json.PatResponse;
import org.mit.uma.rcserver.models.UserDetails;

public interface AuthorizationService {
	
	void processRegistrationRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
	void processLoginRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
	PatResponse processPatRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;
	void processResourceRequest(UserDetails user, String resourceSetpath, Set<String> scopes, String rpt);
}
