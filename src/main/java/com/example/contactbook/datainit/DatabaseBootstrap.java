package com.example.contactbook.datainit;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.repository.CodeRepository;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.Phone;
import com.example.contactbook.service.ContactService;
import com.example.contactbook.utils.ImageProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class DatabaseBootstrap implements InitializingBean {

    @Autowired
    ContactService contactService;

    @Autowired
    CodeRepository codeRepository;

    private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrap.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        initCodes();
        createFirstContact();
        createSecondContact();
        createThirdContact();
        log.info("Bootstrap finished");
    }

    private void initCodes() {
        if (codeRepository.findCodeByTitle("Home") == null) {
            PhoneType phoneType = new PhoneType("Home","H");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("Business","B");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("School","S");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("Mobile","M");
            codeRepository.save(phoneType);

            EmailType emailType = new EmailType("Home","H");
            codeRepository.save(emailType);
            emailType = new EmailType("Business","B");
            codeRepository.save(emailType);
            emailType = new EmailType("School","S");
            codeRepository.save(emailType);

            AddressType addressType = new AddressType("Home","H");
            codeRepository.save(addressType);
            addressType = new AddressType("Business","B");
            codeRepository.save(addressType);
            addressType = new AddressType("School","S");
            codeRepository.save(addressType);

        }
    }

    private void createFirstContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Anna", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Anna");
            contact.setLastName("Muster");
            //contact.setMiddleName("Marta");
            contact.setBirthDate(LocalDate.of(2000, 12,12));
            //contact.setCompany("");
            contact.setNotes("First Contact");

            // Todo Uncomment
            contact.setPhoto(readImageFromResource("image/firstContact.png"));
            contact.setPhotoContentType("image/png");
            contact.setThumbNail(ImageProcessing.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());

            Address address = new Address();
            address.setCity("Basel");
            address.setCountry("Schweiz");
            address.setPostalCode("4000");
            address.setStreet("Peter-Merian 42");
            //AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","School");
            AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(),"School");
            address.setAddressType(addressType);
            address.setDefaultAddress(true);
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            contactService.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createSecondContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Felix", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Felix");
            contact.setLastName("Muster");
            contact.setMiddleName("Franz");
            contact.setBirthDate(LocalDate.of(1990, 1,8));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("Second Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            // contact.setPhotoContentType("image/png");

            contactService.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createThirdContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Max", "Mustermann") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Max");
            contact.setLastName("Mustermann");
            contact.setMiddleName("Fritz");
            contact.setBirthDate(LocalDate.of(1980, 2,8));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("Thrid Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));

            Address address = new Address();
            address.setCity("Zürich");
            address.setCountry("Schweiz");
            address.setPostalCode("8000");
            address.setStreet("Aeschengraben");
            // AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","Home");
            AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(),"Home");
            address.setAddressType(addressType);
            address.setDefaultAddress(true);
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            address = new Address();
            address.setCity("Aarau");
            address.setCountry("Schweiz");
            address.setPostalCode("5000");
            address.setStreet("Bahnhofstrasse");
            // AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","Home");
            addressType = (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(),"Business");
            address.setAddressType(addressType);
            address.setDefaultAddress(false);
            addresses.add(address);

            Phone phone = new Phone();
            phone.setNumber("+41 61 812 34 56");
            // PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle("PhoneType","Home");
            PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(),"Home");
            phone.setPhoneType(phoneType);
            Set<Phone> phones = new HashSet<>();
            phones.add(phone);
            contact.setPhones(phones);

            phone = new Phone();
            phone.setNumber("+41 62 546 12 56");
            // PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle("PhoneType","Home");
            phoneType = (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(),"Business");
            phone.setPhoneType(phoneType);
            phones.add(phone);

            Email email = new Email();
            email.setAddress("max.mustermann@example.com");
            // EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle("EmailType","Home");
            EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(),"Home");
            email.setEmailType(emailType);
            Set<Email> emails = new HashSet<>();
            emails.add(email);
            contact.setEmails(emails);

            email = new Email();
            email.setAddress("max.mustermann@gmail.com");
            // EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle("EmailType","Home");
            emailType = (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(),"Business");
            email.setEmailType(emailType);
            emails.add(email);

            contactService.save(contact);

            List<Code> codes = codeRepository.findAllByType(CodeType.AddressType.getValue());
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private byte[] readImageFromResource(String imageResourceFile) throws IOException {
        ClassPathResource backImgFile = new ClassPathResource(imageResourceFile);
        byte[] image = new byte[(int) backImgFile.contentLength()];
        backImgFile.getInputStream().read(image);
        return image;
    }

}
