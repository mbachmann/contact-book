package com.example.contactbook.repository;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewSmall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {

    @Query("SELECT distinct c.id AS id,  c.company as company, c.lastName as lastName, c.firstName as firstName,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, c.photoContentType AS photoContentType, c.addressesAggregate AS addressesAggregate, c.phonesAggregate AS phonesAggregate, c.emailsAggregate AS emailsAggregate FROM Contact c " +
            "WHERE :filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) ")
    List<ContactViewSmall> findAllContactViewsSmall(Sort sort, String filter);
    @Query("SELECT distinct c.id AS id,  c.company as company, c.lastName as lastName, c.firstName as firstName,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, c.photoContentType AS photoContentType, c.addressesAggregate AS addressesAggregate, c.phonesAggregate AS phonesAggregate, c.emailsAggregate AS emailsAggregate FROM Contact c " +
            "WHERE :filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) ")
    List<ContactViewSmall> findAllContactViewsSmall(Pageable page, String filter);

    @Query("select c from Contact c")
    List<ContactView> findAllContactViews(Sort sort);
    @Query("select c from Contact c")
    Page<ContactView> findAllContactViews(Pageable page);

    Contact findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones")
    List<Contact> findAllWithEagerRelationships(Sort sort);

    @Query(
             value = "select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name from Contact c  " +
                     "left join fetch c.addresses as a  left join fetch c.emails as e left join fetch c.phones as p " +
                     "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,'')) LIKE %:filter%) " +
                     "OR (a is not empty AND CONCAT(COALESCE(a.city,''), ' ', COALESCE(a.street,''), ' ', COALESCE(a.postalCode,''), ' ', COALESCE(a.country,'')) LIKE %:filter%)" +
                     "OR (e is not empty AND COALESCE(e.address,'') LIKE %:filter%)" +
                     "OR (p is not empty AND COALESCE(p.number,'') LIKE %:filter%)" ,
             countQuery = "select count(distinct contact) from Contact contact"
    )
    List<Contact> findAllWithEagerRelationships(Pageable page, @Param("filter") String filter);


    @Query(
            value = "select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c  " +
                    "left join fetch c.addresses left join fetch c.emails left join fetch c.phones",
            countQuery = "select count(distinct contact) from Contact contact"
    )
    Page<Contact> findAllWithEagerRelationships(Pageable page);

    @Query("select c from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones where c.id =:id")
    Contact findByIdWithEagerRelationships(@Param("id") Long id);

}
