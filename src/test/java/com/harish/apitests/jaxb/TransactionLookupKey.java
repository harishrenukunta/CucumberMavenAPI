package com.harish.apitests.jaxb;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@Data
public class TransactionLookupKey {

    protected TransactionLookupKey.GenericTransactionKey genericTransactionKey;
    protected String dndsTransactionKey;

    @Data
    public static class GenericTransactionKey {

        @XmlElement(name="NRID")
        protected String nrid;
        protected String approvalCode;
        @XmlElement(required = true)
        protected String cardNumber;
        @XmlElement(required = true)
        protected String merchantNumber;
        @XmlElement(required = true)
        protected BigDecimal transactionAmount;
        @XmlElement(required = true)
        @XmlSchemaType(name="date")
        protected XMLGregorianCalendar transactionDate;
        protected String transactionCode;
    }
}
