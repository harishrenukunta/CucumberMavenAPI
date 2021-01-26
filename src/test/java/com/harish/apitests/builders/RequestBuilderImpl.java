package com.harish.apitests.builders;

import com.harish.apitests.framework.Constants;
import com.harish.apitests.framework.ObjectUtils;
import com.harish.apitests.framework.SpringContext;
import com.harish.apitests.framework.utils.FileUtilities;
import com.harish.apitests.framework.utils.XMLHolder;
import com.harish.apitests.jaxb.TransactionLookupKey;
import com.harish.apitests.jaxb.UserInfo;
import com.harish.apitests.jaxb.WebServiceAbsRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static com.harish.apitests.framework.ObjectUtils.getObjectFromNode;
import static com.harish.apitests.framework.ObjectUtils.setObject;

@Slf4j
public class RequestBuilderImpl<T extends WebServiceAbsRequestVo> implements RequestBuilder<T>{

    protected UserInfo userInfo;
    protected T requestPOJO;
    protected String msgIdPrefix;
    protected String baseTemplate;
    protected String serviceNodeXPath;
    protected Map<String, String> requestPayloadData;
    protected Class<T> requestPOJOClazz;
    protected Boolean transactionLookUpKey = false;

    public RequestBuilderImpl(){
        userInfo = UserInfo.builder()
                      .userDepartmentName("?")
                      .userId("1:GRP")
                      .userName("AUTHORIZED_USER")
                .memberId(0)
                .roleClass("submit_followup_auction")
                .userDepartmentName("disputes")
                .userEmployerName("discover")
                .userLocation("RIVERWOODS")
                .userCenterCode("2500")
                .build();
    }

    public RequestBuilderImpl withRequestPOJOClazz(Class<T> requestPOJOClazz){
        this.requestPOJOClazz = requestPOJOClazz;
        return this;
    }

    public RequestBuilderImpl withTransactionLookUpKey(Boolean isTransactionLookUpKeyExist){
        this.transactionLookUpKey = isTransactionLookUpKeyExist;
        return this;
    }

    public RequestBuilderImpl withMessageIdPrefix(final String messageIdPrefix){
        this.msgIdPrefix = messageIdPrefix;
        return this;
    }

    public RequestBuilderImpl populateWithRequestData(final Map<String, String> payloadData){
        this.requestPayloadData = payloadData;
        return this;
    }

    public RequestBuilderImpl defaults(){
        try{
            requestPOJO = requestPOJOClazz.newInstance();
            requestPOJO.setTestFlag(false);
        }catch(InstantiationException | IllegalAccessException ex){
            ex.printStackTrace();
            throw new RuntimeException("Exception when instantiating request POJO. Exception msg:" + ex.getMessage());
        }
        return this;
    }

