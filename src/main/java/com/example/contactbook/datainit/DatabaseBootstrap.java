package com.example.contactbook.datainit;

import com.example.contactbook.model.*;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.model.enums.ContactRelationType;
import com.example.contactbook.repository.CodeRepository;
import com.example.contactbook.repository.ContactGroupRepository;
import com.example.contactbook.repository.ContactRelationRepository;
import com.example.contactbook.service.ContactService;
import com.example.contactbook.utils.HasLogger;
import com.example.contactbook.utils.ImageProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class DatabaseBootstrap implements InitializingBean, HasLogger {

    @Autowired
    ContactService contactService;

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    ContactGroupRepository contactGroupRepository;

    @Autowired
    ContactRelationRepository contactRelationRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        initCodes();
        createFirstContact();
        createSecondContact();
        createThirdContact();
        createForthContact();
        createFifthContact();
        getLogger().info("Bootstrap finished");
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

            ContactRelation contactRelation = new ContactRelation();
            contactRelation.setContactRelationType(ContactRelationType.CREDITOR);
            contactRelationRepository.save(contactRelation);
            contactRelation = new ContactRelation();
            contactRelation.setContactRelationType(ContactRelationType.CUSTOMER);
            contactRelationRepository.save(contactRelation);

            ContactGroup contactGroup = new ContactGroup();
            contactGroup.setName("A-Contacts");
            contactGroupRepository.save(contactGroup);
            contactGroup = new ContactGroup();
            contactGroup.setName("B-Contacts");
            contactGroupRepository.save(contactGroup);

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
            contact.getAddresses().add(address);

            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CUSTOMER);
            relation.addContacts(contact);

            ContactGroup group = contactGroupRepository.findContactGroupByName("A-Contacts");
            group.addContacts(contact);

            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createSecondContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Felix", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Felix");
            contact.setLastName("Muster");
            contact.setMiddleName("Franz");
            contact.setBirthDate(LocalDate.of(1990, 1,8));
            contact.setCompany("Great Company Ltd");
            contact.setNotes("Second Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
            relation.addContacts(contact);

            ContactGroup group = contactGroupRepository.findContactGroupByName("A-Contacts");
            group.addContacts(contact);
            group = contactGroupRepository.findContactGroupByName("B-Contacts");
            group.addContacts(contact);


            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
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
            contact.getAddresses().add(address);

            address = new Address();
            address.setCity("Aarau");
            address.setCountry("Schweiz");
            address.setPostalCode("5000");
            address.setStreet("Bahnhofstrasse");
            // AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","Home");
            addressType = (AddressType)codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(),"Business");
            address.setAddressType(addressType);
            address.setDefaultAddress(false);
            contact.getAddresses().add(address);

            Phone phone = new Phone();
            phone.setNumber("+41 61 812 34 56");
            // PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle("PhoneType","Home");
            PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(),"Home");
            phone.setPhoneType(phoneType);
            contact.getPhones().add(phone);

            phone = new Phone();
            phone.setNumber("+41 62 546 12 56");
            // PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle("PhoneType","Home");
            phoneType = (PhoneType)codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(),"Business");
            phone.setPhoneType(phoneType);
            contact.getPhones().add(phone);

            Email email = new Email();
            email.setAddress("max.mustermann@example.com");
            // EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle("EmailType","Home");
            EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(),"Home");
            email.setEmailType(emailType);
            contact.getEmails().add(email);

            email = new Email();
            email.setAddress("max.mustermann@gmail.com");
            // EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle("EmailType","Home");
            emailType = (EmailType)codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(),"Business");
            email.setEmailType(emailType);
            contact.getEmails().add(email);

            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
            relation.addContacts(contact);

            ContactGroup group = contactGroupRepository.findContactGroupByName("B-Contacts");
            group.addContacts(contact);

            contactService.save(contact);

            List<Code> codes = codeRepository.findAllByType(CodeType.AddressType.getValue());
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createForthContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("John", "Doe") == null) {
            Contact contact = new Contact();
            contact.setFirstName("John");
            contact.setLastName("Doe");
            contact.setMiddleName("Jack");
            contact.setBirthDate(LocalDate.of(1991, 2,9));
            contact.setCompany("John Doe Company Ltd");
            contact.setNotes("Forth Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
            relation.addContacts(contact);

            ContactGroup group = contactGroupRepository.findContactGroupByName("A-Contacts");
            group.addContacts(contact);
            group = contactGroupRepository.findContactGroupByName("B-Contacts");
            group.addContacts(contact);


            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createFifthContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Laurent", "Mathis") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Laurent");
            contact.setLastName("Mathis");
            contact.setMiddleName("Jean");
            contact.setBirthDate(LocalDate.of(1989, 4, 10));
            contact.setNotes("Fifth Contact");


            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
            relation.addContacts(contact);

            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }
    private byte[] readImageFromResource(String imageResourceFile) throws IOException {
        ClassPathResource backImgFile = new ClassPathResource(imageResourceFile);
        byte[] image = new byte[(int) backImgFile.contentLength()];
        backImgFile.getInputStream().read(image);
        return image;
    }
}
