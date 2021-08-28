package com.example.contactbook.model;

import com.example.contactbook.model.codes.EmailType;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class Email implements Serializable {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(optional = false)
    private EmailType emailType;

    @Column(nullable = false)
    @Size(min = 2, max = 60)
    private String address;


    public Email() {
    }

    public Email(String address, EmailType emailType) {
        this.address = address;
        this.emailType = emailType;
    }

    public Long getId() {
        return this.id;
    }


    public String getAddress() {
        return this.address;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        String emailTypeString = this.getEmailType() != null ? ", type=" + this.getEmailType().getTitle() + ", " + this.getEmailType().getShortCut() : "";
        return "Email(id=" + this.getId() +  ", address=" + this.getAddress() + emailTypeString + ")";
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        return id != null && id.equals(((Email) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
