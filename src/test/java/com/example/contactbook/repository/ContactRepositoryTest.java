package com.example.contactbook.repository;

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

public class ContactRepositoryTest extends AbstractTest {

    @Autowired
    ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void getAllContacts() throws Exception {
        List<Contact> contacts = contactRepository.findAll();
        assertTrue(contacts.size() > 0);
    }

    @Test
    public void getAllContactViewsSmall() throws Exception {
        List<ContactViewSmall> contacts = contactRepository.findAllContactViewsSmall(Sort.by(Sort.Direction.ASC, "name"), null);
        assertTrue(contacts.size() > 0);
    }

    @Test
    public void getAllContactViews() throws Exception {
        List<ContactView> contacts = contactRepository.findAllContactViews(Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        assertTrue(contacts.size() > 0);
    }
    @Test

    public void getAllContactViewsPageable() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        Page<ContactView> contactsPage = contactRepository.findAllContactViews(sortedByName);

        assertTrue(contactsPage.getTotalElements() > 0);
        List<ContactView> contacts = contactsPage.getContent();
        assertTrue(contactsPage.getTotalElements() >= contacts.size());
    }


}
