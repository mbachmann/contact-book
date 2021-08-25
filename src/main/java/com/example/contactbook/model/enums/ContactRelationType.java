package com.example.contactbook.model.enums;

/**
 * The ContactRelationType enumeration.
 */
public enum ContactRelationType {
    CUSTOMER("CL"),
    CREDITOR("CR"),
    COLLEAGUES("CO"),
    AUTORITY("AT"),
    MEDICAL("ME"),
    OTHERS("OT"),
    FAMILIY("FA"),
    FRIENDS("FR");

    private final String value;

    ContactRelationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
