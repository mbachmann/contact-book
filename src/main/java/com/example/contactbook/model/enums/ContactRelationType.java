package com.example.contactbook.model.enums;

/**
 * The ContactRelationType enumeration.
 */
public enum ContactRelationType {
    CUSTOMER("CL"),
    CREDITOR("CR"),
    TEAM("TE"),
    AUTORITY("AT"),
    MEDICAL("ME"),
    OTHERS("OT");

    private final String value;

    ContactRelationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
