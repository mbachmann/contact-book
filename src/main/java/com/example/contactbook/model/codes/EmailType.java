package com.example.contactbook.model.codes;


import com.example.contactbook.model.Email;
import com.example.contactbook.model.enums.CodeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("EmailType")
public class EmailType extends Code {

    public EmailType() {}

    public EmailType(Long id, String title, String shortCut, Boolean active, Long usage) {
        super(id, title, shortCut, active, usage);
        super.type = CodeType.EmailType.getValue();
    }

    public EmailType(String title, String shortCut) {
        super(title, shortCut);
        super.type = CodeType.EmailType.getValue();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emailType")
    private Set<Email> emails;

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

