package com.example.contactbook.model;

import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.PhoneType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private AddressType addressType;

    @Column(nullable = false)
    @Size(max = 50)
    private String street;

    @Size(max = 255)
    private String additional;

    @Size(min = 4, max = 5)
    private String postalCode;

    @Column(nullable = false)
    @Size(max = 50)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Boolean defaultAddress;


    public Address() {
    }

    public Address(String street, String postalCode, String city, String country, boolean defaultAddress, AddressType addressType) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.defaultAddress = defaultAddress;
        this.addressType = addressType;
    }

    public Long getId() {
        return this.id;
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

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String toString() {
       String addressTypeString = this.getAddressType() != null ? ", type=" + this.getAddressType().getTitle() + ", " + this.getAddressType().getShortCut() : "";
        return "Address(id=" + this.getId() +  ", street=" + this.getStreet() + ", additional=" + this.getAdditional() + ", postalCode=" + this.getPostalCode() + ", city=" + this.getCity() + ", country=" + this.getCountry() + addressTypeString + ")";
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
