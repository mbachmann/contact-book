package com.example.contactbook.model.codes;


import com.example.contactbook.model.Phone;
import com.example.contactbook.model.enums.CodeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("PhoneType")
public class PhoneType extends Code {

    public PhoneType() {}

    public PhoneType(Long id, String title, String shortCut, Boolean active, Long usage) {
        super(id, title, shortCut, active, usage);
        super.type = CodeType.PhoneType.getValue();
    }


    public PhoneType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.PhoneType.getValue();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phoneType")
    private Set<Phone> phones;

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
