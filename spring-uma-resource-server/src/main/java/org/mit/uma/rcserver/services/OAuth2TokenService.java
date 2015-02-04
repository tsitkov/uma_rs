package org.mit.uma.rcserver.services;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public interface OAuth2TokenService extends AuthorizationServerTokenServices, ResourceServerTokenServices {

	@Override
	public OAuth2AccessToken readAccessToken(String accessTokenValue);
	
	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication);
	
}
