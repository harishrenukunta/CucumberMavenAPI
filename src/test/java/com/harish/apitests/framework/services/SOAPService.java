package com.harish.apitests.framework.services;

import java.net.MalformedURLException;
import java.util.Map;

import javax.xml.soap.SOAPException;

import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.model.SoapResponse;

public interface SOAPService {
	
	/***
	 * Load request from file
	 * 
	 * @param requestFile
	 * @param requestParams
	 * @return
	 */
	SoapRequest requestFromFile(final String requestFile, final Map<String, String> requestParams);
	
	
	SoapResponse sendToServer(SoapRequest request) throws MalformedURLException, SOAPException;
	
	void enableFeatureToggle(final String featureName);
	void disableFeatureToggle(final String featureName);
	

}