    protected void createRequestPOJO(){
        if(requestPOJO != null){
            return;
        }

        Predicate<String> isBaseTemplateProvided = Objects::nonNull;
        if(isBaseTemplateProvided.test(baseTemplate)){
            final String xml = FileUtilities.loadRequestFromFile(baseTemplate);
            XMLHolder xmlHolder = null;
            try{
                xmlHolder = new XMLHolder(xml);
            } catch (SAXException | ParserConfigurationException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Exception when creating  xmlholder. Exception details:" + e.getMessage());
            }
            assignRequestNamespaces(xmlHolder);
            Optional.ofNullable(serviceNodeXPath)
                    .orElseThrow(() -> new RuntimeException("Please provide service node xpath e.g //ns1:CreateCaseRequestVo for constructing soap requesting"));
            final Node soapBodyNodeValue = xmlHolder.getNode(serviceNodeXPath);
            requestPOJO = getObjectFromNode(soapBodyNodeValue, requestPOJOClazz);
        }else {
            try{
                requestPOJO = requestPOJOClazz.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("Failed to create a soap request object for clazz %s and exception message is %s", requestPOJOClazz.getName()));
            }
        }
    }


    @Override
    public T build(Map<String, String> requestInfo) {
        this.requestPayloadData = requestInfo;
        assignMessageId(this.msgIdPrefix);
        requestInfo.forEach((key, value) -> {
            switch(key.trim().toLowerCase()){
                case "userinfo":
                    assignUserInfo(value);
                    break;
                default:
                    setObject(requestPOJO.getClass(), requestPOJO, key, value);
            }
        });
        return requestPOJO;
    }

    @Override
    public T build() throws FileNotFoundException {
        createRequestPOJO();
        assignMessageId(msgIdPrefix);
        requestPOJO.setUserInfo(userInfo);
        if(transactionLookUpKey){
            TransactionLookupKey transactionLookUpKeyPOJO = new TransactionLookUpKeyBuilder()
                    .withTransactionLookUpKeyData(requestPayloadData)
                    .build();
            try{
                ObjectUtils.injectObject(requestPOJO, "transactionLookupKey", transactionLookUpKeyPOJO);
            }catch(InvocationTargetException | IllegalAccessException ex){
                ex.printStackTrace();
            }
        }

        requestPayloadData.forEach((key, value) -> {
            switch(key.trim().toLowerCase()){
                case "userinfo":
                    assignUserInfo(value);
                    break;
                default:
                    switch(key.trim()){
                        case "dnDisputeCaseNumber":
                            value = StringUtils.isEmpty(value.trim()) ? null: StringUtils.leftPad(value, 10, "0");
                            break;
                        case "issuerCaseNumber":
                            value = StringUtils.isEmpty(value.trim())?null: value;
                            break;
                        default:
                            value = StringUtils.isEmpty(value) ? null: value;
                    }
                    setObject(requestPOJO.getClass(), requestPOJO, key, value);
            }
        });
        return requestPOJO;
    }

    protected T constructRequestPOJO(final String intiailRequesttemplateToLoad, final String serviceNodeXPath, final String messageIdPrefix, final Class<T> requestPOJOClazz){
        final String xml = FileUtilities.loadRequestFromFile(intiailRequesttemplateToLoad);
        XMLHolder xmlHolder = null;
        try{
            xmlHolder = new XMLHolder(xml);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
            throw new Error("Exception when creating a xmlholder. Exception details:" + e.getMessage());
        }
        assignRequestNamespaces(xmlHolder);
        final Node soapBodyNodeValue = xmlHolder.getNode(serviceNodeXPath);
        this.msgIdPrefix = messageIdPrefix;
        requestPOJO.setUserInfo(userInfo);
        return requestPOJO;
    }

    public void assignRequestNamespaces(final XMLHolder xmlHolder){
        xmlHolder.addNamespace("ns1", "http://www.dfsdisputes.com_iss162/services/DNDisputesIssuer_162");
    }

    protected void assignMessageId(final String messageIdPrefix){
        Optional.ofNullable(messageIdPrefix)
                .orElseThrow(() -> new RuntimeException("Please provide a prefix for message id sothat a unique message id can be created e.g: 'CC' or 'TS'"));
        if(requestPayloadData.containsKey("messageId")){
            final String messageIdValue = requestPayloadData.get("messageId");
            if(Constants.NULL.toString().equals(messageIdValue)){
                requestPOJO.setMessageId(null);
            }else if(Constants.EMPTY.toString().equals(messageIdValue)){
                requestPOJO.setMessageId("");
            }
        }else {
            final String messageId = messageIdPrefix + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + LocalDateTime.now().getNano();
            requestPOJO.setMessageId(messageId);
        }
    }

    protected void assignUserInfo(String userType){
        final Environment env = (Environment) SpringContext.getApplicationContext().getBean(Environment.class);
        final String type = StringUtils.capitalize(userType);

        try{
            userInfo.setUserId(env.getProperty(String.format("%s.userId", type)));
            userInfo.setUserName(env.getProperty(String.format("%s.userName", type)));
            userInfo.setMemberId(Integer.parseInt(env.getProperty(String.format("%s.memberId", type))));
            userInfo.setRoleClass(env.getProperty(String.format("%s.roleClass", type)));
            requestPOJO.setUserInfo(userInfo);
        }catch(Exception ex){
            log.error("Exception while assigning userinfo:" + ex.getMessage());
            throw ex;
        }
    }
}
