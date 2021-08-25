package com.example.contactbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@NamedEntityGraph(
        name = "contact-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("addresses"),
                @NamedAttributeNode("emails"),
                @NamedAttributeNode("phones"),
                @NamedAttributeNode("groups"),
                @NamedAttributeNode("relations"),
        }
)

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

    @JsonIgnoreProperties
    private String phonesAggregate;
    @JsonIgnoreProperties
    private String addressesAggregate;
    @JsonIgnoreProperties
    private String emailsAggregate;
    @JsonIgnoreProperties
    private String groupsIdAggregate;
    @JsonIgnoreProperties
    private String relationsIdAggregate;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL )
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Email> emails = new HashSet<>();

    /**
     * type of contact's relation; possible values: type of contact's relation; possible values: CL - Customer, CR - Creditor, TE - TEAM, OF - Authorities (Behörden), ME - Medical, OT - Others
     */
    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
            name = "rel_contact__relations",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "relations_id")
    )
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<ContactRelation> relations = new HashSet<>();

    /**
     * contact groups
     */
    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
            name = "rel_contact__groups",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<ContactGroup> groups = new HashSet<>();

    public Contact() {
    }

    public Contact(String firstName, String lastName, String middleName, LocalDate birthDate, String company, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.company = company;
        this.notes = notes;
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

    public Set<ContactRelation> getRelations() {
        return this.relations;
    }

    public Contact relations(Set<ContactRelation> contactRelations) {
        this.setRelations(contactRelations);
        return this;
    }

    public Contact addRelation(ContactRelation contactRelation) {
        if (this.relations == null) relations = new HashSet<>();
        this.relations.add(contactRelation);
        contactRelation.getContacts().add(this);
        return this;
    }

    public Contact removeRelation(ContactRelation contactRelation) {
        this.relations.remove(contactRelation);
        contactRelation.getContacts().remove(this);
        return this;
    }

    public void setRelations(Set<ContactRelation> contactRelations) {
        this.relations = contactRelations;
    }

    public Set<ContactGroup> getGroups() {
        return this.groups;
    }

    public Contact groups(Set<ContactGroup> contactGroups) {
        this.setGroups(contactGroups);
        return this;
    }

    public Contact addGroup(ContactGroup contactGroup) {
        if (this.groups == null) groups = new HashSet<>();
        this.groups.add(contactGroup);
        contactGroup.getContacts().add(this);
        return this;
    }

    public Contact removeGroup(ContactGroup contactGroup) {
        this.groups.remove(contactGroup);
        contactGroup.getContacts().remove(this);
        return this;
    }

    public void setGroups(Set<ContactGroup> contactGroups) {
        this.groups = contactGroups;
    }

    public Contact addAddress(Address address) {
        this.addresses.add(address);
        return this;
    }

    public Contact removeAddress(Address address) {
        this.addresses.remove(address);
        return this;
    }

    public Contact addPhone(Phone phone) {
        this.phones.add(phone);
        return this;
    }

    public Contact removePhone(Phone phone) {
        this.phones.remove(phone);
        return this;
    }

    public Contact addEmail(Email email) {
        this.emails.add(email);
        return this;
    }

    public Contact removeEmail(Email email) {
        this.emails.remove(email);
        return this;
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

        if (this.groups != null) {
            this.groupsIdAggregate = this.groups.stream()
                    .map(group -> group.getId().toString())
                    .collect(Collectors.joining(","));
        }

        if (this.relations != null) {
            this.relationsIdAggregate = this.relations.stream()
                    .map(relation -> relation.getId().toString())
                    .collect(Collectors.joining(","));
        }
    }


}
