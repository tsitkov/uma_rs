package org.mit.uma.rcserver.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.security.AuthenticationFilter.TargetLinkURIAuthenticationSuccessHandler;
import org.mit.uma.rcserver.services.AuthorizationService;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.util.StringUtils;

public class ClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


	@Autowired
	private DataService dataService;			

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthorizationService authorizationService;
		
	protected static RegexRequestMatcher matcher = new RegexRequestMatcher("/resource/.*", null);
	
	protected ClientAuthenticationFilter() {
		super(matcher);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// no-op - just allow filter chain to continue to token endpoint
			}
		});
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		System.out.println("in attemptAuthentication");
		String resourceSetPath = request.getServletPath().substring(1);

		System.out.println(resourceSetPath);		
		ResourceSet resourceSet = dataService.getResourceSetByPath(resourceSetPath);
		if (resourceSet == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String rpt = null;
		if (authentication != null && authentication.isAuthenticated()) {
			System.out.println("authenticated!");
			// here we act as a relying party (RP). Authentication context should
			// contain AAT that is used to get RPT 
			return authentication;
		} else {
			// Otherwise the request is coming from external RP.
			// In this case RPT must be provided in request header
			rpt = extractToken(request);
			if (StringUtils.isEmpty(rpt)) {
				throw new AuthenticationServiceException("Empty token");	
			}
		}
		// now we have RPT and have to ask authorization server for permission
		
		
		
//		String rpt = null;
//		String source = request.getParameter("source");
			
//		if(!StringUtils.isEmpty(code)) {
////			rpt = authorizationService.processRptRequest(request, response);
//		} else if (!StringUtils.isEmpty(source) && source.equals("rs-client")) {
//			// we are requested to be a relying party and get rpt from authorization server
//			authorizationService.processLoginRequest(request, response);
//			return null;
//		} else {
//			// The rpt must be provided in request header
//			String token = extractToken(request);
//			if (StringUtils.isEmpty(token)) {
//				throw new AuthenticationServiceException("Empty token");	
//			}
//		}
	
		if (!StringUtils.isEmpty(rpt)) {
			
			System.out.println("owner" + resourceSet.getOwnerId());
			
			UserDetails user = userDetailsService.getUserDetailsById(resourceSet.getOwnerId());			
			String issuer = user.getAuthServer().getIssuer();
			RSAuthenticationToken authToken = new RSAuthenticationToken(user.getUserId(), null, issuer, rpt, null);
			authentication = this.getAuthenticationManager().authenticate(authToken);
			
			System.out.println("passed=" + authentication.isAuthenticated());
			
			return authentication;
		} else {
			throw new AuthenticationServiceException("Empty token");	
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
	    final HttpServletResponse response = (HttpServletResponse) res;
	    String header = request.getHeader("WWW-Authenticate");

	    System.out.println("header=" + header);
	    
	    if (!StringUtils.isEmpty(header) && header.startsWith("Bearer ")) {
	      super.doFilter(request, response, chain);
	    } else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				super.doFilter(request, response, chain);
			} else {
				chain.doFilter(request, response);
			}
	    }
	  }

	private String extractToken(HttpServletRequest request) {
		String credentials = request.getHeader("WWW-Authenticate");
		String token = null;
		if (!StringUtils.isEmpty(credentials) && credentials.startsWith("Bearer ")) {
			String[] pair = credentials.split("\\s+");
			assert(pair.length == 2);
			token = pair[1];
		}
		return token;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}
	
}
