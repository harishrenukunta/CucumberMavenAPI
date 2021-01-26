package com.harish.apitests.builders;

import com.harish.apitests.framework.ObjectUtils;
import com.harish.apitests.jaxb.TransactionLookupKey;

import javax.xml.datatype.DatatypeConfigurationException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.harish.apitests.framework.ObjectUtils.setObject;

public class TransactionLookUpKeyBuilder {

    private Map<String, String> transactionLookUpKeyData;

    public TransactionLookUpKeyBuilder withTransactionLookUpKeyData(final Map<String, String> transactionLookUpKeyData){
        this.transactionLookUpKeyData = transactionLookUpKeyData;
        return this;
    }

    public TransactionLookupKey build(){
        final TransactionLookupKey transactionLookupKey = new TransactionLookupKey();
        if(transactionLookUpKeyData.containsKey("dndsTransactionKey")){
            transactionLookupKey.setDndsTransactionKey(transactionLookUpKeyData.get("dndsTransactionKey"));
        }else{
            transactionLookupKey.setDndsTransactionKey(null);
            final TransactionLookupKey.GenericTransactionKey gtKey = new TransactionLookupKey.GenericTransactionKey();

            try{
                gtKey.setTransactionDate(ObjectUtils.getXMLGregorianCalendarFromStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

            } catch (ParseException | DatatypeConfigurationException e) {
                e.printStackTrace();
            }
            transactionLookupKey.setGenericTransactionKey(gtKey);
            transactionLookUpKeyData.forEach((key, value)->{
                setObject(gtKey.getClass(), gtKey, key, value);
            });
        }
        return transactionLookupKey;
    }
}
