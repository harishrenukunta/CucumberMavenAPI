package com.harish.apitests.framework.services;

import com.harish.apitests.config.CardInfoConfig;
import com.harish.apitests.enums.ServiceName;
import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.model.SoapResponse;
import com.harish.apitests.framework.utils.ScenarioContext;
import com.harish.apitests.framework.utils.XMLHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.harish.apitests.enums.Constants.CARDNUMBER;

public class BaseService {

    @Autowired
    protected SOAPService service;

    @Autowired
    protected CardInfoConfig cardsConfig;

    @Autowired
    protected ScenarioContext scenarioContext;

    @Autowired
    protected Environment env;

    @Autowired
    protected SoapRequest request;

    @Autowired
    protected SoapResponse response;

    public Map<String, String> resolveRequestParamsFromPreviousCalls(final Map<String, String> requestData){
        final Map<String, String> requestParams = new HashMap<>();
        final Pattern pattern = Pattern.compile("<(.*)#(.*)>");
        final Pattern pattern2 = Pattern.compile("<(.*)>");
        final Pattern pattern3 = Pattern.compile(".*([d|D]ate|[t|T]ime[S|s]tamp)");
        final Pattern pattern4 = Pattern.compile("^([-+]?\\d+)$");

        requestData.forEach((key, val) -> {
            final Matcher matcher = pattern.matcher(val);
            final Matcher commonDataMatcher = pattern2.matcher(val);
            final Matcher dateKeyMatcher = pattern3.matcher(key);

            if(dateKeyMatcher.find()){
                final Matcher dateValMatcher = pattern4.matcher(val);
                if(dateValMatcher.find()){
                    final LocalDate todaysDate = LocalDate.now();
                    final LocalDate targetDate = todaysDate.plusDays(Integer.parseInt(val));
                    val = targetDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
                }
            }
            if(matcher.find()){
                final String serviceName = matcher.group(1);
                final String fieldName = matcher.group(2);
                final ServiceName wsService = ServiceName.getServiceByName(serviceName);
                final String savedServiceResponse = scenarioContext.getServiceResponseByName(wsService);
                if(savedServiceResponse != null){
                    try{
                        final XMLHolder savedResponseXmlHolder = new XMLHolder(savedServiceResponse);
                        //add namespaces to xml holder
                        savedResponseXmlHolder.addNamespace("ns2","http://www.dfsdisputes.com_iss162/services/DNDisptesIssuerVo_162" );
                        savedResponseXmlHolder.addNamespace("ns3","http://www.dfsdisputes.com_iss162/services/InterfacesVo" );
                        savedResponseXmlHolder.addNamespace("soap","http://schemas.xmlsoap.org/soap/envelope" );

                        final String xpath = "//soap:Body/*[1]/" + fieldName;
                        final String dataElementValue = savedResponseXmlHolder.getNodeValue(xpath);
                        if(!StringUtils.isEmpty(dataElementValue)){
                            requestParams.put(key, dataElementValue);
                        }
                    }catch(IOException | SAXException | ParserConfigurationException e){
                        e.printStackTrace();
                        throw new RuntimeException("Exception while resolving parameters for request:" + e.getMessage());

                    }
                }else {
                    requestParams.put(key, val);
                }
            }else if(commonDataMatcher.find()){
                final String prop = commonDataMatcher.group(1);
                final String propValue = env.getProperty(prop) == null ? val : env.getProperty(prop);
                requestParams.put(key, propValue);
            }else if(key.equalsIgnoreCase(CARDNUMBER.toString()) || key.contains(CARDNUMBER.toString())){
                final String encryptedCardNumber = cardsConfig.getCardByCardNumber(val) != null ? cardsConfig.getCardByCardNumber(val).getEncryptedCardNo() : val;
                requestParams.put(key, encryptedCardNumber);
            }else {
                requestParams.put(key, val);
            }

        });
        return requestParams;
    }

    private String invokeMethod(final Object object, final Class<?> clazz, final String targetMethod) {
        String result = null;
        final Method[] methods = clazz.getDeclaredMethods();
        for(Method method: methods){
            if(method.toString().toLowerCase().contains("get" + targetMethod.toLowerCase())){
                try {
                    result = method.invoke(object).toString();
                }catch(InvocationTargetException ex){
                    throw new Error("Exception when invoking getter for element " + targetMethod + " Exception thrown:" + ex.getMessage());
                }catch(Exception ex){
                    throw new Error("Exception when invoking getter for " + targetMethod + "Exception message:" + ex.getMessage());
                }
            }
        }
        return result;
    }

}
