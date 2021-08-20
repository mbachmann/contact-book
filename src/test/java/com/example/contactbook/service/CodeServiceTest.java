package com.example.contactbook.service;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CodeServiceTest extends AbstractTest {

    @Autowired
    CodeService codeService;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void getCodeList() throws Exception {
        List<Code> codes = codeService.findAllCodes();
        assertTrue(codes.size() > 0);
    }

    @Test
    public void getAddressCodeList() throws Exception {
        List<Code> codes = codeService.findAllCodesByType(CodeType.AddressType);
        assertTrue(codes != null && codes.size() > 0 && codes.get(0) instanceof AddressType);
        AddressType addressType = (AddressType) codes.get(0);
        assertEquals(addressType, codeService.findAddressTypeByTitle(addressType.getTitle()));
        assertEquals(addressType, codeService.findCodeById(addressType.getId()));
        assertEquals(addressType, codeService.findCodeByTypeAndTitle(CodeType.AddressType, addressType.getTitle()));
    }

    @Test
    public void getPhoneCodeList() throws Exception {
        List<Code> codes = codeService.findAllCodesByType(CodeType.PhoneType);
        assertTrue(codes != null && codes.size() > 0 && codes.get(0) instanceof PhoneType);
        PhoneType phoneType = (PhoneType) codes.get(0);
        assertEquals(phoneType, codeService.findPhoneTypeByTitle(phoneType.getTitle()));
        assertEquals(phoneType, codeService.findCodeById(phoneType.getId()));
        assertEquals(phoneType, codeService.findCodeByTypeAndTitle(CodeType.PhoneType, phoneType.getTitle()));

        int size = codes.size();
        PhoneType phoneTypeNew = new PhoneType("Test","T");
        PhoneType phoneTypeSaved = (PhoneType) codeService.save(phoneTypeNew);
        assertEquals(phoneTypeNew.getTitle(),phoneTypeSaved.getTitle());
        List<Code> codesAfterUpdate = codeService.findAllCodesByType(CodeType.PhoneType);
        assertEquals(size + 1, codesAfterUpdate.size());

        assertTrue(codeService.deleteCodeById(phoneTypeSaved.getId()));
        assertEquals(size, codeService.findAllCodesByType(CodeType.PhoneType).size());
    }

    @Test
    public void getEmailCodeList() throws Exception {
        List<Code> codes = codeService.findAllCodesByType(CodeType.EmailType);
        assertTrue(codes != null && codes.size() > 0 && codes.get(0) instanceof EmailType);
        EmailType emailType = (EmailType) codes.get(0);
        assertEquals(emailType, codeService.findEmailTypeByTitle(emailType.getTitle()));
        assertEquals(emailType, codeService.findCodeById(emailType.getId()));
        assertEquals(emailType, codeService.findCodeByTypeAndTitle(CodeType.EmailType, emailType.getTitle()));
    }


}
