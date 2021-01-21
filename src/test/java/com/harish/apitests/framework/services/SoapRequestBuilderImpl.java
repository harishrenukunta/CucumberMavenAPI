package com.harish.apitests.framework.services;

import java.util.Map;

import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.utils.FileUtilities;

/**
 * 
 * @author harishrenukunta
 *
 */

public class SoapRequestBuilderImpl implements SoapRequestBuilder{

	private PropertyExpansion propertyExpansion;
	
	@Override
	public SoapRequest fromFile(String requestFile) {
		String envelope = FileUtilities.loadRequestFromFile(requestFile);
		envelope = replacePropertyExpansions(envelope);
		return new SoapRequest(envelope);
	}

	
	private String replacePropertyExpansions(String envelope) {
		return propertyExpansion.translate(envelope);
	}
	@Override
	public SoapRequest fromFile(String requestFile, String messageId, String memberId, String cardNumber,
			String transactionAmount, String transactionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SoapRequest fromFile(String requestFile, Map<String, String> requestParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
