package com.example.contactbook.repository;

import com.example.contactbook.model.ContactGroup;
import com.example.contactbook.model.ContactRelation;
import com.example.contactbook.model.enums.ContactRelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRelationRepository extends JpaRepository<ContactRelation, Long> {

    @Query("Select distinct c FROM ContactRelation c left join fetch c.contacts where c.contactRelationType = :contactRelationType" )
    ContactRelation findContactRelationByContactRelationType(ContactRelationType contactRelationType);
}
