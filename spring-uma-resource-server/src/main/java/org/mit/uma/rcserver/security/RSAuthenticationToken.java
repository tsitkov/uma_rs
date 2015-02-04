package org.mit.uma.rcserver.security;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class RSAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 22100073066377804L;

//	private final ImmutableMap<String, String> principal;
	private final Map<String,String> principal;
	private final String accessTokenValue; // string representation of the access token
	private final String refreshTokenValue; // string representation of the refresh token
	private final String userId; // user Id issued by resource server 
	private final String issuer; // issuer URL (parsed from the id token)
	private final String sub; // user id (parsed from the id token)
	private final Long rootId; // root resource set associated with this token-user 

	/**
	 * Constructs OIDCAuthenticationToken with a full set of authorities, marking this as authenticated.
	 * 
	 * Set to authenticated.
	 * 
	 * Constructs a Principal out of the subject and issuer.
	 * @param subject
	 * @param authorities
	 * @param principal
	 * @param idToken
	 */
	public RSAuthenticationToken(String userId, String subject, String issuer,
			Collection<? extends GrantedAuthority> authorities,
			String accessTokenValue, String refreshTokenValue, Long rootId) {

		super(authorities);

		this.principal = setPrincipal(subject, issuer);
		this.userId = userId;
		this.sub = subject;
		this.issuer = issuer;
		this.accessTokenValue = accessTokenValue;
		this.refreshTokenValue = refreshTokenValue;
		this.rootId = rootId;
		
		setAuthenticated(true);
	}

	/**
	 * Constructs OIDCAuthenticationToken for use as a data shuttle from the filter to the auth provider.
	 * 
	 * Set to not-authenticated.
	 * 
	 * Constructs a Principal out of the subject and issuer.
	 * @param sub
	 * @param idToken
	 */
	public RSAuthenticationToken(String userId, String subject, String issuer,
			String accessTokenValue, String refreshTokenValue) {

		super(new ArrayList<GrantedAuthority>(0));

		this.principal = this.setPrincipal(subject, issuer);
		this.userId = userId;
		this.sub = subject;
		this.issuer = issuer;
		this.accessTokenValue = accessTokenValue;
		this.refreshTokenValue = refreshTokenValue;
		this.rootId = -1L;

		setAuthenticated(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return accessTokenValue;
	}

	/**
	 * Get the principal of this object, an immutable map of the subject and issuer.
	 */
	@Override
	public Object getPrincipal() {
		return principal;
	}

	public String getSub() {
		return sub;
	}

	/**
	 * @return the accessTokenValue
	 */
	public String getAccessTokenValue() {
		return accessTokenValue;
	}

	/**
	 * @return the refreshTokenValue
	 */
	public String getRefreshTokenValue() {
		return refreshTokenValue;
	}

	/**
	 * @return the serverConfiguration
	 */
//	public AuthServerDetails getServerConfiguration() {
//		return user.getAuthServer();
//	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @return the userInfo
	 */
//	public UserDetails getUserInfo() {
//		return user;
//	}

	public String getUserId() {
		return userId;
	}
	
	public Long getRootId() {
		return rootId;
	}

	private Map<String,String> setPrincipal(String subject, String issuer) {
		Map<String,String> principal = new HashMap<String,String>();
		principal.put("subject", subject);
		principal.put("issuer", issuer);
		
		return Collections.unmodifiableMap(principal);
	}
	
}
