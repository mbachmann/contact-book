package com.example.contactbook.repository;

import com.example.contactbook.model.ContactGroup;
import com.example.contactbook.model.ContactRelation;
import com.example.contactbook.model.enums.ContactRelationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the ContactRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRelationRepository extends JpaRepository<ContactRelation, Long> {

    @Query("Select distinct c FROM ContactRelation c left join fetch c.contacts where c.contactRelationType = :contactRelationType" )
    ContactRelation findContactRelationByContactRelationType(ContactRelationType contactRelationType);

    @Query("Select distinct new com.example.contactbook.model.ContactRelation(cr.id, cr.contactRelationType, count(co)) FROM ContactRelation cr left join cr.contacts as co GROUP BY cr.id, cr.contactRelationType " )
    Page<ContactRelation> findAllContactRelationsWithUsages(Pageable pageable);

    @Query("Select distinct new com.example.contactbook.model.ContactRelation(cr.id, cr.contactRelationType, count(co)) FROM ContactRelation cr left join cr.contacts as co where cr.id = :id GROUP BY cr.id, cr.contactRelationType " )
    Optional<ContactRelation> findContactRelationById(long id);

}
