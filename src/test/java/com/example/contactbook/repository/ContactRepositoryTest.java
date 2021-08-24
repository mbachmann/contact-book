package com.example.contactbook.repository;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.QContact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewList;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactRepositoryTest extends AbstractTest {

    @Autowired
    ContactRepository contactRepository;


    @Autowired
    private EntityManager entityManager;

    private QuerydslJpaPredicateExecutor<Contact> predicateExecutor;

    @BeforeEach
    public void setUp() {

        JpaEntityInformation<Contact, Long> information = new JpaMetamodelEntityInformation<>(Contact.class, entityManager.getMetamodel());
        this.predicateExecutor = new QuerydslJpaPredicateExecutor<>(information, entityManager, SimpleEntityPathResolver.INSTANCE, null);
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
    public void getAllContactViewsList() throws Exception {
        List<ContactViewList> contacts = contactRepository.findAllContactViewsList(Sort.by(Sort.Direction.ASC, "name"), null, Arrays.asList("*"), Arrays.asList("*")); // , Arrays.asList("A-Contacts"));
        assertTrue(contacts.size() > 0);
        printContacts(contacts, "Repository Test Filter to *");

    }

    @Test
    public void getAllContactViewsListNoGroup() throws Exception {
        List<ContactViewList> contacts = contactRepository.findAllContactViewsList(Sort.by(Sort.Direction.ASC, "name"), null, Arrays.asList("-"), Arrays.asList("*")); // , Arrays.asList("A-Contacts"));
        assertTrue(contacts.size() > 0);
        printContacts(contacts, "Repository Test Filter to - (no Group)");
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

    @Test
    void givenContactsCreatedWhenQueriesForContactsThenGetTheCount() {
        QContact contact = QContact.contact;
        JPAQuery<QContact> query1 = new JPAQuery<>(entityManager);
        query1.from(contact).where(contact.firstName.eq("Anna"));
        assertThat(query1.fetch().size()).isEqualTo(1);
    }

    @Test
    void givenContactsCreatedWhenPageableAndEager() {
        QContact contact = QContact.contact;
        JPQLQuery<?> jpaQuery = new JPAQuery<>(entityManager);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "company", "lastName", "firstName"));

        PathBuilder<Contact> entityPath = new PathBuilder<>(Contact.class, "contact");
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = entityPath.get(order.getProperty());
            jpaQuery.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name()), path));
        }

        // Predicate predicate = contact.firstName.eq("Anna") ;
        Predicate predicate = contact.groups.any().name.eq("A-Contacts");
        jpaQuery.from(contact)
                .leftJoin(contact.addresses).fetchJoin()
                .leftJoin(contact.phones).fetchJoin()
                .leftJoin(contact.emails).fetchJoin()
                .leftJoin(contact.relations).fetchJoin()
                .leftJoin(contact.groups).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(predicate);

        List<Contact> contacts = (List<Contact>) jpaQuery.fetch();

        assertThat(contacts.size()).isGreaterThan(0);

        // Create a Page information
        JPQLQuery<?> jpaNewQuery = new JPAQuery<>(entityManager);
        long totalSize = jpaNewQuery.from(contact).where(predicate).fetchCount();
        Page<Contact> page =  new PageImpl<>(contacts, pageable, totalSize);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(contacts.size());
    }

    @Test
    void givenContactsCreatedWhenQueriesForContactsPageable() {
        QContact contact = QContact.contact;

        Pageable sortedByName = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "lastName", "firstName"));
        Page<Contact> contacts = predicateExecutor.findAll(contact.firstName.eq("Anna"), sortedByName);

        assertThat(contacts.getTotalElements()).isGreaterThan(0);

        Page<Contact> contacts2 = contactRepository.findAll(contact.firstName.eq("Anna"), sortedByName);
        assertThat(contacts2.getTotalElements()).isGreaterThan(0);
    }

}
