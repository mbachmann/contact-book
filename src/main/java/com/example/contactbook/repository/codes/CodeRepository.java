package com.example.contactbook.repository.codes;

import com.example.contactbook.model.codes.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Code findCodeByTitle(String title);
    List<Code> findByType(String type);
    Code findByTypeAndTitle(String type, String title);

}
