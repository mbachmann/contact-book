package com.example.contactbook.service;


import com.example.contactbook.model.Contact;
import com.example.contactbook.model.ContactSmall;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact edit(Contact contact);

    List<Contact> saveAll(List<Contact> contacts);

    List<ContactSmall> findAllContacts();

    Contact findContactById(Long id);

    boolean deleteContact(Long id);
}
