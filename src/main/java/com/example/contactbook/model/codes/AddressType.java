package com.example.contactbook.model.codes;


import com.example.contactbook.model.Address;
import com.example.contactbook.model.enums.CodeType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("AddressType")
public class AddressType extends Code {

    public AddressType() {}

    public AddressType(Long id, String title, String shortCut, Boolean active, Long usage) {
        super(id, title, shortCut, active, usage);
        super.type = CodeType.AddressType.getValue();
    }

    public AddressType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.AddressType.getValue();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "addressType")
    private Set<Address> addresses;

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
