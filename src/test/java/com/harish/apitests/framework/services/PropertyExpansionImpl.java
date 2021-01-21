package com.harish.apitests.framework.services;

import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PropertyExpansionImpl implements PropertyExpansion {
	
	private List<Property> list = Collections.singletonList(element("${#TestSuite#authUserName}", "AUTHORIZED_USER"));

	@Override
	public String translate(String envelope) {
		if(StringUtils.isEmpty(envelope))
		return null;
		
		for(Property property: list) {
			envelope = replaceInEnvelope(envelope, property);
		}
		
		return envelope;
	}
	
	private String replaceInEnvelope(String envelope, Property prop) {
		return envelope.replace(prop.key,  prop.value);
	}
	
	
	
	private Property element(String key, String value) {
		return new Property(key, value);
	}
	
	@Getter
	@AllArgsConstructor
	private class Property {
		private String key;
		private String value;
	}

}
