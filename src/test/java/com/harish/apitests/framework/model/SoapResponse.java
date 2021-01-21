package com.harish.apitests.framework.model;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoapResponse {
	
	private String httpStatusCode;
	private String payload;
	
	public void get(Supplier<SoapResponse> supp) {
		final SoapResponse res = supp.get();
		this.httpStatusCode = res.getHttpStatusCode();
		this.payload = res.getPayload();
	}

}
