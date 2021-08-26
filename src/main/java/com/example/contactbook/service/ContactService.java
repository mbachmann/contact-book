package com.example.contactbook.service;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewList;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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


    public Optional<Contact> findContactByIdWithEagerRelationships(Long id) {
        return contactRepository.findByIdWithEagerRelationships(id);
    }

    @Transactional
    public Optional<Contact> findContactEagerHibernateById(Long id) {
        Optional<Contact> contact= contactRepository.findById(id);

        if (contact.isPresent()) {
            Hibernate.initialize(contact.get().getPhones());
            Hibernate.initialize(contact.get().getAddresses());
            Hibernate.initialize(contact.get().getEmails());
            Hibernate.initialize(contact.get().getRelations());
            Hibernate.initialize(contact.get().getGroups());
        }
        return contact;
    }

    public Optional<Contact> findContactEagerById(Long id) {
        Optional<Contact> contact= contactRepository.findByIdWithEagerRelationships(id);

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

    public List<Contact> findAll(Sort sort) {
        return contactRepository.findAll(sort);
    }


    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }


    public Page<Contact> findAll(Pageable pageable, String filter) {

        if (filter == null) {
            return findAll(pageable);
        } else {
            List<Contact> contactListWithDuplicates = contactRepository.findAllWithFilter(pageable, filter);
            List<Contact> contacts = contactListWithDuplicates.stream()
                    .distinct()
                    .collect(Collectors.toList());
            int totalSize = contacts.size();
            return createPage(contacts, pageable, totalSize);
        }


    }

    public List<ContactView> findAllContactViews() {
        return contactRepository.findAllContactViews(Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
    }

    public Page<ContactView> findAllContactViews(Pageable pageable) {
        return contactRepository.findAllContactViews(pageable);
    }

    public List<ContactViewList> findAllContactViewsList(String filter, List<String> groups, List<String> relations) {
        if (groups == null) groups = Arrays.asList("*");
        if (relations == null) relations = Arrays.asList("*");
        return contactRepository.findAllContactViewsList(Sort.by(Sort.Direction.ASC, "name"), filter, groups, relations); //, groups);
    }

    public Page<ContactViewList> findAllContactViewsList(Pageable pageable, String filter, List<String> groups, List<String> relations) {
        if (groups == null) groups = Arrays.asList("*");
        if (relations == null) relations = Arrays.asList("*");
        return contactRepository.findAllContactViewsList(pageable, filter, groups, relations);
    }

    public List<Contact> findAllContactsWithEagerRelationships() {
        List<Contact> contactListWithDuplicates =  contactRepository.findAllWithEagerRelationships(Sort.by(Sort.Direction.ASC, "name"));
        return  contactListWithDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Page<Contact> findAllContactsWithEagerRelationships(Pageable pageable) {
        List<Contact> contactListWithDuplicates = contactRepository.findAllWithEagerRelationships(pageable);
        List<Contact> contacts = contactListWithDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
        int totalSize = contacts.size();
        return createPage(contacts, pageable, totalSize);
    }

    public Page<Contact> findAllContactsWithEagerRelationships(Pageable pageable, String filter) {

        if (filter == null) {
           return findAllContactsWithEagerRelationships(pageable);
        } else {
            List<Contact> contactListWithDuplicates = contactRepository.findAllWithEagerRelationships(pageable, filter);
            List<Contact> contacts = contactListWithDuplicates.stream()
                    .distinct()
                    .collect(Collectors.toList());
            int totalSize = contactRepository.countAllWithFilter(filter);
            return createPage(contacts, pageable, totalSize);
        }
    }

    private <T> Page<T> createPage(List<T> contacts, Pageable pageable, int totalSize) {
        int total = contacts.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), total);

        List<T> output = new ArrayList<>();
        if (start <= end) output = contacts.subList(start, end);

        return new PageImpl<>(output, pageable, totalSize);
    }


    public Contact findByFirstNameAndLastName(String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public boolean existsById(Long id) {
        return !contactRepository.existsById(id);
    }



}
