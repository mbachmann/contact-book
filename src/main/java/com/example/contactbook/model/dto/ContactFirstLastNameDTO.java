package com.example.contactbook.model.dto;


import com.example.contactbook.model.codes.PhoneType;

public class ContactFirstLastNameDTO {

    String firstName;
    String lastName;

    public ContactFirstLastNameDTO () {}
    public ContactFirstLastNameDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContactFirstLastNameDTO)) {
            return false;
        }
        return firstName.equals(((ContactFirstLastNameDTO)obj).firstName) &&
                lastName.equals(((ContactFirstLastNameDTO)obj).lastName);
    }
}

