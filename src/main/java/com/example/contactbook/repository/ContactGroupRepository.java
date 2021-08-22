package com.example.contactbook.repository;

import com.example.contactbook.model.ContactGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {

    @Query("Select distinct c FROM ContactGroup c left join fetch c.contacts where c.name = :groupName" )
    ContactGroup findContactGroupByName(String groupName);

}
