package com.example.contactbook.datainit;

import com.example.contactbook.model.Contact;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.repository.codes.CodeRepository;
import com.example.contactbook.repository.ContactRepository;
import com.example.contactbook.model.Address;
import com.example.contactbook.model.Email;
import com.example.contactbook.model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;

public class DatabaseBootstrap implements InitializingBean {

    @Autowired
    ContactRepository contactRepository;

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
        if (contactRepository.findByFirstNameAndLastName("Anna", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Anna");
            contact.setLastName("Muster");
            contact.setMiddleName("Marta");
            contact.setBirthDate(new GregorianCalendar(2000, Calendar.DECEMBER,12));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("First Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/firstContact.png"));
            Address address = new Address();
            address.setCity("Basel");
            address.setCountry("Schweiz");
            address.setPostalCode("4000");
            address.setStreet("Peter Merian");
            AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","School");
            address.setAddressType(addressType);
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            contactRepository.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createSecondContact() throws IOException {
        if (contactRepository.findByFirstNameAndLastName("Felix", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Felix");
            contact.setLastName("Muster");
            contact.setMiddleName("Franz");
            contact.setBirthDate(new GregorianCalendar(1990, Calendar.JANUARY,8));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("Second Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            contactRepository.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createThirdContact() throws IOException {
        if (contactRepository.findByFirstNameAndLastName("Max", "Mustermann") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Max");
            contact.setLastName("Mustermann");
            contact.setMiddleName("Fritz");
            contact.setBirthDate(new GregorianCalendar(1980, Calendar.FEBRUARY,8));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("Thrid Contact");

            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));

            Address address = new Address();
            address.setCity("Basel");
            address.setCountry("Schweiz");
            address.setPostalCode("4000");
            address.setStreet("Aeschengraben");
            AddressType addressType = (AddressType)codeRepository.findByTypeAndTitle("AddressType","Home");
            address.setAddressType(addressType);
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            Phone phone = new Phone();
            phone.setNumber("+41 61 812 34 56");
            PhoneType phoneType = (PhoneType)codeRepository.findByTypeAndTitle("PhoneType","Home");
            phone.setPhoneType(phoneType);
            Set<Phone> phones = new HashSet<>();
            phones.add(phone);
            contact.setPhones(phones);

            Email email = new Email();
            email.setAddress("max.mustermann@example.com");
            EmailType emailType = (EmailType)codeRepository.findByTypeAndTitle("EmailType","Home");
            email.setEmailType(emailType);
            Set<Email> emails = new HashSet<>();
            emails.add(email);
            contact.setEmails(emails);

            contactRepository.save(contact);

            List<Code> codes = codeRepository.findByType("AddressTypeCode");
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
