package org.mit.uma.rcserver.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.security.RSAuthenticationToken;
import org.mit.uma.rcserver.services.DataService;




//public class RSAuthenticationEntryPoint extends  OAuth2AuthenticationEntryPoint{
public class RSAuthenticationEntryPoint	implements AuthenticationEntryPoint, InitializingBean {	

	@Autowired
	DataService dataService;
	
	private String realmName;

    public void afterPropertiesSet() throws Exception {
        Assert.hasText(realmName, "realmName must be specified");
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

    	String resourcePath = request.getServletPath().substring(1);
    	ResourceSet resourceSet = dataService.getResourceSetByPath(resourcePath);
		UserDetails user  = dataService.getUserDetailsById(resourceSet.getOwnerId());
		    	
    	response.addHeader("as_url", user.getAuthServer().getIssuer());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

	
	
//	RSAuthenticationEntryPoint() {
//		super();
//	}
//
//	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//			throws IOException, ServletException {
//		
//		System.out.println("path=" + request.getServletPath());
//		
//		String issuer = "test-issuer";
//		RSAuthenticationToken authentication = new RSAuthenticationToken(null, null, issuer, null, null);
//		authException.setAuthentication(authentication);
//		System.out.println(authException.getClass().toString());
//		super.commence(request, response, authException);
//		
//	}
//
//	
//	@Override
//	protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
//		HttpHeaders headers = response.getHeaders();
//		
//		RSAuthenticationToken authentication = (RSAuthenticationToken) ((AuthenticationException) exception).getAuthentication();
//		
//		System.out.println("issuer" + authentication.getIssuer());
//		
//		System.out.println("Headers");
//		for (String key: headers.keySet()) {
//			System.out.println(key + "=" + headers.getFirst(key));
//		}
//		
//		String existing = null;
//		if (headers.containsKey("WWW-Authenticate")) {
////			existing = extractTypePrefix(headers.getFirst("WWW-Authenticate"));
//		}
//		
//		StringBuilder builder = new StringBuilder();
////		builder.append(typeName+" ");
////		builder.append("realm=\"" + realmName + "\"");
////		if (existing!=null) {
////			builder.append(", "+existing);
////		}
//		
//		HttpHeaders update = new HttpHeaders();
//		update.putAll(response.getHeaders());
//		update.set("WWW-Authenticate", builder.toString());
//		return new ResponseEntity<OAuth2Exception>(response.getBody(), update, response.getStatusCode());
//	}

}
