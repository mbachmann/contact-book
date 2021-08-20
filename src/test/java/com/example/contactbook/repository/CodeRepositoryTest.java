package com.example.contactbook.repository;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.enums.CodeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeRepositoryTest extends AbstractTest {

    @Autowired
    CodeRepository codeRepository;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void getCodeList() throws Exception {
        List<Code> codes = codeRepository.findAll();
        assertTrue(codes.size() > 0);
    }

    @Test
    public void getCodeListByType() throws Exception {
        List<Code> codes = codeRepository.findAllByType(CodeType.AddressType.getValue(), Sort.by(Sort.Direction.ASC,"shortCut"));
        assertTrue(codes.size() > 0);
    }

}
