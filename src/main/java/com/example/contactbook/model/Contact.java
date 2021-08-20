package com.example.contactbook.model;

import javax.persistence.*;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Entity
public class Contact  {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] photo;

    @Lob
    private byte[] thumbNail;

    private String photoContentType;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    private String lastName;

    private String company;

    private LocalDate birthDate;

    private String notes;

    private String phonesAggregate;

    private String addressesAggregate;

    private String emailsAggregate;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL )
    private Set<Phone> phones;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Address> addresses;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Email> emails;

    public Contact() {
    }

    public Long getId() {
        return this.id;
    }


    public byte[] getPhoto() {
        return this.photo;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getCompany() {
        return this.company;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public Set<Phone> getPhones() {
        return this.phones;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public Set<Email> getEmails() {
        return this.emails;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public byte[] getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(byte[] thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getPhonesAggregate() {
        return phonesAggregate;
    }

    public void setPhonesAggregate(String phonesAggregate) {
        this.phonesAggregate = phonesAggregate;
    }

    public String getAddressesAggregate() {
        return addressesAggregate;
    }

    public void setAddressesAggregate(String addressesAggregate) {
        this.addressesAggregate = addressesAggregate;
    }

    public String getEmailsAggregate() {
        return emailsAggregate;
    }

    public void setEmailsAggregate(String emailsAggregate) {
        this.emailsAggregate = emailsAggregate;
    }

    public String toString() {
        String addressesString = this.getAddresses() != null ? ", addresses=" + this.getAddresses() : "";
        String phonesString = this.getPhones() != null ? ", phones=" + this.getPhones() : "";
        String emailsString = this.getEmails() != null ? ", emails=" + this.getEmails() : "";;

        return "Contact(id=" + this.getId() +
                ", firstName=" + this.getFirstName() +
                ", middleName=" + this.getMiddleName() +
                ", lastName=" + this.getLastName() +
                ", company=" + this.getCompany() +
                ", birthDate=" + this.getBirthDate() +
                ", notes=" + this.getNotes() +
                ", photoContentType=" + this.getPhotoContentType() +
                ", phonesAggregate=" + this.getPhonesAggregate() +
                ", addressesAggregate=" + this.getAddressesAggregate() +
                ", emailsAggregate=" + this.getEmailsAggregate() +
                addressesString + phonesString + emailsString + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public void createAggregates() {

        if (this.emails != null) {
            this.emailsAggregate = this.emails.stream()
                    .map(Email::getAddress)
                    .collect(Collectors.joining(" | "));
        }

        if (this.addresses != null) {
            // default first
            this.addressesAggregate = "";
            this.addressesAggregate = this.addresses.stream()
                    .filter(Address::getDefaultAddress)
                    .map(address -> address.getStreet() + ", " + address.getPostalCode() + ", " + address.getCity())
                    .collect(Collectors.joining(" | "));
            this.addressesAggregate += this.addressesAggregate.isEmpty() || this.addresses.size() == 1? "" :  " | ";
            this.addressesAggregate += this.addresses.stream()
                    .filter(address -> !address.getDefaultAddress())
                    .map(address -> address.getStreet() + ", " + address.getPostalCode() + " " + address.getCity())
                    .collect(Collectors.joining(" | "));
        }

        if (this.phones != null) {
            this.phonesAggregate = this.phones.stream()
                    .map(Phone::getNumber)
                    .collect(Collectors.joining(" | "));
        }

    }


}
