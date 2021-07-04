package com.example.contactbook.datainit;

import com.example.contactbook.model.Contact;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class DatabaseBootstrap implements InitializingBean {
    @Autowired
    ContactRepository repository;
    private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrap.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        createFirstContact();
        createSecondContact();
        createThirdContact();
        log.info("Bootstrap finished");
    }

    private void createFirstContact() throws IOException {
        if (repository.findByFirstNameAndLastName("Anna", "Muster") == null) {
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
            address.setType("School");
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            repository.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createSecondContact() throws IOException {
        if (repository.findByFirstNameAndLastName("Felix", "Muster") == null) {
            Contact contact = new Contact();
            contact.setFirstName("Felix");
            contact.setLastName("Muster");
            contact.setMiddleName("Franz");
            contact.setBirthDate(new GregorianCalendar(1990, Calendar.JANUARY,8));
            contact.setCompany("Example Company Ltd");
            contact.setNotes("Second Contact");
            // Todo Uncomment
            // contact.setPhoto(readImageFromResource("image/secondContact.png"));
            repository.save(contact);
            log.info(contact.getFirstName() + " " + contact.getLastName() + " created");
        }
    }

    private void createThirdContact() throws IOException {
        if (repository.findByFirstNameAndLastName("Max", "Mustermann") == null) {
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
            address.setType("Privat");
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            contact.setAddresses(addresses);

            Phone phone = new Phone();
            phone.setNumber("+41 61 812 34 56");
            phone.setType("Privat");
            Set<Phone> phones = new HashSet<>();
            phones.add(phone);
            contact.setPhones(phones);

            Email email = new Email();
            email.setAddress("max.mustermann@example.com");
            email.setType("Privat");
            Set<Email> emails = new HashSet<>();
            emails.add(email);
            contact.setEmails(emails);

            repository.save(contact);
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
