package com.example.contactbook.repository;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.ContactGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactGroupRepositoryTest extends AbstractTest {

    @Autowired
    ContactGroupRepository contactGroupRepository;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getContactGroupList() throws Exception {
        List<ContactGroup> contactGroups = contactGroupRepository.findAll();
        assertTrue(contactGroups.size() > 0);
        contactGroups.forEach(group -> getLogger().info(group.toString()));
    }

    @Test
    public void getContactGroupByName() throws Exception {
        ContactGroup contactGroup = contactGroupRepository.findContactGroupByName("A-Contacts");
        assertEquals(contactGroup.getName(), "A-Contacts");
        getLogger().info(contactGroup.toString());
    }

    @Test
    public void getContactGroupsWithUsage() throws Exception {
        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));
        Page<ContactGroup> contactGroup = contactGroupRepository.findAllContactGroupsWithUsages(sortedByName);
        assertTrue(contactGroup.getContent().size() > 0);
        contactGroup.forEach(group -> getLogger().info(group.toString()));
    }

    @Test
    public void getContactGroupWithUsage() throws Exception {
        long id = 1L;
        Optional<ContactGroup> contactGroupOptional = contactGroupRepository.findContactGroupById(id);
        contactGroupOptional.ifPresent(contactGroup -> {
            assertEquals(contactGroup.getId(), id);
            getLogger().debug(contactGroup.toString());
        });
    }
}
