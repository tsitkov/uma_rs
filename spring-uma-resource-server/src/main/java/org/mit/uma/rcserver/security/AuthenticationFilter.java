package org.mit.uma.rcserver.security;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
//import java.math.BigInteger;
//import java.net.URI;
//import java.security.SecureRandom;
//import java.text.ParseException;
//import java.util.Date;
//import java.util.Map;


import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;

import org.mit.uma.rcserver.config.Configurator;
import org.mit.uma.rcserver.json.PatResponse;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.services.AuthorizationService;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.client.ClientHttpRequest;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private Configurator config;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private DataService dataService;

	// private helpers to handle target link URLs
	private TargetLinkURIAuthenticationSuccessHandler targetSuccessHandler = new TargetLinkURIAuthenticationSuccessHandler();
//	private TargetLinkURIChecker deepLinkFilter;

//	protected int httpSocketTimeout = HTTP_SOCKET_TIMEOUT;
	protected static final String TARGET_SESSION_VARIABLE = "target";
	protected static final String FILTER_PROCESSES_URL = "/openid_connect_login";
	protected static final String DEFAULT_URL = "/login";
	
	/**
	 * OpenIdConnectAuthenticationFilter constructor
	 */
	protected AuthenticationFilter() {
		super(FILTER_PROCESSES_URL);
		
		System.out.print("in filter\n");
		
		targetSuccessHandler.passthrough = super.getSuccessHandler();
		super.setAuthenticationSuccessHandler(targetSuccessHandler);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		System.out.print("*****In attempt\n***");
		
		String error = request.getParameter("error");
		if (error != null && error.length()>0) { 
			// there's an error coming back from the server, need to handle this
			handleError(request, response);
			return null; // no auth, response is sent to display page or something
		}
		
		String code = request.getParameter("code");
		if (code != null && code.length() > 0) {
			Authentication auth = handleAuthorizationCodeResponse(request, response);
			return auth;
		}
		
		// not an error, not a code, must be an initial login of some type
		handleAuthorizationRequest(request, response);

		return null; // no auth, response redirected to the server's Auth Endpoint (or possibly to the account chooser)
	}
	
/*
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("in doFilter");
		RegexRequestMatcher matcher = new RegexRequestMatcher("/resource/.*", null);
		return matcher.matches(request);
	}
*/
	
	protected void handleError(HttpServletRequest request, HttpServletResponse response) {
		System.out.print("This is error");
	}
	
	protected Authentication handleAuthorizationCodeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.print("We got a code!");
		HttpSession session = request.getSession();
		PatResponse token = authorizationService.processPatRequest(request, response);
		
//		UserDetails user = authorizationService.processPatRequest(request, response);
//		dataService.saveUserDetails(user);
		
//		Authentication authentication = validateToken(user, session);
		
		System.out.println("before validate");
		
		Authentication authentication = validateToken(token, session);

		return authentication;
	}

	protected void handleAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.print("In handleAuthorizationRequest\n");
		
		String source = request.getParameter("source");
		System.out.print("source: " + source + "\n");
		if (source == null || source.length() == 0) {
			// we need to redirect to local login page
			String redirectUrl = config.getLoginUrl();
			response.sendRedirect(redirectUrl);
		} else if(source.equals("user_login")) {
			// user is already registered. We need to authenticate her by checking his credentials
			// for access of her recourse root.
			authorizationService.processLoginRequest(request, response);
		} else if(source.equals("user_registration")) {
			// this is a new user
			authorizationService.processRegistrationRequest(request, response);
		} else {
			throw new AuthenticationServiceException("Unknown request source: " + source);
		}		
				
