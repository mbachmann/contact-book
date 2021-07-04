package com.example.contactbook.service;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.ContactSmall;
import com.example.contactbook.repository.ContactRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service

public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact edit(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        return contactRepository.saveAll(contacts);
    }

    @Override
    public List<ContactSmall> findAllContacts() {
        return contactRepository.findContactList(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    @Transactional
    public Contact findContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            Hibernate.initialize(contact.getPhones());
            Hibernate.initialize(contact.getAddresses());
            Hibernate.initialize(contact.getEmails());
        }
        return contact;
    }

    @Override
    public boolean deleteContact(Long id) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            contactRepository.delete(contact);
            return true;
        }

        return false;
    }
}
