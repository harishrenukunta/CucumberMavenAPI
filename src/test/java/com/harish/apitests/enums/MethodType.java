package com.harish.apitests.enums;

public enum MethodType {
	
	SETTER("set"),
	GETTER("get");
	
	MethodType(final String mType){ this.mType = mType;}
	
	String mType;
	
	public String getMType() {return mType;}

}
