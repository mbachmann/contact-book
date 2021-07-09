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
        return "Phone(id=" + this.getId() + ", type=" + this.getPhoneType() + ", number=" + this.getNumber() + ")";
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }
}
