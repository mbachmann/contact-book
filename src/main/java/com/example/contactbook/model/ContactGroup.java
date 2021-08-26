package com.example.contactbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The group a contact belongs to
 */
@Entity
@Table(name = "contact_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    public ContactGroup () {
    }

    public ContactGroup (Long id, String name, Long usage) {
        this.id = id;
        this.name = name;
        this.usage = usage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Name of the contact group
     */
    @NotNull
    @Column(nullable = false)
    private String name;

    /**
     * how many contacts are assigned to this group ,
     */
    private Long usage;

    @ManyToMany(mappedBy = "groups")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relations", "groups" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactGroup id(Long id) {
        this.id = id;
        return this;
    }


    public String getName() {
        return this.name;
    }

    public ContactGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUsage() {
        return this.usage;
    }

    public ContactGroup usage(Long usage) {
        this.usage = usage;
        return this;
    }

    public void setUsage(Long usage) {
        this.usage = usage;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public ContactGroup contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public ContactGroup addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.getGroups().add(this);
        return this;
    }

    public ContactGroup removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.getGroups().remove(this);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.removeGroup(this));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.addGroup(this));
        }
        this.contacts = contacts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactGroup)) {
            return false;
        }
        return id != null && id.equals(((ContactGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", usage=" + getUsage() +
            "}";
    }
}
