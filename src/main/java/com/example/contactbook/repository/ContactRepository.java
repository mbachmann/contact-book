package com.example.contactbook.repository;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.ContactSmall;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT t.id AS id, CONCAT(t.lastName, ', ', t.firstName) AS name FROM Contact t")
    List<ContactSmall> findContactList(Sort sort);
    Contact findByFirstNameAndLastName(String firstname, String lastname);

}