/*
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
	    String path = savedRequest.getRedirectUrl();
		System.out.print("path: " + path + "\n");

		if (StringUtils.isEmpty(path)) {
			// we need to redirect to local login page
			String redirectUrl = config.getLoginUrl();
			response.sendRedirect(redirectUrl);
		} else {
			// found path attribute
			authorizationService.processLoginRequest(request, response);			
		}
*/		
//		else if(source.equals("user_login")) {
//			// user is already registered. We need to authenticate her by checking his credentials
//			// for access of her recourse root.
//			authorizationService.processLoginRequest(request, response);			
//		} else if(source.equals("user_registration")) {
//			// this is a new user
//			authorizationService.processRegistrationRequest(request, response);
//		} else {
//			throw new AuthenticationServiceException("Unknown request source: " + source);
//		}

	}
	
	@Override
	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		System.out.print("*****In setAuthenticationSuccessHandler***");
		targetSuccessHandler.passthrough = successHandler;
		super.setAuthenticationSuccessHandler(targetSuccessHandler);
	}
	
	//private Authentication validateToken(UserDetails user, HttpSession session) {
	private Authentication validateToken(PatResponse token, HttpSession session) {
		
//		String accessToken = user.getAccessToken();
//		String refreshToken = user.getRefreshToken();
//		AuthServerDetails authServer = user.getAuthServer();

		String accessToken = token.getAccessToken();
		String refreshToken = token.getRefreshToken();

		UserDetails user = null;
		AuthServerDetails authServer = null;
		Object userName = session.getAttribute("user_name");
		if (!StringUtils.isEmpty(userName)) {
			user = userDetailsService.getUserDetails(userName.toString());
			authServer = user.getAuthServer();
		} else {
			Object issuer =  session.getAttribute("issuer");
			if(StringUtils.isEmpty(issuer)) {
				throw new AuthenticationServiceException("'issuer' attribute is not found");			
			}
			authServer = dataService.getAuthServerDetails(issuer.toString());
		}

		try {
			JWT jwtToken = JWTParser.parse(accessToken);
			
			// validate our ID Token over a number of tests
			ReadOnlyJWTClaimsSet idClaims = jwtToken.getJWTClaimsSet();
			System.out.print(idClaims.toJSONObject().toJSONString());

			// check the signature
//			JwtSigningAndValidationService jwtValidator = null;

			Algorithm tokenAlg = jwtToken.getHeader().getAlgorithm();
			System.out.print("algo " + tokenAlg.toJSONString() + "\n");
		
//			Algorithm clientAlg = clientConfig.getIdTokenSignedResponseAlg();
		
			if (jwtToken instanceof PlainJWT) {
			
				System.out.print("plain\n");
				//if (clientAlg == null) {
				//	throw new AuthenticationServiceException("Unsigned ID tokens can only be used if explicitly configured in client.");
				//}
			
				if (tokenAlg != null && !tokenAlg.equals(JWSAlgorithm.NONE)) {
					throw new AuthenticationServiceException("Unsigned token received, expected signature with " + tokenAlg);
				}
			
			} else if (jwtToken instanceof SignedJWT) {

				System.out.print("plain\n");
		
				SignedJWT signedIdToken = (SignedJWT)jwtToken;
			
				if (tokenAlg.equals(JWSAlgorithm.HS256)
						|| tokenAlg.equals(JWSAlgorithm.HS384)
						|| tokenAlg.equals(JWSAlgorithm.HS512)) {
				
					// generate one based on client secret
					//jwtValidator = symmetricCacheService.getSymmetricValidtor(clientConfig.getClient());
				} else {
					// otherwise load from the server's public key
					//jwtValidator = validationServices.getValidator(serverConfig.getJwksUri());
				}
			
				//if (jwtValidator != null) {
				//	if(!jwtValidator.validateSignature(signedIdToken)) {
				//		throw new AuthenticationServiceException("Signature validation failed");
				//	}
				//} else {
				//	logger.error("No validation service found. Skipping signature validation");
				//	throw new AuthenticationServiceException("Unable to find an appropriate signature validator for ID Token.");
				//}
			} // TODO: encrypted id tokens
			
			// check the issuer
			if (idClaims.getIssuer() == null) {
				throw new AuthenticationServiceException("Id Token Issuer is null");
			} else if (!idClaims.getIssuer().equals(authServer.getIssuer())){
				throw new AuthenticationServiceException("Issuers do not match, expected " + authServer.getIssuer() + " got " + idClaims.getIssuer());
			}

			// check expiration
			Long timeSkewAllowance = 60L;
			if (idClaims.getExpirationTime() == null) {
				throw new AuthenticationServiceException("Id Token does not have required expiration claim");
			} else {
			// it's not null, see if it's expired
				Date now = new Date(System.currentTimeMillis() - (timeSkewAllowance * 1000));
				if (now.after(idClaims.getExpirationTime())) {
					throw new AuthenticationServiceException("Id Token is expired: " + idClaims.getExpirationTime());
				}
			}

			// check not before
			if (idClaims.getNotBeforeTime() != null) {
				Date now = new Date(System.currentTimeMillis() + (timeSkewAllowance * 1000));
				if (now.before(idClaims.getNotBeforeTime())){
					throw new AuthenticationServiceException("Id Token not valid untill: " + idClaims.getNotBeforeTime());
				}
			}

			// check issued at
			if (idClaims.getIssueTime() == null) {
				throw new AuthenticationServiceException("Id Token does not have required issued-at claim");
			} else {
				// since it's not null, see if it was issued in the future
				Date now = new Date(System.currentTimeMillis() + (timeSkewAllowance * 1000));
				if (now.before(idClaims.getIssueTime())) {
					throw new AuthenticationServiceException("Id Token was issued in the future: " + idClaims.getIssueTime());
				}
			}

			// check audience
			if (idClaims.getAudience() == null) {
				throw new AuthenticationServiceException("Id token audience is null");
			} else if (!idClaims.getAudience().contains(authServer.getClientId())) {
				throw new AuthenticationServiceException("Audience does not match, expected " + authServer.getClientId() + " got " + idClaims.getAudience());
			}

			// compare the nonce to our stored claim
			String nonce = idClaims.getStringClaim("nonce");
			if (!StringUtils.isEmpty(nonce) && session != null) {
				Object storedNonce = session.getAttribute("nonce");
				if (!StringUtils.isEmpty(storedNonce) && !nonce.equals(storedNonce.toString())) {
					logger.error("Possible replay attack detected! The comparison of the nonce in the returned "
						+ "ID Token to the session nonce failed. Expected " + storedNonce + " got " + nonce + ".");			
					throw new AuthenticationServiceException("Possible replay attack detected! The comparison of the nonce in the returned "
									+ "ID Token to the session " + "nonce" + " failed. Expected " + storedNonce.toString() + " got " + nonce + ".");
				}
			}

			// pull the subject (user id) out as a claim on the id_token
			String subject = idClaims.getSubject();
			System.out.println("subject=" + subject);
			
			String issuer = idClaims.getIssuer();
			String userId = null;
			// we successfully validated token
			if (user != null) {
				user.setAccessToken(accessToken);
				user.setRefreshToken(refreshToken);
				dataService.saveUserDetails(user);
				userId = user.getUserId();
			}
			
			// construct an AuthenticationToken and return a Authentication object w/the userId and the idToken
			RSAuthenticationToken authToken = new RSAuthenticationToken(userId, subject, issuer, accessToken, refreshToken);

			System.out.println("***");
			System.out.println((this.getAuthenticationManager() instanceof RSAuthenticationProvider));
			System.out.println("*** client");			
			System.out.println((this.getAuthenticationManager() instanceof ClientAuthenticationProvider));
			
			Authentication authentication = this.getAuthenticationManager().authenticate(authToken);
			return authentication;
			
		} catch (ParseException e) {
			throw new AuthenticationServiceException("Couldn't parse idToken: ", e);
		}
		
	}
	
	private static String getStoredSessionString(HttpSession session, String key) {
		Object o = session.getAttribute(key);
		if (o != null && o instanceof String) {
			return o.toString();
		} else {
			return null;
		}
	}
	
	protected class TargetLinkURIAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		private AuthenticationSuccessHandler passthrough;

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
						throws IOException, ServletException {

			HttpSession session = request.getSession();

			// check to see if we've got a target
			String target = getStoredSessionString(session, TARGET_SESSION_VARIABLE);			
			if (!StringUtils.isEmpty(target)) {
				session.removeAttribute(TARGET_SESSION_VARIABLE);

//				target = deepLinkFilter.filter(target);
				response.setContentType("application/json");
				response.sendRedirect(target);
			} else {
				// if the target was blank, use the default behavior here
				// passthrough.onAuthenticationSuccess(request, response, authentication);
				response.sendRedirect(DEFAULT_URL);
			}

		}

	}
}
