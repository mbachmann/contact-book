package com.example.contactbook.model.codes;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.enums.CodeType;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "Code")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type", "title"}),
        @UniqueConstraint(columnNames = {"type", "shortCut"})
})
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    protected Long id;

    @Column(name = "type", insertable = false, updatable = false)
    protected String type;

    @Column(nullable = false)
    protected String title;

    @Column
    protected String shortCut;

    @Column(nullable = false)
    protected Boolean active = true;

    @Version
    private int version;

    public Code() {
    }

    public Code(String title, String shortCut) {
        this.title = title;
        this.shortCut = shortCut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Code)) {
            return false;
        }
        return id != null && id.equals(((Code) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
