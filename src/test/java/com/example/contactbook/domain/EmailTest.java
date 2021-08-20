package com.example.contactbook.domain;

import com.example.contactbook.TestUtil;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.Phone;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Email.class);
        Email email1 = new Email();
        email1.setId(1L);
        Email email2 = new Email();
        email2.setId(email1.getId());
        assertThat(email1).isEqualTo(email2);
        email2.setId(2L);
        assertThat(email1).isNotEqualTo(email2);
        email1.setId(null);
        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    void toStringVerifier() throws Exception {
        Email email = new Email();
        email.setAddress("max.mustermann@example.com");

        String temp = "Email(id=null, address=max.mustermann@example.com)";
        assertThat(temp).isEqualTo(email.toString());

        EmailType emailType = new EmailType("EmailType","Home");
        email.setEmailType(emailType);

        temp = "Email(id=null, address=max.mustermann@example.com, type=EmailType, Home)";
        assertThat(temp).isEqualTo(email.toString());
    }
}
