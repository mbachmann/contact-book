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
import com.example.contactbook.utils.ImageProcessingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseBootstrap implements InitializingBean, HasLogger {

    @Autowired
    ContactService contactService;

    @Autowired
    ImageProcessingService imageProcessingService;

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    ContactGroupRepository contactGroupRepository;

    @Autowired
    ContactRelationRepository contactRelationRepository;

    @Autowired
    DummyDataService dummyDataService;


    @Override
    public void afterPropertiesSet() throws Exception {
        dummyDataService.initCodes();
        createFirstContact();
        createSecondContact();
        createThirdContact();
        createForthContact();
        createFifthContact();
        createContacts(0);
        getLogger().info("Bootstrap finished");
    }


    @Transactional
    public String createContacts(int amountToCreate) throws IOException, URISyntaxException {

        int remainingAmount = amountToCreate;
        int alreadyExistsCount = 0;
        int runningTotal = 0;
        final int CHUNK = 100;
        while (remainingAmount > 0) {
            long millisStart = System.currentTimeMillis();
            List<Contact> contacts = new ArrayList<>();
            int chunk = Math.min(remainingAmount, CHUNK);
            for (int i = 0; i < chunk; i++) {
                Contact contact = dummyDataService.createContact();
                if (contact == null) alreadyExistsCount += 1;
                else  contacts.add(contact);
            }
            long millisCreate = System.currentTimeMillis();
            contactService.saveAll(contacts);

            runningTotal += chunk;
            writeCreateContactLogging(millisStart, millisCreate, runningTotal, amountToCreate, alreadyExistsCount);
            remainingAmount -= chunk;
        }

        return amountToCreate + " Contacts created";
    }

    private void createFirstContact() throws IOException, URISyntaxException {
        if (contactService.findByFirstNameAndLastName("Anna", "Muster").size() == 0) {
            Contact contact = new Contact();
            contact.setFirstName("Anna");
            contact.setLastName("Muster");
            contact.setBirthDate(LocalDate.of(2000, 12, 12));
            contact.setNotes("First Contact");

            // Todo Uncomment
            contact.setPhoto(imageProcessingService.getBytesFromResource(getClass(), "image/female-avatar01.png"));
            contact.setPhotoContentType("image/png");
            contact.setThumbNail(imageProcessingService.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());

            Address address = new Address();
            address.setCity("Basel");
            address.setCountry("Schweiz");
            address.setPostalCode("4000");
            address.setStreet("Peter-Merian 42");
            AddressType addressType = (AddressType) codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), "School");
            address.setAddressType(addressType);
            address.setDefaultAddress(true);
            contact.addAddress(address);

            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CUSTOMER);
            relation.addContacts(contact);

            ContactGroup group = contactGroupRepository.findContactGroupByName("A-Contacts");
            group.addContacts(contact);

            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createSecondContact() throws IOException {
        if (contactService.findByFirstNameAndLastName("Felix", "Muster").size() == 0) {
            Contact contact = new Contact("Felix", "Muster", "Franz", LocalDate.of(1990, 1, 8), "Great Company Ltd", "Second Contact");
            // Todo Uncomment
            // contact.setPhoto(imageProcessingService.getBytesFromResource(getClass(), "image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            // contact.setThumbNail(imageProcessingService.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());

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
        if (contactService.findByFirstNameAndLastName("Max", "Mustermann").size() == 0) {
            Contact contact = new Contact("Max", "Mustermann", "Fritz", LocalDate.of(1980, 2, 8), "Example Company Ltd", "Thrid Contact");

            // Todo Uncomment
            // contact.setPhoto(imageProcessingService.getBytesFromResource(getClass(), "image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            // contact.setThumbNail(imageProcessingService.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());

            AddressType addressType = (AddressType) codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), "Home");
            Address address = new Address("Aeschengraben", "8000", "Z??rich", "CH", true, addressType);
            contact.addAddress(address);

            addressType = (AddressType) codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), "Business");
            address = new Address("Bahnhofstrasse", "5000", "Aarau", "CH", true, addressType);
            contact.addAddress(address);

            addressType = (AddressType) codeRepository.findByTypeAndTitle(CodeType.AddressType.getValue(), "School");
            address = new Address("Lagerstrasse", "8004", "Z??rich", "CH", true, addressType);
            contact.addAddress(address);


            PhoneType phoneType = (PhoneType) codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(), "Home");
            Phone phone = new Phone("+41 61 812 34 56", phoneType);
            contact.addPhone(phone);

            phone = new Phone();
            phone.setNumber("+41 62 546 12 56");
            phoneType = (PhoneType) codeRepository.findByTypeAndTitle(CodeType.PhoneType.getValue(), "Business");
            phone.setPhoneType(phoneType);
            contact.getPhones().add(phone);


            EmailType emailType = (EmailType) codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(), "Home");
            Email email = new Email("max.mustermann@example.com", emailType);
            contact.addEmail(email);

            email = new Email();
            email.setAddress("max.mustermann@gmail.com");
            emailType = (EmailType) codeRepository.findByTypeAndTitle(CodeType.EmailType.getValue(), "Business");
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
        if (contactService.findByFirstNameAndLastName("John", "Doe").size() == 0) {
            Contact contact = new Contact("John", "Doe", "Jack", LocalDate.of(1991, 2, 9), "John Doe Company Ltd", "Forth Contact");

            // Todo Uncomment
            // contact.setPhoto(imageProcessingService.getBytesFromResource(getClass(), "image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            // contact.setThumbNail(imageProcessingService.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());
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
        if (contactService.findByFirstNameAndLastName("Laurent", "Mathis").size() == 0) {
            Contact contact = new Contact("Laurent", "Mathis", "Jean", LocalDate.of(1989, 4, 10), null, "Fifth Contact");

            // Todo Uncomment
            // contact.setPhoto(imageProcessingService.getBytesFromResource(getClass(), "image/secondContact.png"));
            // contact.setPhotoContentType("image/png");
            // contact.setThumbNail(imageProcessingService.createThumbnail(contact.getPhoto(), 32, contact.getPhotoContentType()).toByteArray());
            ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(ContactRelationType.CREDITOR);
            relation.addContacts(contact);

            contactService.save(contact);
            getLogger().info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void writeCreateContactLogging(long millisStart, long millisCreate, int runningTotal, int amountToCreate, int alreadyExistsCount) {
        long millisSaved = System.currentTimeMillis();
        long millisSaving = millisSaved - millisCreate;
        long millisCreating = millisCreate - millisStart;
        long timeUsed = System.currentTimeMillis() - millisStart;
        getLogger().info(runningTotal + " of " + amountToCreate + " contacts in " + timeUsed + " ms ( create: " + millisCreating + " ms, saved: " + millisSaving + " ms)" + " created; (" + alreadyExistsCount+") already exists");
    }
}
