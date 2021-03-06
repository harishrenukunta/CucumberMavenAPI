package com.harish.apitests.framework.services;

import java.io.FileNotFoundException;
import java.util.Map;
import com.harish.apitests.enums.ServiceName;

public interface DiscService {
	public void callServiceWithRequestPOJO(final Object requestPOJO, final Map<String, String> headerData);
	public Object createRequestPOJO(final ServiceName serviceName, final Map<String, String> requestData) throws FileNotFoundException;
	public Map<String, String> resolveRequestParamsFromPreviousCalls(final Map<String, String> requestData);
}
