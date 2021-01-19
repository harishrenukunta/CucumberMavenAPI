package com.harish.apitests.framework.utils;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class DiscNSPrefixMapper extends NamespacePrefixMapper{
	
	private Map<String, String> nsMap = new HashMap();
	
	public DiscNSPrefixMapper() {
		// TODO Auto-generated constructor stub
		//nsMap.put("http://www.dfsdisputes.com__iss162/services/DNDisputesIssuerVo_162", "dnd");
		//populate hashmap with all namespaces just as shown above
	}

	@Override
	public String getPreferredPrefix(String namespaceUrl, String suggestion, boolean requirePrefix) {
		// TODO Auto-generated method stub
		return nsMap.getOrDefault(namespaceUrl, suggestion);
	}

}
