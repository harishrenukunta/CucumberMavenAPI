package com.harish.apitests.cucumber;

import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;

public class CucumberConfiguration {
	
	
	private final ObjectMapper objectMapper;
	
	public CucumberConfiguration() {
		objectMapper = new ObjectMapper();
	}
	
	@DefaultDataTableCellTransformer
	@DefaultDataTableEntryTransformer
	@DefaultParameterTransformer
	public Object transform(final Object fromValue, Type to) {
		return objectMapper.convertValue(fromValue, objectMapper.constructType(to));
	}

}
