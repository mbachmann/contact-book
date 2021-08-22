package com.example.contactbook.model;

import com.example.contactbook.model.enums.ContactRelationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContactRelation.
 */
@Entity
public class ContactRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * type of contact's relation; possible values: type of contact's relation; possible values: CL - Customer, CR - Creditor, TE - TEAM, OF - Authorities (Behörden), ME - Medical, OT - Others
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_relation_type")
    private ContactRelationType contactRelationType;

    private String contactRelationValue;

    @ManyToMany(mappedBy = "relations")
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

    public ContactRelation id(Long id) {
        this.id = id;
        return this;
    }

    public ContactRelationType getContactRelationType() {
        return this.contactRelationType;
    }

    public String getContactRelationValue() {
        return this.contactRelationValue;
    }

    public ContactRelation contactRelationType(ContactRelationType contactRelationType) {
        this.contactRelationType = contactRelationType;
        return this;
    }

    public void setContactRelationType(ContactRelationType contactRelationType) {
        this.contactRelationType = contactRelationType;
        this.contactRelationValue = contactRelationType.getValue();
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public ContactRelation contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public ContactRelation addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.getRelations().add(this);
        return this;
    }

    public ContactRelation removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.getRelations().remove(this);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.removeRelations(this));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.addRelations(this));
        }
        this.contacts = contacts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactRelation)) {
            return false;
        }
        return id != null && id.equals(((ContactRelation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactRelation{" +
            "id=" + getId() +
            ", contactRelationType='" + getContactRelationType() + "'" +
            "}";
    }
}
