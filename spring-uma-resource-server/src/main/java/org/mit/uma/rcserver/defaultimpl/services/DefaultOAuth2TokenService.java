package org.mit.uma.rcserver.defaultimpl.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mit.uma.rcserver.services.OAuth2TokenService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Service;

@Service("defaultOAuth2TokenService")
public class DefaultOAuth2TokenService implements OAuth2TokenService {

	@Override
	public OAuth2AccessToken createAccessToken(
			OAuth2Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("In createAccessToken");
		
		if (authentication != null && authentication.getOAuth2Request() != null) {

			return null;
		} else {
			throw new AuthenticationCredentialsNotFoundException("No authentication credentials found");
		}
	}

	@Override
	public OAuth2AccessToken refreshAccessToken(String refreshToken,
			TokenRequest tokenRequest) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("In refreshAccessToken");

		return null;
	}

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		// TODO Auto-generated method stub
		System.out.println("In loadAuthentication");
		System.out.println("token=" + accessToken);
		
		Map<String, String> authorizationParameters = new HashMap<String,String>();
		String clientId = "test-client";
		Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<SimpleGrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
		Set<String> scope = null;
		
		OAuth2Request clientAuth = new OAuth2Request(authorizationParameters, clientId, grantedAuthorities, 
													 true, scope, null, null, null, null);
		OAuth2Authentication authentication = new OAuth2Authentication(clientAuth, null);
		
		return authentication;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessTokenValue) {
		// TODO Auto-generated method stub
		System.out.println("In readAccessToken");

		return null;
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		System.out.println("In getAccessToken");
		

		return null;
	}

}
