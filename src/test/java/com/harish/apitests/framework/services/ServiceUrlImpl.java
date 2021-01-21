package com.harish.apitests.framework.services;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServiceUrlImpl implements ServiceUrl {
	
	private ServerConfig serverConfig;
	
	/* (non-Javadoc)
	 * @see com.discover.inbound.service.ServiceUrl#soapServerAddress()
	 */

	@Override
	public String soapServerAddress() {
		return serverConfig.getServerUrl() + serverConfig.getServiceAddress();
	}
	
	
	/* (non-Javadoc)
	 * @see com.discover.inbound.service.ServiceUrl#soapServerAddress()
	 */
	@Override
	public String restServerAddress() {
		// TODO Auto-generated method stub
		return serverConfig.getServerUrl() + serverConfig.getTogglzServiceAddress();
	}

}
