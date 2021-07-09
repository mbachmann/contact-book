package com.example.contactbook.model;

import com.example.contactbook.model.codes.EmailType;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class Email implements Serializable {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(optional = false)
    private EmailType emailType;

    @Column(nullable = false)
    private String address;

    @Pattern(regexp = "\\w+@\\w+\\.\\w+(,\\s*\\w+@\\w+\\.\\w+)*")
    @Size(min = 2, max = 40)
    public Email() {
    }

    public Long getId() {
        return this.id;
    }


    public String getAddress() {
        return this.address;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return "Email(id=" + this.getId() + ", type=" + this.getEmailType() + ", address=" + this.getAddress() + ")";
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }
}
