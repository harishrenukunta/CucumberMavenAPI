package com.harish.apitests.framework.services;

import static org.junit.Assert.assertTrue;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author harishrenukunta
 *
 */
@Slf4j
@AllArgsConstructor
public class RestClientImpl implements RestClient{
	
	private RestTemplate restTemplate;
	
	private ServiceUrl serviceUrl;
	
	
	
	/*(non-Javadoc)
	 * @see com.discover.inbound.service.RestClient#changeFeatureToggleState(String, boolean)
	 */
	@Override
	public void changeFeatureToggleState(String featureName, boolean newState) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(featureName))
			invokeRestCall(featureName, newState);
		else
			log.info("No REST call is made since featureName is empty");
		
	}
	
	private void invokeRestCall(final String featureName, boolean newState) {
		String url = getUri(featureName, newState);
		HttpEntity<String> requestEntity = requestEntity(null);
		
		ResponseEntity<String> rawResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class );
		
		log.info("REST returned:" + rawResponse);
		
		verifyNewStateOfFeatureOrThrowException(rawResponse.getBody(), featureName, newState);
 	}
	
	private void verifyNewStateOfFeatureOrThrowException(String response,String featureName, boolean newState ) {
		assertTrue("Response should contains:" + featureName, response.contains("\"feature\":\"" + featureName + "\""));
		assertTrue("Response should contains:" + newState, response.contains("\"enabled\":" + newState));
	}
	
	private HttpEntity<String> requestEntity(HttpHeaders headers){
		return new HttpEntity<>(null, headers);
	}
	
	private String getUri(String featureName, boolean newState) {
		String restServiceUrl = serviceUrl.restServerAddress();
		String uri = restServiceUrl + "/" + featureName + "/" + newState;
		log.info("Setting featureName: {}, newSate:{} at uri: {}", featureName, newState, restServiceUrl);
		log.info("REST address: {}", uri);
		
		return uri;
	}
	
	
	

}
