package com.harish.apitests.framework.services;


/**
 * 
 * Calls the application with rest requests, ie. allows to change feature toggle state
 * @author harishrenukunta
 *
 */
@FunctionalInterface
public interface RestClient {
	
	/**
	 * @param featureName
	 * @param newState
	 */
	
	void changeFeatureToggleState(String featureName, boolean newState);
}
