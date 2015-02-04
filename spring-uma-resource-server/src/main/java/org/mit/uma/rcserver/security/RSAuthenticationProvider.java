package org.mit.uma.rcserver.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.util.StringUtils;
import org.mit.uma.rcserver.client.HttpClient;
import org.mit.uma.rcserver.json.ResourceRegistrationResponse;
import org.mit.uma.rcserver.json.ResourcePermissionsResponse;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.ResourceSet;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.services.ResourceAccessService;
import org.mit.uma.rcserver.services.UserDetailsService;


public class RSAuthenticationProvider implements AuthenticationProvider {

	//private UserInfoFetcher userInfoFetcher = new UserInfoFetcher();

	@Autowired
	DataService dataService;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	HttpClient httpClient;

	@Autowired
	ResourceAccessService resourceAccessService;
	
	private GrantedAuthoritiesMapper authoritiesMapper = new NamedAdminAuthoritiesMapper();

	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException {
		System.out.println("In RSAuthenticationProvider:authenticate");

		if (!supports(authentication.getClass())) {
			return null;
		}

		if (authentication instanceof RSAuthenticationToken) {

			RSAuthenticationToken token = (RSAuthenticationToken) authentication;

			System.out.println(token.getSub() + token.getIssuer());
			
			String issuer = token.getIssuer();
			String subject = token.getSub();
			String userId = token.getUserId();

			UserDetails user = null;
			AuthServerDetails authServer = null;
			if (!StringUtils.isEmpty(userId)) {
				user = userDetailsService.getUserDetails(token.getUserId());
				authServer = user.getAuthServer();				
			}

			if (userId == null) {
				userId = "anonymous";
			}

			if (subject == null) {
				subject = userId;
			}
			Collection<SubjectIssuerGrantedAuthority> authorities = new HashSet<SubjectIssuerGrantedAuthority>(Arrays.asList(new SubjectIssuerGrantedAuthority(subject, issuer)));
			
			if (user != null && user.getId() != null) {
				/* 
				 * Here we need:
				 * - extract userInfo from authorization server
				 * - if this is a new user we need to create root resource set for her, if not
				 *   we need to get full set of permissions for root resource set. 
				 */
				ResourceSet rootResourceSet = dataService.getRootResourceSet(user.getId(), true);
				if (rootResourceSet.getResourceId() == null) {
					// a new user
					rootResourceSet.setOwnerId(user.getId());
					rootResourceSet.setResourceDescription("home resource set for user " + user.getUserId());
					rootResourceSet.setResourceName(userId);
					rootResourceSet.setResourcePath("resource/" + userId);
					dataService.saveResource(rootResourceSet);
					// protect resource here
					try {
						rootResourceSet = resourceAccessService.protect(rootResourceSet, user);					
					} catch (Exception e) {
						throw new AuthenticationServiceException("failed to register root resource set with authorization server");
					}
					// save resource
					dataService.saveResource(rootResourceSet);
				}

				// Now we need to check protection status of root.
				try {
					ResourcePermissionsResponse ticket = resourceAccessService.getTicket(rootResourceSet, user);		
					assert(ticket.getTicket() != null);
				} catch (Exception e ) {
					throw new AuthenticationServiceException(e.getMessage());
				}
				
//				Map<String,String> request = new HashMap<String,String>();
//				request.put("resource_set_id", rootResourceSet.getProtectionId());
//				request.put("scopes", rootResourceSet.getDefaultScopes());
//				request.put("credentials", user.getAccessToken());
//				String resourcePermissionsEndpoint = authServer.getResourcePermissionsEndpoint();
//				ResourcePermissionsResponse ticket = httpClient.resourcePermissionsRequest(resourcePermissionsEndpoint, request);
//				if (ticket.getTicket() == null) {
//					throw new AuthenticationServiceException("Failed to obtain root resource set permissions for user " + userId);
//				}
								
				/*
				if (!StringUtils.isEmpty(str)(userInfo.getSub()) && !userInfo.getSub().equals(token.getSub())) {
					// the userinfo came back and the user_id fields don't match what was in the id_token
					throw new UsernameNotFoundException("user_id mismatch between id_token and user_info call: " + token.getSub() + " / " + userInfo.getSub());
				}
				 */
			}
			
			Long rootId = 0L;
			return new RSAuthenticationToken(token.getUserId(), subject, issuer,
					authoritiesMapper.mapAuthorities(authorities),
					token.getAccessTokenValue(), token.getRefreshTokenValue(), rootId);
		}

		return null;
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return RSAuthenticationToken.class.isAssignableFrom(authentication);
	}
			
}
