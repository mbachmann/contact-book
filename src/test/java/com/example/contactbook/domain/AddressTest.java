package com.example.contactbook.domain;

import com.example.contactbook.TestUtil;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.EmailType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = new Address();
        address1.setId(1L);
        Address address2 = new Address();
        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);
        address2.setId(2L);
        assertThat(address1).isNotEqualTo(address2);
        address1.setId(null);
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void toStringVerifier() throws Exception {
        Address address = new Address();
        address.setCity("Basel");
        address.setCountry("Schweiz");
        address.setPostalCode("4000");
        address.setStreet("Peter Merian");
        String temp = "Address(id=null, street=Peter Merian, additional=null, postalCode=4000, city=Basel, country=Schweiz)";
        assertThat(temp).isEqualTo(address.toString());

        AddressType addressType = new AddressType("Home","H");
        address.setAddressType(addressType);

        temp = "Address(id=null, street=Peter Merian, additional=null, postalCode=4000, city=Basel, country=Schweiz, type=Home, H)";
        assertThat(temp).isEqualTo(address.toString());
    }
}
