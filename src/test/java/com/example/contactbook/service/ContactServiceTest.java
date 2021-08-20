package com.example.contactbook.service;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewSmall;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactServiceTest  extends AbstractTest {

    @Autowired
    ContactService contactService;

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
    public void getContactListSmall() throws Exception {
        List<ContactViewSmall> contacts = contactService.findAllContactViewsSmall(null);
        assertTrue(contacts.size() > 0);
        contacts.forEach(contact -> {
            System.out.println(contact.getId());
            System.out.println(contact.getName());
            System.out.println(contact.getAddressesAggregate());
            System.out.println(contact.getPhonesAggregate());
            System.out.println(contact.getEmailsAggregate());
        });
    }

    @Test
    public void getContactListSmallPageable() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));
        Page<ContactViewSmall> contactsPage = contactService.findAllContactViewsSmall(sortedByName, null);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactViewSmall> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }

    @Test
    public void getContactListSmallPageableFilter() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));
        Page<ContactViewSmall> contactsPage = contactService.findAllContactViewsSmall(sortedByName, "Anna");

        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactViewSmall> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());

       contactsPage = contactService.findAllContactViewsSmall(sortedByName, "Basel");

        assertTrue(contactsPage.getTotalElements() > 0);
        contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
        contacts.forEach(contact -> {
            System.out.println("Filter to Basel");
            System.out.println(contact.getId());
            System.out.println(contact.getName());
            System.out.println(contact.getAddressesAggregate());
            System.out.println(contact.getPhonesAggregate());
            System.out.println(contact.getEmailsAggregate());
        });
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
}
