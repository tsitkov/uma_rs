package org.mit.uma.rcserver.security;

import org.springframework.security.core.AuthenticationException;
import org.mit.uma.rcserver.security.RSAuthenticationToken;


public class RSAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 22100073066377805L;

	private RSAuthenticationToken authentication;
	
	public RSAuthenticationException(String msg) {
		super(msg);
	}

	public void setAuthentication(RSAuthenticationToken authentication) {
		this.authentication = authentication;
	}
	
	public RSAuthenticationToken getAuthentication() {
		return authentication;
	}

	
}
