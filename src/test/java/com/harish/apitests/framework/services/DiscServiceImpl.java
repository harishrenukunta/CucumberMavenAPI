package com.harish.apitests.framework.services;


import com.harish.apitests.builders.RequestBuilderImpl;
import com.harish.apitests.enums.ServiceName;
import com.harish.apitests.framework.model.SoapResponse;
import com.harish.apitests.framework.utils.FileUtilities;
import com.harish.apitests.framework.utils.SOAPUtils;
import com.harish.apitests.jaxb.CaseDetailsRequestVo;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import io.cucumber.java.Scenario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.harish.apitests.framework.ObjectUtils.getXmlFromPOJO;
import static com.harish.apitests.framework.utils.FileUtilities.loadRequestFromFile;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class DiscServiceImpl extends BaseService implements DiscService {

    @Setter
    Scenario scenario;
    final Environment env;

    @Value("${test.maxRetry.attempt}")
    private int maxRetryAttempt;

    final static String BUILDER_CLASS_PACKAGE = "com.disc.sit.webservices.model.builders.";

    String pwd = null;


    @Override
    public void callServiceWithRequestPOJO(Object requestPOJO, Map<String, String> requestData) {
        try {
            getSOAPRequest(requestPOJO, requestData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Boolean.parseBoolean(env.getProperty("mock.response")) == true){
            this.maxRetryAttempt = 1;
            response.get(this::get);
            loadResponseFile(env.getProperty("mock.response.file"));
            InputStream is = new ByteArrayInputStream(response.getPayload().getBytes());
            SOAPMessage soapResponse = null;
            try{
                soapResponse = MessageFactory.newInstance().createMessage(null, is);
                final String formattedResponse;
                formattedResponse = SOAPUtils.messageToString(soapResponse);
                log.debug("Mock Response:" + formattedResponse);
                scenario.log("Mock Response: \\n" + formattedResponse);
            } catch (SOAPException | IOException | TransformerException e) {
                e.printStackTrace();
            }
        }else {
            response.get(this::get);
            Optional.ofNullable(response)
                    .orElseThrow(() -> new RuntimeException("Error while calling service"));
        }
        scenarioContext.storeSoapResponse(response);
    }

    private void loadResponseFile(final String responseFileToLoad){
        String res = FileUtilities.loadResponseFromFile(responseFileToLoad);
        response.setPayload(res);
    }

    private SoapResponse get(){
        boolean reRun = true;
        SoapResponse response = null;
        this.scenario.log("Max.Attempts:" + maxRetryAttempt);
        for(int runCounter = 0; runCounter < maxRetryAttempt && reRun; runCounter++){
            try {
                response = service.sendToServer(request);
                reRun = checkForRetryResponseCodes(response);
            } catch (MalformedURLException | SOAPException e) {
                log.debug("Exception while calling service : Attempt :" + (runCounter + 1));
                scenario.log("Exception while calling service: Attempt:" + (runCounter + 1) + ":" + ExceptionUtils.getStackTrace(e));
            }finally {
                if(response == null)
                    reRun = true;
            }
        }
        return response;
    }

    private boolean checkForRetryResponseCodes(final SoapResponse response){
        final String[] responseCodes = {"99.102", "2036.1", "2034.1"};
        return Arrays.asList(responseCodes).stream()
                .anyMatch(responseCode -> response.getPayload().contains(String.format("<responseCode>%s</responseCode>", responseCode)));
    }

    private String getSOAPRequest(Object requestPOJO, Map<String, String> requestData) throws JSONException {
        final String xmlFragment = getXmlFromPOJO(requestPOJO, true);
        final String soapRequestTemplate = loadRequestFromFile("SoapRequest_User1_Template.xml");
        final String userInfoKey = requestData.get("UserInfo") == null ? requestData.get("userInfo") : requestData.get("UserInfo");
        Optional.ofNullable(userInfoKey)
                .orElseThrow(() -> new AssertionError("'UserInfo' not provided. Please provide"));

        if(Boolean.parseBoolean(env.getProperty("local.env"))){
            this.pwd = env.getProperty("pwd");
        }else {
            if(StringUtils.isEmpty(this.pwd)){
                this.pwd = getPwdFromVault();
            }
        }

        final String wsseUserPropKey = "wsse.username";
        final String wssePasswordPropKey = "wsse.password";
        final String wsseUsername = (requestData.get("wsseUsername") != null) ? requestData.get("wsseUsername") : env.getProperty(userInfoKey + "." + wsseUserPropKey);
        final String wssePassword = requestData.get("wssePassword") != null ? requestData.get("wssePassword") : this.pwd;
        final String requestXML = soapRequestTemplate
                .replace("{payload}", xmlFragment)
                .replace("{wssePassword}", wssePassword)
                .replace("{wsseUsername}", wsseUsername);
        log.debug("Request log:" + requestXML);
        request.setEnvelope(requestXML);
        return requestXML;
    }

    private String getPwdFromVault() throws JSONException {
        final String BASE_URI = env.getProperty("vault.url");
        final String LOGIN_URI = "/auth/approle/login";
        final String SECRET_PATH = "/" + env.getProperty("vault.secret.path");
        final String ROLE_ID = env.getProperty("vault.role.id");
        final RequestSpecification request = RestAssured.given();
        request.baseUri(BASE_URI);

        final JSONObject loginPayload = new JSONObject();
        loginPayload.put("role_id", ROLE_ID);
        loginPayload.put("secret", "null");
        request.header("Content-Type", MediaType.APPLICATION_JSON);
        request.body(loginPayload.toString());
        final Response response = request.post(LOGIN_URI);
        log.info(response.asString());

        final String clientToken = response.jsonPath().get("auth_client_token");
        final String pw = Optional.ofNullable(clientToken)
                .map(ct -> {
                    final Map<String, String> requestHeaders = new HashMap<>();
                    requestHeaders.put("Content-Type", MediaType.APPLICATION_JSON.toString());
                    requestHeaders.put("X-Vault-Token", ct);
                    final Response secretPathResponse = request
                            .headers(requestHeaders)
                            .get(SECRET_PATH);
                    return secretPathResponse.jsonPath().get("data.passphrase").toString();
                })
                .orElse(null);
        log.debug("pw: " + pw);
        return pw;
    }

    @Override
    public Object createRequestPOJO(ServiceName serviceName, Map<String, String> requestParams) throws FileNotFoundException {
        scenarioContext.setCurrentService(serviceName);
        final Map<String, String> requestData = resolveRequestParamsFromPreviousCalls(requestParams);
        if(serviceName == ServiceName.CASE_DETAILS){
            return new RequestBuilderImpl<CaseDetailsRequestVo>()
                    .withRequestPOJOClazz(CaseDetailsRequestVo.class)
                    .defaults()
                    .withMessageIdPrefix("CD")
                    .populateWithRequestData(requestParams)
                    .build();
        }
        return null;
    }
}
