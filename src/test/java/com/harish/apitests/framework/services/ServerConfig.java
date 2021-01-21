package com.harish.apitests.framework.services;

public interface ServerConfig {
	
	String getServerUrl();
	
	/**
	 * 
	 * @param port
	 */
	
	void registerServerPort(int port);
	
	default String getServiceAddress() {
		return "/disputesWebService1Web/services/DNDisputesIssuerServicesSOAPExt_162";
	}
	
	default String getTogglzServiceAddress() { return "/devtools/togglz";}

}
