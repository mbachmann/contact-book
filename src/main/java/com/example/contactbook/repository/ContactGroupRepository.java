package com.example.contactbook.repository;

import com.example.contactbook.model.ContactGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ContactGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {

    @Query("Select distinct c FROM ContactGroup c left join fetch c.contacts where c.name = :groupName" )
    ContactGroup findContactGroupByName(String groupName);

    @Query("Select distinct new com.example.contactbook.model.ContactGroup(cg.id, cg.name, count(co)) FROM ContactGroup cg left join cg.contacts as co GROUP BY cg.id, cg.name " )
    Page<ContactGroup> findAllContactGroupsWithUsages(Pageable pageable);
}
