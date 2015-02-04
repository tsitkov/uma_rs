package org.mit.uma.rcserver.springimpl.client;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
import java.util.List;
import java.util.Map;

//import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mit.uma.rcserver.client.HttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.mit.uma.rcserver.json.AuthServerApiJson;
import org.mit.uma.rcserver.json.JsonUtil;
import org.mit.uma.rcserver.json.PatResponse;
import org.mit.uma.rcserver.json.RegistrationRequest;
import org.mit.uma.rcserver.json.RegistrationResponse;
import org.mit.uma.rcserver.json.ResourcePermissionsResponse;
import org.mit.uma.rcserver.json.ResourceRegistrationResponse;

@Service
public class SpringHttpClient implements HttpClient {

	static final Logger logger = Logger.getLogger(SpringHttpClient.class);  

	public SpringHttpClient() {
		
	}
	
	@Override
	public AuthServerApiJson apiRequest(String apiEndpoint) {
		/*
		 * This is very simple RPC call and it only requires URI of authorization
		 * server and API end-point.
		 */		
		logger.setLevel(Level.DEBUG);
		logger.debug("API Endpoint " + apiEndpoint + "\n");
		/*
		 * If everything goes well, the RestTemplate will return a JSON object with required info.
		 */
		RestTemplate restTemplate = new RestTemplate();
		AuthServerApiJson api = restTemplate.getForObject(apiEndpoint, AuthServerApiJson.class);
		// log response from the server
		logger.debug("API Response :\n" + JsonUtil.toString(api) + "\n");
		
		return api;
	}

	@Override
	public RegistrationResponse registrationRequest(String registrationEndpoint, RegistrationRequest request) {
		logger.setLevel(Level.DEBUG);
		logger.debug("Registration Endpoint " + registrationEndpoint + "\n");

		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    RestTemplate template = new RestTemplate();
	    // log registration request
		logger.debug("requestAsString :" + JsonUtil.toString(request) + "\n");

	    HttpEntity<RegistrationRequest> request_entity = new HttpEntity<RegistrationRequest>(request,headers);
		ResponseEntity<RegistrationResponse> result = template.postForEntity(registrationEndpoint, request_entity, RegistrationResponse.class);
		RegistrationResponse response = result.getBody();

		System.out.print("issued: " + response.getClientIdIssuedAt() + "\n");		
		// log registration response
		logger.debug("responseAsString :" + JsonUtil.toString(response) + "\n");

		return response;
	}

	@Override
	public PatResponse patRequest(String tokenEndpoint, Map<String, String> request) {

		logger.setLevel(Level.DEBUG);
		logger.debug("I am in patRequest");
		
		assert(!StringUtils.isEmpty(tokenEndpoint));

		PatResponse response = urlEncodedRequest(tokenEndpoint, request, HttpClient.AuthorizationType.BASIC, 
				HttpMethod.POST, PatResponse.class);
				
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	    headers.setAccept(Arrays.asList(MediaType.ALL));
//	    
//	    String credentials = request.get("credentials");
//	    assert(!StringUtils.isEmpty(credentials));
//	    logger.debug("credentials:  " + credentials + "\n");
//		headers.add("Authorization", "Basic " + _base64Encode(credentials));
//		
//		logger.debug("Http Headers: " + headers.toString());
//
//		MultiValueMap<String, String> variables = new LinkedMultiValueMap<String, String>();
//
//		String grantType = request.get("grant_type");
//		assert(!StringUtils.isEmpty(grantType));
//		variables.add("grant_type", grantType);
//        
//		String code = request.get("code");
//		assert(!StringUtils.isEmpty(code));		
//		variables.add("code", code);
//        
//		String redirectUri = request.get("redirect_uri");
//		assert(!StringUtils.isEmpty(redirectUri));
//        variables.add("redirect_uri", redirectUri);
//        
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(variables, headers);
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//
//		messageConverters.add(new FormHttpMessageConverter());
//		messageConverters.add(new MappingJackson2HttpMessageConverter());
//		RestTemplate client = new RestTemplate();
//		client.setMessageConverters(messageConverters);
//        ResponseEntity<PatResponse> response = client.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, PatResponse.class, variables);
		
        logger.debug("AM server response:\n" + JsonUtil.toString(response) + "\n");
                
//        return response.getBody();
        
        return response;
	}

