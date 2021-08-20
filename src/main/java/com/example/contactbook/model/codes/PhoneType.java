package com.example.contactbook.model.codes;


import com.example.contactbook.model.enums.CodeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PhoneType")
public class PhoneType extends Code {

    public PhoneType() {}

    public PhoneType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.PhoneType.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneType)) {
            return false;
        }
        return id != null && id.equals(((PhoneType) o).id);
    }
}
