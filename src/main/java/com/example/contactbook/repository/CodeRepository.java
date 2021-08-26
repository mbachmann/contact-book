package com.example.contactbook.repository;

import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.enums.CodeType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Code findCodeByTitle(String title);
    List<Code> findAllByType(String type, Sort sort);
    List<Code> findAllByType(String type);
    Code findByTypeAndTitle(String type, String title);
}
