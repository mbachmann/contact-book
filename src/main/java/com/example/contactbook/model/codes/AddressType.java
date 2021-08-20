package com.example.contactbook.model.codes;


import com.example.contactbook.model.enums.CodeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AddressType")
public class AddressType extends Code {

    public AddressType() {}

    public AddressType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.AddressType.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressType)) {
            return false;
        }
        return id != null && id.equals(((AddressType) o).id);
    }
}
