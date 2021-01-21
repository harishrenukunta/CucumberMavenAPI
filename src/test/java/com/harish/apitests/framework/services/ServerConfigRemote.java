package com.harish.apitests.framework.services;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerConfigRemote implements ServerConfig {
	private Integer serverPort;
	
	@Getter
	private String serverUrl;
	
	public ServerConfigRemote(String serverUrl) {this.serverUrl = serverUrl;}
	
	/* (non-Javadoc)
	 * @see com.discover.inbound.srvice.ServerConfig#registerServerPort(int)
	 */
	@Override
	public void registerServerPort(int port) {
		if(Objects.isNull(serverPort)) {
			serverPort = port;
		}
	}

}
