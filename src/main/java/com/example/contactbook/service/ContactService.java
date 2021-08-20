package com.example.contactbook.service;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewSmall;
import com.example.contactbook.repository.ContactRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService  {

    @Autowired
    private ContactRepository contactRepository;


    public Contact save(Contact contact) {
        contact.createAggregates();
        return contactRepository.save(contact);
    }


    public Contact edit(Contact contact) {
        contact.createAggregates();
        return contactRepository.save(contact);
    }


    public List<Contact> saveAll(List<Contact> contacts) {
        contacts.forEach(Contact::createAggregates);
        return contactRepository.saveAll(contacts);
    }


    public Contact findContactByIdWithEagerRelationships(Long id) {
        return contactRepository.findByIdWithEagerRelationships(id);
    }

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


    public boolean deleteContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact != null) {
            contactRepository.delete(contact);
            return true;
        }
        return false;
    }


    public List<Contact> findAll() {
        return contactRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
    }

    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    public List<ContactView> findAllContactViews() {
        return contactRepository.findAllContactViews(Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
    }

    public Page<ContactView> findAllContactViews(Pageable pageable) {
        return contactRepository.findAllContactViews(pageable);
    }

    public List<ContactViewSmall> findAllContactViewsSmall(String filter) {
        return contactRepository.findAllContactViewsSmall(Sort.by(Sort.Direction.ASC, "name"), filter);
    }

    public Page<ContactViewSmall> findAllContactViewsSmall(Pageable pageable, String filter) {
        List<ContactViewSmall> contacts =  contactRepository.findAllContactViewsSmall(pageable, filter);
        return createPage(contacts, pageable);
    }

    public List<Contact> findAllContactsWithEagerRelationships() {
        List<Contact> contactListWithDuplicates =  contactRepository.findAllWithEagerRelationships(Sort.by(Sort.Direction.ASC, "name"));
        return  contactListWithDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Page<Contact> findAllContactsWithEagerRelationships(Pageable pageable) {
        return contactRepository.findAllWithEagerRelationships(pageable);
    }

    public Page<Contact> findAllContactsWithEagerRelationships(Pageable pageable, String filter) {

        if (filter == null || filter.isEmpty()) {
           return findAllContactsWithEagerRelationships(pageable);
        } else {
            List<Contact> contactListWithDuplicates = contactRepository.findAllWithEagerRelationships(pageable, filter);
            List<Contact> contacts = contactListWithDuplicates.stream()
                    .distinct()
                    .collect(Collectors.toList());
            return createPage(contacts, pageable);
        }
    }

    private <T> Page<T> createPage(List<T> contacts, Pageable pageable) {
        int total = contacts.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), total);

        List<T> output = new ArrayList<>();
        if (start <= end) output = contacts.subList(start, end);

        return new PageImpl<>(output, pageable, total);
    }


    public Contact findByFirstNameAndLastName(String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
