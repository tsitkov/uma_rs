package org.mit.uma.rcserver.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class ClientAuthenticationProvider implements AuthenticationProvider {

	private GrantedAuthoritiesMapper authoritiesMapper = new NamedAdminAuthoritiesMapper();
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("in ClientAuthenticationProvider");
		
		if (authentication instanceof RSAuthenticationToken) {

			RSAuthenticationToken token = (RSAuthenticationToken) authentication;
			String subject = "test-subject";
			long rootId = 1;
			Collection<SubjectIssuerGrantedAuthority> authorities = new HashSet<SubjectIssuerGrantedAuthority>
				(Arrays.asList(new SubjectIssuerGrantedAuthority(subject, token.getIssuer())));

			return new RSAuthenticationToken(token.getUserId(), subject, token.getIssuer(),
					authoritiesMapper.mapAuthorities(authorities),
					token.getAccessTokenValue(), token.getRefreshTokenValue(), rootId);
		}
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return RSAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
