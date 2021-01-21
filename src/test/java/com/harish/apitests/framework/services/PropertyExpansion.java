package com.harish.apitests.framework.services;

/**
 * replaces property expansion or placeholders in soap envelope
 * 
 * @author harishrenukunta
 *
 *@see https://www.soapui.org/scriptiong-properties/property-expansion.html
 */
public interface PropertyExpansion {
	
	String translate(String envelope);

}
