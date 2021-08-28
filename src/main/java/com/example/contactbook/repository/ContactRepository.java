package com.example.contactbook.repository;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.dto.ContactFirstLastNameDTO;
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
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long>, QuerydslPredicateExecutor<Contact> {

    @Query("SELECT " +
            "distinct c.id AS id,  " +
            "c.company as company, " +
            "c.lastName as lastName, " +
            "c.firstName as firstName,  " +
            "c.birthDate as birthDate,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, " +
            "c.photoContentType AS photoContentType, " +
            "c.addressesAggregate AS addressesAggregate, " +
            "c.phonesAggregate AS phonesAggregate, " +
            "c.emailsAggregate AS emailsAggregate,  " +
            "c.groupsIdAggregate AS groupsIdAggregate, " +
            "c.relationsIdAggregate AS relationsIdAggregate " +
            "FROM Contact c " +
            "WHERE (:filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) " +
            "OR (c.birthDate is not null AND CONCAT(COALESCE(c.birthDate,'2000-01-01'), ' ') LIKE %:filter%)  )" +
            "AND (EXISTS (SELECT 1 FROM c.groups g WHERE COALESCE(g.name,'') IN (:groups) ) " +
            "OR '*' IN (:groups) OR (SIZE(c.groups) = 0 AND '-' IN (:groups))) " +
            "AND (EXISTS (SELECT 1 FROM c.relations g WHERE COALESCE(g.contactRelationValue,'') IN (:relations) ) " +
            "OR '*' IN (:relations) OR (SIZE(c.relations) = 0 AND '-' IN (:relations)))"
    )
    List<ContactViewList> findAllContactViewsList(Sort sort, String filter, List<String> groups, List<String> relations);

    @Query("SELECT " +
            "distinct c.id AS id,  " +
            "c.company as company, " +
            "c.lastName as lastName, " +
            "c.firstName as firstName,  " +
            "c.birthDate as birthDate,  " +
            "CASE When (c.company is null) Then CONCAT(c.lastName, ' ', c.firstName) Else CONCAT(c.company , ' | ',c.lastName, ' ', c.firstName) End AS name, " +
            "c.thumbNail AS thumbNail, " +
            "c.photoContentType AS photoContentType, " +
            "c.addressesAggregate AS addressesAggregate, " +
            "c.phonesAggregate AS phonesAggregate, " +
            "c.emailsAggregate AS emailsAggregate,  " +
            "c.groupsIdAggregate AS groupsIdAggregate, " +
            "c.relationsIdAggregate AS relationsIdAggregate " +
            "FROM Contact c " +
            "WHERE (:filter is null " +
            "OR (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.addressesAggregate,'') , ' ', COALESCE(c.phonesAggregate,'') , ' ', COALESCE(c.emailsAggregate,'')) LIKE %:filter%) " +
            "OR (c.birthDate is not null AND CONCAT(COALESCE(c.birthDate,'2000-01-01'), ' ') LIKE %:filter%)  )" +
            "AND (EXISTS (SELECT 1 FROM c.groups g WHERE COALESCE(g.name,'') IN (:groups) ) " +
            "OR '*' IN (:groups) OR (SIZE(c.groups) = 0 AND '-' IN (:groups)))" +
            "AND (EXISTS (SELECT 1 FROM c.relations g WHERE COALESCE(g.contactRelationValue,'') IN (:relations) ) " +
            "OR '*' IN (:relations) OR (SIZE(c.relations) = 0 AND '-' IN (:relations)))"
    )
    Page<ContactViewList> findAllContactViewsList(Pageable page, String filter, List<String> groups, List<String> relations);

    @Query("select c from Contact c")
    List<ContactView> findAllContactViews(Sort sort);

    @Query("select c from Contact c")
    Page<ContactView> findAllContactViews(Pageable page);

    List<Contact>  findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones left join fetch c.groups left join fetch c.relations")
    List<Contact> findAllWithEagerRelationships(Sort sort);

    @Query(
            value = "select distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name  from Contact c  " +
                    "left join fetch c.addresses left join fetch c.emails left join fetch c.phones left join fetch c.groups left join fetch c.relations",
            countQuery = "select count(distinct contact) from Contact contact"
    )
    List<Contact> findAllWithEagerRelationships(Pageable page);

    /**
     * **** DO NOT USE THIS QUERY ****
     * Query with Eager Relations. The query might produce duplicates due to cartesian product. The query is inefficient. The countQuery will return the count of all contacts and not only the filtered
     *
     * @param pageable the paging and sorting information
     * @param filter   the filter string
     * @return Contact information within a Page Object (total might be wrong - higher than with filter)
     */
    @Query(
            value = "SELECT distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name from Contact c  " +
                    "left join fetch c.addresses as a  left join fetch c.emails as e left join fetch c.phones as p left join fetch c.groups as g left join fetch c.relations as r " +
                    "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,''), ' ', COALESCE(c.birthDate,'')) LIKE %:filter%) " +
                    "OR (EXISTS (SELECT 1 FROM c.addresses ad WHERE (CONCAT(COALESCE(ad.city,''), ' ', COALESCE(ad.street,''), ' ', COALESCE(ad.postalCode,''), ' ', COALESCE(ad.country,'')) LIKE %:filter%) ) )" +
                    "OR (EXISTS (SELECT 1 FROM c.emails em WHERE COALESCE(em.address,'') LIKE %:filter%) ) " +
                    "OR (EXISTS (SELECT 1 FROM c.phones ph WHERE COALESCE(ph.number,'') LIKE %:filter%) ) ",
            countQuery = "select count(distinct c) from Contact c " +
                    "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,''), ' ', COALESCE(c.birthDate,'')) LIKE %:filter%) " +
                    "OR (EXISTS (SELECT 1 FROM c.addresses ad WHERE (CONCAT(COALESCE(ad.city,''), ' ', COALESCE(ad.street,''), ' ', COALESCE(ad.postalCode,''), ' ', COALESCE(ad.country,'')) LIKE %:filter%) ) )" +
                    "OR (EXISTS (SELECT 1 FROM c.emails em WHERE COALESCE(em.address,'') LIKE %:filter%) ) " +
                    "OR (EXISTS (SELECT 1 FROM c.phones ph WHERE COALESCE(ph.number,'') LIKE %:filter%) ) "
    )
    List<Contact> findAllWithEagerRelationships(Pageable pageable, @Param("filter") String filter);

    /**
     * **** DO NOT USE THIS QUERY ****
     * Query with Eager Relations. The query might produce duplicates due to cartesian product. The query is inefficient. The countQuery will return the count of all contacts and not only the filtered
     *
     * @param pageable the paging and sorting information
     * @param filter   the filter string
     * @return Contact information within a Page Object (total might be wrong - higher than with filter)
     */
    @Query(
            value = "SELECT distinct c, CONCAT(c.lastName, ', ', c.firstName) AS name from Contact c  " +
                    "left join  c.addresses as a  left join  c.emails as e left join  c.phones as p left join  c.groups as g left join  c.relations as r " +
                    "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,''), ' ', COALESCE(c.birthDate,'')) LIKE %:filter%) " +
                    "OR (EXISTS (SELECT 1 FROM c.addresses ad WHERE (CONCAT(COALESCE(ad.city,''), ' ', COALESCE(ad.street,''), ' ', COALESCE(ad.postalCode,''), ' ', COALESCE(ad.country,'')) LIKE %:filter%) ) )" +
                    "OR (EXISTS (SELECT 1 FROM c.emails em WHERE COALESCE(em.address,'') LIKE %:filter%) ) " +
                    "OR (EXISTS (SELECT 1 FROM c.phones ph WHERE COALESCE(ph.number,'') LIKE %:filter%) ) ",
            countQuery = "select count(distinct c) from Contact c " +
                    "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,''), ' ', COALESCE(c.birthDate,'')) LIKE %:filter%) " +
                    "OR (EXISTS (SELECT 1 FROM c.addresses ad WHERE (CONCAT(COALESCE(ad.city,''), ' ', COALESCE(ad.street,''), ' ', COALESCE(ad.postalCode,''), ' ', COALESCE(ad.country,'')) LIKE %:filter%) ) )" +
                    "OR (EXISTS (SELECT 1 FROM c.emails em WHERE COALESCE(em.address,'') LIKE %:filter%) ) " +
                    "OR (EXISTS (SELECT 1 FROM c.phones ph WHERE COALESCE(ph.number,'') LIKE %:filter%) ) "
    )
    List<Contact> findAllWithFilter(Pageable pageable, @Param("filter") String filter);

    @Query("SELECT count(distinct c) from Contact c " +
            "WHERE (CONCAT(COALESCE(c.firstName,''), ' ', COALESCE(c.lastName,''), ' ', COALESCE(c.company,'') , ' ', COALESCE(c.middleName,''), ' ', COALESCE(c.birthDate,'')) LIKE %:filter%) " +
            "OR (EXISTS (SELECT 1 FROM c.addresses ad WHERE (CONCAT(COALESCE(ad.city,''), ' ', COALESCE(ad.street,''), ' ', COALESCE(ad.postalCode,''), ' ', COALESCE(ad.country,'')) LIKE %:filter%) ) )" +
            "OR (EXISTS (SELECT 1 FROM c.emails em WHERE COALESCE(em.address,'') LIKE %:filter%) ) " +
            "OR (EXISTS (SELECT 1 FROM c.phones ph WHERE COALESCE(ph.number,'') LIKE %:filter%) ) "
    )
    int countAllWithFilter(@Param("filter") String filter);

    @Query("select c from Contact c left join fetch c.addresses left join fetch c.emails left join fetch c.phones  left join fetch c.groups left join fetch c.relations where c.id =:id")
    Optional<Contact> findByIdWithEagerRelationships(@Param("id") Long id);

    @EntityGraph(value = "contact-entity-graph")
    @NonNull
    Page<Contact> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable);

    @Query("SELECT distinct new com.example.contactbook.model.dto.ContactFirstLastNameDTO(c.firstName, c.lastName) FROM Contact c ")
    List<ContactFirstLastNameDTO> findAllContactFirstLastNameView();

}
