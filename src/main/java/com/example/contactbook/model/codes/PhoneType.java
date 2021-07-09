package com.example.contactbook.model.codes;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PhoneType")
public class PhoneType extends Code {

    public PhoneType() {}

    public PhoneType(String title, String shortCut) {
        super(title, shortCut);
    }
}
