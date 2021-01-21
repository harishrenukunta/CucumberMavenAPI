package com.harish.apitests.framework.services;

import java.net.MalformedURLException;

import javax.xml.soap.SOAPException;

import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.model.SoapResponse;

public interface SoapClient {
	
	SoapResponse send(SoapRequest request)throws MalformedURLException, SOAPException;

}
