package com.example.contactbook.model;

import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class Phone implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private PhoneType phoneType;

    @Size(min = 2, max = 20)
    private String number;

    public Phone() {
    }

    public Phone(String number, PhoneType phoneType) {
        this.number = number;
        this.phoneType = phoneType;
    }

    public Long getId() {
        return this.id;
    }


    public String getNumber() {
        return this.number;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    public String toString() {
        String phoneTypeString = this.getPhoneType() != null ? ", type=" + this.getPhoneType().getTitle() + ", " + this.getPhoneType().getShortCut() : "";
        return "Phone(id=" + this.getId() + ", number=" + this.getNumber() + phoneTypeString + ")";
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phone)) {
            return false;
        }
        return id != null && id.equals(((Phone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
