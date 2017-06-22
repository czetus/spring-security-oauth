package org.springframework.security.oauth.examples.tonr.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.couchbase.client.deps.io.netty.handler.codec.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class HubicController {

	private String clientId = "api_hubic_LMifNw9mzgbnTot06kPSDj45oHdNDBky";
	private String clientSecret = "3x107Y9rRcqNlYHYRgNVADvROXd2BWlp6C04bRiMWRpCZkhKMsBRXRC0YJZ0g1gv";
	private String base64 = "YXBpX2h1YmljX0xNaWZOdzltemdiblRvdDA2a1BTRGo0NW9IZE5EQmt5OjN4MTA3WTlyUmNxTmxZSFlSZ05WQUR2Uk9YZDJCV2xwNkMwNGJSaU1XUnBDWmtoS01zQlJYUkMwWUpaMGcxZ3Y=";
	private String redirectURI = "http://localhost/tonr2/about/";
	private String grant_Type = "authorization_code";
	
	

	private OAuth2RestOperations hubicRestTemplate;
	@Autowired
	private OAuth2ProtectedResourceDetails hubic;
	
	
	@RequestMapping("/hubic/get")
	public void getSmth(@RequestHeader HttpHeaders header) throws  RestClientException{		
//		String accesToken = "rOEfIxhXzwJk7am3oAEhIGcVppkg7QsDJBBdxPfv18mbmxaxusw2SPsA8Wkcv7Dh";
//	    DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accesToken);
//	    token.setTokenType(DefaultOAuth2AccessToken.BEARER_TYPE);    
//	    hubicRestTemplate.getOAuth2ClientContext().setAccessToken(token); 	
		for(Map.Entry<String,List<String>> entry : header.entrySet()){
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

		
			
				    
//					HttpHeaders headers = new HttpHeaders();
//					//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED));
//					headers.add("Accept", "*/*");
//					headers.add("Authorization", String.format("Bearer %s", accesToken));
//					headers.add("Content-Type", "application/json");
				//	headers.add("Authorization Header", "Bearer 1496947268BtJDDBIZeYmOykMA4nK1UCMcf3xb2zgX6QX9PzBzRLczW7UT5x5FKOL0DuEx3yQn");
//					HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			
//			 System.out.println(hubicRestTemplate.exchange("https://api.hubic.com/1.0/account",HttpMethod.GET,entity, String.class).getBody());
		if(hubicRestTemplate.getAccessToken() == null){
			
			//https://api.hubic.com/oauth/authorize/?client_id=api_hubic_LMifNw9mzgbnTot06kPSDj45oHdNDBky&duration=permanent&redirect_uri=http://localhost/tonr2/about/&response_type=code&scope=usage.r,account.r,getAllLinks.r,credentials.r,links.drw&state=yF9zi2		
			
		}
			System.out.println(hubicRestTemplate.getForEntity("https://api.hubic.com/1.0/account", String.class));
			//System.out.println(hubicRestTemplate.getAccessToken().toString());
	}
	
	@RequestMapping(value="/about{code}")
	public void small(@RequestParam("code") String code,@RequestParam("state") String state){
		String requestBody;
		System.out.println(code);
		System.out.println(state);
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", code);
		body.add("redirect_uri", redirectURI);
		body.add("grant_type", grant_Type);
		//map.add("state",state);
		
		//requestBody = "code="+code+"&redirect_uri="+redirectURI+"&grant_type="+grant_Type;
			
		System.out.println("Parametry do wysłania" + body);
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.ALL);
		mediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept();; /*("Accept", "application/json"); */
		headers.setAccept(mediaTypes);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Basic " + base64); //String.format("Basic %s", base64));
		
//		HttpEntity<String> request = new HttpEntity<String> (requestBody, headers);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>> (body, headers);
		HttpEntity<String> node = hubicRestTemplate.postForEntity("https://api.hubic.com/oauth/token/", request, String.class);
//		System.out.println(hubicRestTemplate.getForEntity("https://api.hubic.com/1.0/account", String.class));
		System.out.println(node.getBody());
		
	}
	
	@RequestMapping(value="/about/",method=RequestMethod.POST)
	void shouldReturnAccesToken(HttpServletRequest request){
		System.out.println("content type-" + request.getContentType());
		System.out.println("URI -" + request.toString());
		System.out.println("URI -" + request.getRequestURI());
		
	}
	@RequestMapping(value="/hubic/getAllLinks")
	public void giveAllLinksFromHubic(){
		String response = hubicRestTemplate.getForObject("https://api.hubic.com/1.0/account/getAllLinks", String.class);
		
		System.out.println("Links: "+ response);
	}
	
	@RequestMapping(value="/hubic/info")
	public void giveInfo(){
		String response = hubicRestTemplate.getForObject("https://api.hubic.com/1.0/info", String.class);
		
		System.out.println("Info: "+ response);
	}	
	
	@RequestMapping(value="/hubic/getToken")
	public void giveToken(){
		String token = hubicRestTemplate.getAccessToken().getValue() + "Wygasa=" + hubicRestTemplate.getAccessToken().getExpiration() + " refreshToken= "+ 
	    hubicRestTemplate.getAccessToken().getRefreshToken().getValue();
		
		System.out.println("Token: "+ token);
	}
	
	
	public void setHubicRestTemplate(OAuth2RestOperations template){
		this.hubicRestTemplate = template;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
		public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	public void setGrant_Type(String grant_Type) {
		this.grant_Type = grant_Type;
	}	
}
