package com.example.contactbook.domain;

import com.example.contactbook.TestUtil;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.Phone;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.utils.ImageProcessing;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);
        contact2.setId(2L);
        assertThat(contact1).isNotEqualTo(contact2);
        contact1.setId(null);
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    void toStringVerifier() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Anna");
        contact.setLastName("Muster");
        contact.setMiddleName("Marta");
        contact.setBirthDate(LocalDate.of(2000, 12,12));
        contact.setCompany("Example Company Ltd");
        contact.setNotes("First Contact");

        contact.setPhoto(TestUtil.readImageFromResource("image/firstContact.png"));
        contact.setPhotoContentType("image/png");
        contact.setThumbNail(ImageProcessing.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());
        String temp =  "Contact(id=null, firstName=Anna, middleName=Marta, lastName=Muster, company=Example Company Ltd, birthDate=2000-12-12, notes=First Contact, photoContentType=image/png, phonesAggregate=null, addressesAggregate=null, emailsAggregate=null, addresses=[], phones=[], emails=[])";
        assertThat(temp).isEqualTo(contact.toString());

        Address address = new Address();
        address.setCity("Basel");
        address.setCountry("Schweiz");
        address.setPostalCode("4000");
        address.setStreet("Peter Merian");
        AddressType addressType = new AddressType("Home","H");
        address.setAddressType(addressType);
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        contact.setAddresses(addresses);

        Phone phone = new Phone();
        phone.setNumber("+41 61 812 34 56");
        PhoneType phoneType = new PhoneType("PhoneType","Home");
        phone.setPhoneType(phoneType);
        Set<Phone> phones = new HashSet<>();
        phones.add(phone);
        contact.setPhones(phones);

        Email email = new Email();
        email.setAddress("max.mustermann@example.com");
        EmailType emailType = new EmailType("EmailType","Home");
        email.setEmailType(emailType);
        Set<Email> emails = new HashSet<>();
        emails.add(email);
        contact.setEmails(emails);
         temp = "Contact(id=null, firstName=Anna, middleName=Marta, lastName=Muster, company=Example Company Ltd, birthDate=2000-12-12, notes=First Contact, photoContentType=image/png, "
                 + "phonesAggregate=null, addressesAggregate=null, emailsAggregate=null, addresses=[Address(id=null, street=Peter Merian, additional=null, postalCode=4000, city=Basel, country=Schweiz, type=Home, H)], "
                 + "phones=[Phone(id=null, number=+41 61 812 34 56, type=PhoneType, Home)], emails=[Email(id=null, address=max.mustermann@example.com, type=EmailType, Home)])";
        assertThat(temp).isEqualTo(contact.toString());
    }
}
