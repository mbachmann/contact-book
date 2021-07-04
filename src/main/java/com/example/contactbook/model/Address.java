package com.example.contactbook.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String street;

    private String additional;

    @Size(min = 4, max = 5)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;


    public Address() {
    }

    public Long getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getStreet() {
        return this.street;
    }

    public String getAdditional() {
        return this.additional;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String toString() {
        return "Address(id=" + this.getId() + ", type=" + this.getType() + ", street=" + this.getStreet() + ", additional=" + this.getAdditional() + ", postalCode=" + this.getPostalCode() + ", city=" + this.getCity() + ", country=" + this.getCountry() + ")";
    }
}
