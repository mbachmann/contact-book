package com.example.contactbook.repository;

import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.enums.CodeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Code findCodeByTitle(String title);
    List<Code> findAllByType(String type, Sort sort);

    Page<Code> findAllByType(String type, Pageable pageable);
    List<Code> findAllByType(String type);

    Code findByTypeAndTitle(String type, String title);

    @Query("Select distinct new com.example.contactbook.model.codes.EmailType(et.id, et.title, et.shortCut, et.active, count(em)) FROM EmailType  et left join et.emails em where et.id = :id GROUP BY et.id, et.title, et.shortCut ")
    Optional<Code> findEmailTypeById(Long id);

    @Query("Select distinct new com.example.contactbook.model.codes.EmailType(et.id, et.title, et.shortCut, et.active, count(em)) FROM EmailType  et left join et.emails em GROUP BY et.id, et.title, et.shortCut ")
    Page<Code> findAllEmailType(Pageable pageable);

    @Query("Select distinct new com.example.contactbook.model.codes.EmailType(et.id, et.title, et.shortCut, et.active, count(em)) FROM EmailType  et left join et.emails em GROUP BY et.id, et.title, et.shortCut ")
    List<Code> findAllEmailType();


    @Query("Select distinct new com.example.contactbook.model.codes.PhoneType(pt.id, pt.title, pt.shortCut, pt.active, count(ph)) FROM PhoneType  pt left join pt.phones ph where pt.id = :id GROUP BY pt.id, pt.title, pt.shortCut ")
    Optional<Code> findPhoneTypeById(Long id);

    @Query("Select distinct new com.example.contactbook.model.codes.PhoneType(pt.id, pt.title, pt.shortCut, pt.active, count(ph)) FROM PhoneType  pt left join pt.phones ph GROUP BY pt.id, pt.title, pt.shortCut ")
    Page<Code> findAllPhoneType(Pageable pageable);

    @Query("Select distinct new com.example.contactbook.model.codes.PhoneType(pt.id, pt.title, pt.shortCut, pt.active, count(ph)) FROM PhoneType  pt left join pt.phones ph GROUP BY pt.id, pt.title, pt.shortCut ")
    List<Code> findAllPhoneType();

    @Query("Select distinct new com.example.contactbook.model.codes.AddressType(at.id, at.title, at.shortCut, at.active, count(ad)) FROM AddressType  at left join at.addresses ad where at.id = :id GROUP BY at.id, at.title, at.shortCut ")
    Optional<Code> findAddressTypeById(Long id);

    @Query("Select distinct new com.example.contactbook.model.codes.AddressType(at.id, at.title, at.shortCut, at.active, count(ad)) FROM AddressType  at left join at.addresses ad GROUP BY at.id, at.title, at.shortCut ")
    Page<Code> findAllAddressType(Pageable pageable);

    @Query("Select distinct new com.example.contactbook.model.codes.AddressType(at.id, at.title, at.shortCut, at.active, count(ad)) FROM AddressType  at left join at.addresses ad GROUP BY at.id, at.title, at.shortCut ")
    List<Code> findAllAddressType();

}
