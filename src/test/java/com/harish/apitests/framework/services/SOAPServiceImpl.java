package com.harish.apitests.framework.services;

import java.net.MalformedURLException;
import java.util.Map;

import javax.xml.soap.SOAPException;

import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.model.SoapResponse;
import org.springframework.stereotype.Component;

@Component
public class SOAPServiceImpl implements SOAPService{
	
	private SoapRequestBuilder requestBuilder;
	private SoapClient soapClient;
	private RestClient restClient;
	@Override
	public SoapRequest requestFromFile(String requestFile, Map<String, String> requestParams) {
		// TODO Auto-generated method stub
		return requestBuilder.fromFile(requestFile, requestParams);
	}
	@Override
	public SoapResponse sendToServer(SoapRequest request) throws MalformedURLException, SOAPException {
		// TODO Auto-generated method stub
		return soapClient.send(request);
	}
	@Override
	public void enableFeatureToggle(String featureName) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void disableFeatureToggle(String featureName) {
		// TODO Auto-generated method stub
		
	}
}
