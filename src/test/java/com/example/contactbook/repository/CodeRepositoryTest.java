package com.example.contactbook.repository;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.enums.CodeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

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
    public void getCodeListByTypeSorted() throws Exception {
        List<Code> codes = codeRepository.findAllByType(CodeType.AddressType.getValue(), Sort.by(Sort.Direction.ASC,"shortCut"));
        assertTrue(codes.size() > 0);
    }

    @Test
    public void getCodeListByTypePaged() throws Exception {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "shortCut"));

        Page<Code> codes = codeRepository.findAllByType(CodeType.AddressType.getValue(), pageable);
        assertTrue(codes.getContent().size() > 0);
    }

    @Test
    public void getCodeById() throws Exception {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "shortCut"));
        long id = 1L;
        Optional<Code> code = codeRepository.findById(id);
        assertTrue(code.isPresent());

    }

    @Test
    public void getEmailTypeWithUsageById() throws Exception {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "shortCut"));
        // Page<Code> codesPaged = codeRepository.findAllByType(CodeType.EmailType.getValue(), pageable);
        Page<Code> codesPaged = codeRepository.findAllEmailTypes(pageable);

        if (codesPaged.getTotalElements() > 0) {
            long emailTypeId = codesPaged.getContent().get(0).getId();
            Optional<Code>  emailCodeOptional = codeRepository.findEmailTypeById(emailTypeId);
            emailCodeOptional.ifPresent(emailCode -> {
                getLogger().debug(emailCode.toString());
            });
        }
    }

    @Test
    public void getAddressTypeWithUsageById() throws Exception {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "shortCut"));
        // Page<Code> codesPaged = codeRepository.findAllByType(CodeType.AddressType.getValue(), pageable);
        Page<Code> codesPaged = codeRepository.findAllAddressTypes(pageable);

        if (codesPaged.getTotalElements() > 0) {
            long addressTypeId = codesPaged.getContent().get(0).getId();
            Optional<Code>  addressCodeOptional = codeRepository.findAddressTypeById(addressTypeId);
            addressCodeOptional.ifPresent(addressCode -> {
                getLogger().debug(addressCode.toString());
            });
        }
    }

    @Test
    public void getPhoneTypeWithUsageById() throws Exception {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "shortCut"));
        // Page<Code> codesPaged = codeRepository.findAllByType(CodeType.PhoneType.getValue(), pageable);
        Page<Code> codesPaged = codeRepository.findAllPhoneTypes(pageable);

        if (codesPaged.getTotalElements() > 0) {
            long phoneTypeId = codesPaged.getContent().get(0).getId();
            Optional<Code>  phoneCodeOptional = codeRepository.findPhoneTypeById(phoneTypeId);
            phoneCodeOptional.ifPresent(phoneCode -> {
                getLogger().debug(phoneCode.toString());
            });
        }
    }


}
