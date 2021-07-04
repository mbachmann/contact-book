package com.example.contactbook.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Contact  {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] photo;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    private String lastName;

    private String company;

    @Temporal(TemporalType.DATE)
    private Calendar birthDate;

    private String notes;

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

    public Calendar getBirthDate() {
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

    public void setBirthDate(Calendar birthDate) {
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



    public String toString() {
        return "Contact(id=" + this.getId() + ", firstName=" + this.getFirstName() + ", middleName=" + this.getMiddleName() + ", lastName=" + this.getLastName() + ", company=" + this.getCompany() + ", birthDate=" + this.getBirthDate() + ", notes=" + this.getNotes() + ", phones=" + this.getPhones() + ", addresses=" + this.getAddresses() + ", emails=" + this.getEmails() + ")";
    }
}
