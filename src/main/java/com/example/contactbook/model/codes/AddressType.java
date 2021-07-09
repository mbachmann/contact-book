package com.example.contactbook.model.codes;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AddressType")
public class AddressType extends Code {

    public AddressType() {}

    public AddressType(String title, String shortCut) {
        super(title, shortCut);
    }


}
