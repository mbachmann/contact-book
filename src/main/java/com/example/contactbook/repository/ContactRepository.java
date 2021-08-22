package com.example.contactbook.repository;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewList;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long>, QuerydslPredicateExecutor<Contact> {

    @Query("SELECT distinct c.id AS id,  c.company as company, c.lastName as lastName, c.firstName as firstName,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, c.photoContentType AS photoContentType, c.addressesAggregate AS addressesAggregate, c.phonesAggregate AS phonesAggregate, c.emailsAggregate AS emailsAggregate,  c.groupsIdAggregate AS groupsIdAggregate, c.relationsIdAggregate AS relationsIdAggregate FROM Contact c " +
            "WHERE (:filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) ) " +
            "AND (EXISTS (SELECT 1 FROM c.groups g WHERE COALESCE(g.name,'') IN (:groups)  ) OR '*' IN (:groups) OR (SIZE(c.groups)=0 AND '-' IN (:groups)))" )

    List<ContactViewList> findAllContactViewsList(Sort sort, String filter,  List<String> groups);
    @Query("SELECT distinct c.id AS id,  c.company as company, c.lastName as lastName, c.firstName as firstName,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, c.photoContentType AS photoContentType, c.addressesAggregate AS addressesAggregate, c.phonesAggregate AS phonesAggregate, c.emailsAggregate AS emailsAggregate,  c.groupsIdAggregate AS groupsIdAggregate, c.relationsIdAggregate AS relationsIdAggregate FROM Contact c " +
            "WHERE (:filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) ) " +
            "AND (EXISTS (SELECT 1 FROM c.groups g WHERE COALESCE(g.name,'') IN (:groups)  ) OR '*' IN (:groups) OR (SIZE(c.groups)=0 AND '-' IN (:groups)))" )

    List<ContactViewList> findAllContactViewsList(Pageable page, String filter,  List<String> groups);

    @Query("select c from Contact c")
    List<ContactView> findAllContactViews(Sort sort);
    @Query("select c from Contact c")
    Page<ContactView> findAllContactViews(Pageable page);

    Contact findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones left join fetch c.groups left join fetch c.relations")
    List<Contact> findAllWithEagerRelationships(Sort sort);

    @Query(
            value = "select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c  " +
                    "left join fetch c.addresses left join fetch c.emails left join fetch c.phones left join fetch c.groups left join fetch c.relations",
            countQuery = "select count(distinct contact) from Contact contact"
    )
    Page<Contact> findAllWithEagerRelationships(Pageable page);

    /**
     * **** DO NOT USE THIS QUERY ****
     * Query with Eager Relations. The query might produce duplicates due to cartesian product. The query is inefficient. The countQuery will return the count of all contacts and not only the filtered
     * @param pageable the paging and sorting information
     * @param filter the filter string
     * @return Contact information within a Page Object (total might be wrong - higher than with filter)
     */
    @Query(
             value = "select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name from Contact c  " +
                     "left join fetch c.addresses as a  left join fetch c.emails as e left join fetch c.phones as p left join fetch c.groups as g left join fetch c.relations as r " +
                     "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,'')) LIKE %:filter%) " +
                     "OR (a is not empty AND CONCAT(COALESCE(a.city,''), ' ', COALESCE(a.street,''), ' ', COALESCE(a.postalCode,''), ' ', COALESCE(a.country,'')) LIKE %:filter%)" +
                     "OR (e is not empty AND COALESCE(e.address,'') LIKE %:filter%)" +
                     "OR (p is not empty AND COALESCE(p.number,'') LIKE %:filter%)" +
                     "OR (g is not empty AND COALESCE(g.name,'') LIKE %:filter%)" +
                     "OR (r is not empty AND COALESCE(r.contactRelationValue,'') LIKE %:filter%)" ,
             countQuery = "select count(distinct contact) from Contact contact"
    )
    List<Contact> findAllWithEagerRelationships(Pageable pageable, @Param("filter") String filter);

    @Query("select c from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones  left join fetch c.groups left join fetch c.relations where c.id =:id")
    Contact findByIdWithEagerRelationships(@Param("id") Long id);

    @EntityGraph(value = "contact-entity-graph")
    @NonNull Page<Contact> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable);

}
