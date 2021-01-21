package com.harish.apitests.framework.services;

import java.util.Map;

import com.harish.apitests.framework.model.SoapRequest;

/**
 * Builds SoapRequest from file
 * @author harishrenukunta
 * Not only raw envelope is delivered. There might be some property expansion translations necessary.
 *
 */
public interface SoapRequestBuilder {

	/**
	 * @param requestFile
	 * @return
	 */
	
	SoapRequest fromFile(String requestFile);
	
	/**
	 * @param requestFile
	 * @param messageId
	 * @param memberId
	 * @param cardNumber
	 * @param transactionAmount
	 * @param transactionDate
	 * @return
	 */
	SoapRequest fromFile(String requestFile, String messageId, String memberId, String cardNumber, String transactionAmount, String transactionDate);
	
	/**
	 * @param requestFile
	 * @param requestParams
	 * @return
	 */
	SoapRequest fromFile(String requestFile, Map<String, String> requestParams);
	
}
