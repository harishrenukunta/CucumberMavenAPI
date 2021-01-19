package com.harish.apitests.framework;

public enum Constants {
	
	NULL("<null>"),
	EMPTY("<empty>");
	
	Constants(final String val){
		this.enumValue = val;
	}
	
	private String enumValue;

}
