package com.example.contactbook.service;

import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public Code findCodeById(Long id) {
        return codeRepository.findById(id).orElse(null);
    }

    public List<Code> findAllCodes() {
        return codeRepository.findAll();
    }

    public List<Code> findAllCodesByType(CodeType codeType) {
        return codeRepository.findAllByType(codeType.getValue());
    }


    public Code findCodeByTypeAndTitle(CodeType codeType, String title) {
        return codeRepository.findByTypeAndTitle(codeType.getValue(),title);
    }

    public AddressType findAddressTypeByTitle(String title) {
        return (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), title);
    }

    public PhoneType findPhoneTypeByTitle(String title) {
        return (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(), title);
    }

    public EmailType findEmailTypeByTitle(String title) {
        return (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(), title);
    }

    public Code save(Code code) {
        return codeRepository.save(code);
    }

    public boolean deleteCodeById(Long id) {
        Code code = codeRepository.findById(id).orElse(null);
        if (code != null) {
            codeRepository.delete(code);
            return true;
        }
        return false;
    }

}
