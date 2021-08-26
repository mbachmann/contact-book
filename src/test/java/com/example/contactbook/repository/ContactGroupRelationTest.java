package com.example.contactbook.repository;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.ContactGroup;
import com.example.contactbook.model.ContactRelation;
import com.example.contactbook.model.enums.ContactRelationType;
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

public class ContactGroupRelationTest extends AbstractTest {

    @Autowired
    ContactRelationRepository contactRelationRepository;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void getContactRelationList() throws Exception {
        List<ContactRelation> contactRelations = contactRelationRepository.findAll();
        assertTrue(contactRelations.size() > 0);
        contactRelations.forEach(relation -> getLogger().info(relation.toString()));
    }

    @Test
    public void getContactRelationByName() throws Exception {
        ContactRelation contactRelation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
        assertEquals(contactRelation.getContactRelationType(), ContactRelationType.CREDITOR);
        getLogger().info(contactRelation.toString());
    }

    @Test
    public void getContactRelationWithUsage() throws Exception {
        Pageable sortedByContactRelationType = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "contactRelationType"));
        Page<ContactRelation> contactGroups = contactRelationRepository.findAllContactRelationsWithUsages(sortedByContactRelationType);
        assertTrue(contactGroups.getContent().size() > 0);
        contactGroups.getContent().forEach(group -> getLogger().info(group.toString()));
    }
}
