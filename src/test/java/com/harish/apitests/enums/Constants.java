package com.harish.apitests.enums;

public enum Constants {

    NOT_NULL("<not null>"),
    NULL("<null>"),
    IGNORE("<ignore>"),
    EMPTY("<empty>"),
    CARDNUMBER("cardNumber");

    String val;
    Constants(String val){this.val = val;}


    @Override
    public java.lang.String toString() {
        return this.val;
    }
}