	@Override
	public ResourceRegistrationResponse resourceRegistrationRequest(String resourceRegistrationEndpoint, Map<String, String> request) {

		logger.setLevel(Level.DEBUG);
		logger.debug("I am in resourceRegistrationRequest");
		
		assert(!StringUtils.isEmpty(resourceRegistrationEndpoint));

		ResourceRegistrationResponse response = urlEncodedRequest(resourceRegistrationEndpoint, request, HttpClient.AuthorizationType.BEARER, 
				HttpMethod.PUT, ResourceRegistrationResponse.class);
		
		
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	    headers.setAccept(Arrays.asList(MediaType.ALL));
//	    
//	    String credentials = request.get("credentials");
//	    assert(!StringUtils.isEmpty(credentials));
//	    logger.debug("credentials:  " + credentials + "\n");
//		headers.add("Authorization", "Basic " + _base64Encode(credentials));
//		
//		logger.debug("Http Headers: " + headers.toString());
//
//		MultiValueMap<String, String> variables = new LinkedMultiValueMap<String, String>();
//
//		String scope = request.get("scope");
//		assert(!StringUtils.isEmpty(scope));
//		variables.add("scope", scope);
//		
//		String name = request.get("name");
//		assert(!StringUtils.isEmpty(name));
//		variables.add("name", name);
//
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(variables, headers);
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//
//		messageConverters.add(new FormHttpMessageConverter());
//		messageConverters.add(new MappingJackson2HttpMessageConverter());
//		RestTemplate client = new RestTemplate();
//		client.setMessageConverters(messageConverters);
//        ResponseEntity<ResourceRegistrationResponse> response = client.exchange(resourceRegistrationEndpoint, 
//        		HttpMethod.POST, requestEntity, ResourceRegistrationResponse.class, variables);
//		
//        logger.debug("AM server response to \"resourceRegistrationRequest\":\n" + JsonUtil.toString(response.getBody()) + "\n");
//                
//        return response.getBody();

		logger.debug("AM server response to \"resourceRegistrationRequest\":\n" + JsonUtil.toString(response) + "\n");

		return response;
	}
	
	@Override
	public ResourcePermissionsResponse resourcePermissionsRequest(
			String permissionEndpoint, Map<String, String> request) {
		logger.setLevel(Level.DEBUG);
		logger.debug("I am in resource permission request");
		
		assert(!StringUtils.isEmpty(permissionEndpoint));
		
		ResourcePermissionsResponse response = urlEncodedRequest(permissionEndpoint, request, HttpClient.AuthorizationType.BEARER, 
						HttpMethod.POST, ResourcePermissionsResponse.class);
		
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//	    headers.setAccept(Arrays.asList(MediaType.ALL));
//	    
//	    String credentials = request.get("credentials");
//	    assert(!StringUtils.isEmpty(credentials));
//	    logger.debug("credentials:  " + credentials + "\n");
//		headers.add("Authorization", "Basic " + _base64Encode(credentials));
//		
//		logger.debug("Http Headers: " + headers.toString());
//
//		MultiValueMap<String, String> variables = new LinkedMultiValueMap<String, String>();
//
//		String scopes = request.get("scopes");
//		assert(!StringUtils.isEmpty(scopes));
//		variables.add("scope", scopes);
//		
//		String resourceSetId = request.get("resource_set_id");
//		assert(!StringUtils.isEmpty(resourceSetId));
//		variables.add("resource_set_id", resourceSetId);
//
//		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(variables, headers);
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//
//		messageConverters.add(new FormHttpMessageConverter());
//		messageConverters.add(new MappingJackson2HttpMessageConverter());
//		RestTemplate client = new RestTemplate();
//		client.setMessageConverters(messageConverters);
//        ResponseEntity<ResourcePermissionsResponse> response = client.exchange(permissionEndpoint, 
//        		HttpMethod.POST, requestEntity, ResourcePermissionsResponse.class, variables);
		
        logger.debug("AM server response to \"resourcePermissionsRequest\":\n" + JsonUtil.toString(response) + "\n");
                
        return response;
	}

	protected <T> T urlEncodedRequest(String endpoint, Map<String,String> request,
			HttpClient.AuthorizationType authorizationType, HttpMethod method, Class<T> responseType) {

		logger.setLevel(Level.DEBUG);

		assert(!StringUtils.isEmpty(endpoint));
		assert(authorizationType != null);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.setAccept(Arrays.asList(MediaType.ALL));
	    
	    String credentials = request.get("credentials");
	    assert(!StringUtils.isEmpty(credentials));
	    logger.debug("credentials:  " + credentials + "\n");
//		headers.add("Authorization", "Basic " + _base64Encode(credentials));
		if (authorizationType == HttpClient.AuthorizationType.BASIC) {
			headers.add("Authorization", authorizationType.value + " " + _base64Encode(credentials));
		} else {
			headers.add("Authorization", authorizationType.value + " " + credentials);			
		}
		logger.debug("Http Headers: " + headers.toString());

		MultiValueMap<String, String> variables = new LinkedMultiValueMap<String, String>();
		for( Map.Entry<String, String> entry : request.entrySet()) {
			String key = entry.getKey();
			if (key.equals("credentials")) {
				continue;
			}
			String value = entry.getValue();
			assert(!StringUtils.isEmpty(value));
			variables.add(entry.getKey(), value);
		}
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(variables, headers);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		RestTemplate client = new RestTemplate();
		client.setMessageConverters(messageConverters);
        ResponseEntity<T> response = client.exchange(endpoint, method, requestEntity, responseType, variables);
                
        return response.getBody();

	}
	
	protected String _base64Encode(String msg) {
		byte[] encodedAuth = Base64.encodeBase64(msg.getBytes());
		return new String(encodedAuth);
	}


}
