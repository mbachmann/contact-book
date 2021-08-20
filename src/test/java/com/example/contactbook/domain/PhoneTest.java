package com.example.contactbook.domain;

import com.example.contactbook.TestUtil;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Phone;
import com.example.contactbook.model.codes.PhoneType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phone.class);
        Phone phone1 = new Phone();
        phone1.setId(1L);
        Phone phone2 = new Phone();
        phone2.setId(phone1.getId());
        assertThat(phone1).isEqualTo(phone2);
        phone2.setId(2L);
        assertThat(phone1).isNotEqualTo(phone2);
        phone1.setId(null);
        assertThat(phone1).isNotEqualTo(phone2);
    }

    @Test
    void toStringVerifier() throws Exception {
        Phone phone = new Phone();
        phone.setNumber("+41 61 812 34 56");
        String temp = "Phone(id=null, number=+41 61 812 34 56)";
        assertThat(temp).isEqualTo(phone.toString());

        PhoneType phoneType = new PhoneType("PhoneType","Home");
        phone.setPhoneType(phoneType);
        temp = "Phone(id=null, number=+41 61 812 34 56, type=PhoneType, Home)";
        assertThat(temp).isEqualTo(phone.toString());
    }
}
