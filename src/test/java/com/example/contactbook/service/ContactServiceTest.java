package com.example.contactbook.service;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.Phone;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewList;
import com.example.contactbook.repository.CodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactServiceTest extends AbstractTest {

    @Autowired
    ContactService contactService;

    @Autowired
    CodeRepository codeRepository;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void getAll() {
        List<Contact> contacts = contactService.findAll();
        assertTrue(contacts.size() > 0);
    }

    @Test
    public void getAllPageable() {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        Page<Contact> contactsPage = contactService.findAll(sortedByName);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }


    @Test
    public void getAllContactViews() {
        List<ContactView> contacts = contactService.findAllContactViews();
        assertTrue(contacts.size() > 0);
    }

    @Test

    public void getAllContactViewsPageable() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        Page<ContactView> contactsPage = contactService.findAllContactViews(sortedByName);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactView> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getContactViewsList() throws Exception {
        List<ContactViewList> contacts = contactService.findAllContactViewsList(null, Arrays.asList("A-Contacts", "B-Contacts"), null);
        assertTrue(contacts.size() > 0);
        printContacts(contacts, "A-Contacts, B-Contacts");
    }

    @Test
    public void getContactViewsListPageable() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        Page<ContactViewList> contactsPage = contactService.findAllContactViewsList(sortedByName, null, Arrays.asList("A-Contacts", "B-Contacts"), null);
        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactViewList> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
        printContacts(contacts, "Paged: A-Contacts, B-Contacts");

        contactsPage = contactService.findAllContactViewsList(sortedByName, "1991", null, null);
        assertTrue(contactsPage.getTotalElements() > 0);
        contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
        printContacts(contacts, "Paged: 1991");

    }

    @Test
    public void getContactViewsListPageableFilter() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));
        Page<ContactViewList> contactsPage = contactService.findAllContactViewsList(sortedByName, "Anna", Arrays.asList("A-Contacts", "B-Contacts"), null);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactViewList> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());

        contactsPage = contactService.findAllContactViewsList(sortedByName, "Basel", Arrays.asList("A-Contacts", "B-Contacts"), null);

        assertTrue(contactsPage.getTotalElements() > 0);
        contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
        printContacts(contacts, "Paged: Basel " + "A-Contacts, B-Contacts");
    }


    @Test
    public void getContactListSmallWithEager() throws Exception {
        List<Contact> contacts = contactService.findAllContactsWithEagerRelationships();
        assertTrue(contacts.size() > 0);
    }

    @Test
    public void getAllContactsWithEagerRelationships() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getAllContactsWithEagerRelationshipsWithFilter() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "Anna";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());

        filter = "nna";

        contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertTrue(contactsPage.getTotalElements() > 0);
        contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getAllContactsWithEagerRelationshipsAddress() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "Basel";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void AllContactsWithEagerRelationshipsEmail() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "max.mustermann@example.com";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getAllContactsWithEagerRelationshipsPhone() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "+41 61 812 34 56";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getAllContactsWithEagerRelationshipsGroup() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "Mustermann";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getAllContactsWithEagerRelationshipsRelation() {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));

        String filter = "+41 61 812 34 56";

        Page<Contact> contactsPage = contactService.findAllContactsWithEagerRelationships(sortedByName, filter);
        assertEquals(1, contactsPage.getTotalElements());
        List<Contact> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getContactEagerWithDbAndHibernateById() throws JsonProcessingException {

        Optional<Contact> eagerContactHibernate = contactService.findContactEagerHibernateById(1L);
        eagerContactHibernate.ifPresent(contact -> getLogger().debug(contact.toString()));

        Optional<Contact> eagerContact = contactService.findContactEagerById(1L);
        eagerContact.ifPresent(contact -> getLogger().debug(contact.toString()));

        assertEquals(mapToJson(eagerContactHibernate), mapToJson(eagerContact));
    }

    @Test
    public void createUpdateAndDeleteContact() throws JsonProcessingException {

        long id = 1;
        Optional<Contact> eagerContactHibernate = contactService.findContactEagerHibernateById(id);
        eagerContactHibernate.ifPresent(contact -> getLogger().debug(contact.toString()));

        Optional<Contact> eagerContact = contactService.findContactEagerById(id);
        eagerContact.ifPresent(contact -> {
            try {
                getLogger().debug(contact.toString());
                String jsonOriginalContact = mapToJson(contact);

                // change contact
                contact.setFirstName("Anna-Maria");
                if  (!contact.getAddresses().isEmpty()) {
                    Address address = contact.getAddresses().iterator().next();
                    address.setCity("Andermatt");
                    address.setPostalCode("6490");
                }
                contact.addAddress(createAddress("Rosenweg 3", "4000", "Basel"));
                contact.addEmail(createEmail("hallo@example.com"));
                contact.addEmail(createEmail("test@example.com"));
                contact.addPhone(createPhone("+41 45 678 34 12"));
                contact.addPhone(createPhone("+41 62 678 34 12"));
                Contact savedContact = contactService.save(contact);
                Contact reloadedContact = contactService.findContactEagerHibernateById(id).orElseThrow();
                assertEquals(mapToJson(savedContact), mapToJson(reloadedContact));

                // Restore the Original
                contact = mapFromJson(jsonOriginalContact, Contact.class);
                savedContact = contactService.save(contact);
                Contact originalContact = contactService.findContactEagerHibernateById(id).orElseThrow();
                assertEquals(mapToJson(savedContact), mapToJson(originalContact));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        assertEquals(mapToJson(eagerContactHibernate), mapToJson(eagerContact));


    }

    public Address createAddress(String street, String postalCode, String city) {
        AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(),"Home");
        return new Address(street, postalCode, city, "CH",false , addressType);
    }

    public Email createEmail(String eMailAddress) {
        EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(),"Home");
        return new Email(eMailAddress, emailType);
    }

    public Phone createPhone(String phoneNumer) {
        PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(),"Home");
        return new Phone(phoneNumer, phoneType);
    }


}
