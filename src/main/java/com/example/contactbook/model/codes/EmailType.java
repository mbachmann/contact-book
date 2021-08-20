package com.example.contactbook.model.codes;


import com.example.contactbook.model.enums.CodeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EmailType")
public class EmailType extends Code {

    public EmailType() {}

    public EmailType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.EmailType.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailType)) {
            return false;
        }
        return id != null && id.equals(((EmailType) o).id);
    }
}

