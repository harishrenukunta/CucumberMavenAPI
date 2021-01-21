package com.harish.apitests.enums;

import org.assertj.core.util.Arrays;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ServiceName {
	
	TRANSACTION_SEARCH("transaction search");
	
	private String wsName;

	
	ServiceName(String wsName){
		this.wsName = wsName;
	}
	
	public static ServiceName getServiceByName(final String name) {
		return Stream.of(ServiceName.values())
				.filter(sn -> sn.wsName.equalsIgnoreCase(name) || (ServiceName.checkServiceWithNoSpaces(sn.wsName).equalsIgnoreCase(name)))
				.findAny()
				.orElse(null);
	}
	
	private static String checkServiceWithNoSpaces(final String sname) {
		return Arrays.asList(sname.split(" ")).stream()
				.map( s -> s.toString())
				.collect(Collectors.joining(""));
				
	}
	
	public String getWsName() { return this.wsName;}

}
