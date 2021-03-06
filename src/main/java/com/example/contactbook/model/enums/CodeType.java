package com.example.contactbook.model.enums;

public enum CodeType {

    EmailType("EmailType"),
    PhoneType("PhoneType"),
    AddressType("AddressType"),
    AllCodes("CodeType");

    private final String value;

    CodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

