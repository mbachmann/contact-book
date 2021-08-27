package com.example.contactbook.service;

import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.repository.CodeRepository;
import com.example.contactbook.web.rest.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public Code findCodeById(Long id) {
        return codeRepository.findById(id).orElse(null);
    }

    public Optional<Code> findById(Long id) {
        return codeRepository.findById(id);
    }

    public Optional<Code> findByIdWithUsage(Long id) {
        Optional<Code> codeOptional = codeRepository.findById(id);
        if (codeOptional.isPresent()) {
            CodeType codeType = CodeType.valueOf(codeOptional.get().getType());
            switch (codeType) {
                case AddressType: codeOptional = codeRepository.findAddressTypeById(id); break;
                case EmailType: codeOptional = codeRepository.findEmailTypeById(id);     break;
                case PhoneType: codeOptional = codeRepository.findPhoneTypeById(id);     break;
            }
        }
        return codeOptional;
    }

    public List<Code> findAllCodes() {
        return codeRepository.findAll();
    }

    public List<Code> findAllCodesByType(CodeType codeType) {

        List<Code> codes;

        switch (codeType) {
            case AddressType:   codes = codeRepository.findAllAddressType();    break;
            case EmailType:     codes = codeRepository.findAllEmailType();      break;
            case PhoneType:     codes = codeRepository.findAllPhoneType();      break;
            case AllCodes:      codes = findAllCodesWithUsage();                break;
            default:            codes = codeRepository.findAllByType(codeType.getValue());
        }

        return codes;
    }

    public List<Code> findAllCodesWithUsage() {

        List<Code> codes = new ArrayList<>();
        codes.addAll(codeRepository.findAllAddressType()) ;
        codes.addAll(codeRepository.findAllEmailType());
        codes.addAll(codeRepository.findAllPhoneType());
        return codes;
    }

    public Page<Code> findAllCodesWithUsage(Pageable pageable) {
        Comparator<Code> compareByShortCutAndName = Comparator
                .comparing(Code::getShortCut)
                .thenComparing(Code::getTitle);
        List<Code> codes = findAllCodesWithUsage();
        List<Code> sortedCodes = codes.stream()
                .sorted(compareByShortCutAndName)
                .collect(Collectors.toList());
        return PageUtil.createPageFromList(sortedCodes, pageable);
    }

    public Page<Code> findAllCodesByType(CodeType codeType, Pageable pageable) {

        Page<Code> page;

        switch (codeType) {
            case AddressType: page = codeRepository.findAllAddressType(pageable);   break;
            case EmailType:   page = codeRepository.findAllEmailType(pageable);     break;
            case PhoneType:   page = codeRepository.findAllPhoneType(pageable);     break;
            case AllCodes:    page = findAllCodesWithUsage(pageable);               break;
            default:          page = codeRepository.findAllByType(codeType.getValue(), pageable);
        }

        return page;
    }

    public Code findCodeByTypeAndTitle(CodeType codeType, String title) {
        return codeRepository.findByTypeAndTitle(codeType.getValue(), title);
    }

    public AddressType findAddressTypeByTitle(String title) {
        return (AddressType) codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), title);
    }

    public PhoneType findPhoneTypeByTitle(String title) {
        return (PhoneType) codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(), title);
    }

    public EmailType findEmailTypeByTitle(String title) {
        return (EmailType) codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(), title);
    }

     public boolean deleteById(Long id) {
        Code code = codeRepository.findById(id).orElse(null);
        if (code != null) {
            codeRepository.delete(code);
            return true;
        }
        return false;
    }

    public boolean existsById(Long id) {
        return codeRepository.existsById(id);
    }


    public Code save(Code code) {
        return codeRepository.save(code);
    }

    public Code save (CodeType codeType, Code code) {
        Code result;
        switch (codeType) {
            case AddressType:   result = codeRepository.save(new AddressType(code.getTitle(), code.getShortCut()));   break;
            case EmailType:     result = codeRepository.save(new EmailType(code.getTitle(), code.getShortCut()));     break;
            case PhoneType:     result = codeRepository.save(new PhoneType(code.getTitle(), code.getShortCut()));     break;
            default:            result = codeRepository.save(code);
        }
        return result;
    }

    public Code update (CodeType codeType, Code code) {
        Code result;
        switch (codeType) {
            case AddressType:   result = codeRepository.save(new AddressType(code.getId(), code.getTitle(), code.getShortCut(), code.getActive(), code.getUsage()));   break;
            case EmailType:     result = codeRepository.save(new EmailType(code.getId(), code.getTitle(), code.getShortCut(), code.getActive(), code.getUsage()));     break;
            case PhoneType:     result = codeRepository.save(new PhoneType(code.getId(), code.getTitle(), code.getShortCut(), code.getActive(), code.getUsage()));     break;
            default:            result = codeRepository.save(code);
        }
        return result;
    }
}
