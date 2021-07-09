package com.example.contactbook.model.codes;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EmailType")
public class EmailType extends Code {

    public EmailType() {}

    public EmailType(String title, String shortCut) {
        super(title, shortCut);
    }


}

