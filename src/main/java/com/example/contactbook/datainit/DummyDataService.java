package com.example.contactbook.datainit;

import com.example.contactbook.model.*;
import com.example.contactbook.model.codes.AddressType;
import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.codes.EmailType;
import com.example.contactbook.model.codes.PhoneType;
import com.example.contactbook.model.enums.ContactRelationType;
import com.example.contactbook.model.dto.ContactFirstLastNameDTO;
import com.example.contactbook.repository.CodeRepository;
import com.example.contactbook.repository.ContactGroupRepository;
import com.example.contactbook.repository.ContactRelationRepository;
import com.example.contactbook.service.ContactService;
import com.example.contactbook.utils.HasLogger;
import com.example.contactbook.utils.ImageProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

@Service
public class DummyDataService implements HasLogger {

    private static final List<Contact> CONTACTS = new ArrayList<>();

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageProcessingService imageProcessingService;

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private ContactGroupRepository contactGroupRepository;

    @Autowired
    private ContactRelationRepository contactRelationRepository;

    private List<Code> phoneTypes = null;
    private List<Code> emailTypes = null;
    private List<Code> addressTypes = null;
    private List<ContactRelation> contactRelations = null;
    private List<ContactGroup> contactGroups = null;
    private final Map<String, byte[]> imageMap = new HashMap<>();
    private final Map<String, byte[]> thumbNailMap = new HashMap<>();
    private List<ContactFirstLastNameDTO> allContactsFirstAndLastName;

    public DummyDataService() {
    }

    // @Transactional
    public Contact createContact() throws IOException, URISyntaxException {
        int firstNameRandom = random.nextInt(DummyData.FIRST_NAMES.length);

        String firstName = DummyData.FIRST_NAMES[firstNameRandom];
		String lastName = getLastName();

		if (allContactsFirstAndLastName.contains(new ContactFirstLastNameDTO(firstName, lastName))) return null;

        Contact contact = new Contact(
				firstName,
				lastName,
                random.nextBoolean() ? getMiddleName(firstNameRandom) : null,
                random.nextBoolean() ? LocalDate.of(getRandomInt(1940, 2010), 1, 8) : null,
                random.nextBoolean() ? getCompany() : null,
                "Random Information " + getRandomInt(1, 100000)
        );

        if (random.nextInt(10) > 5) addPhoto(contact, firstNameRandom);
        if (random.nextInt(10) > 2) createAndAddAddress(contact);
        if (random.nextInt(10) > 8) createAndAddAddress(contact);
        if (random.nextInt(10) > 2) createAndAddPhoneNumber(contact);
        if (random.nextInt(10) > 8) createAndAddPhoneNumber(contact);
        if (random.nextInt(10) > 2) createAndAddEmail(contact);
        if (random.nextInt(10) > 8) createAndAddEmail(contact);
        if (random.nextInt(10) > 4) addContactGroup(contact);
        if (random.nextInt(10) > 8) addContactGroup(contact);
        if (random.nextInt(10) > 4) addContactRelation(contact);
        if (random.nextInt(10) > 8) addContactRelation(contact);

        contact.createAggregates();
        return contact;
    }

    private void addPhoto(Contact contact, int firstNameRandom) throws IOException, URISyntaxException {
        String imageKey = getImageSource(firstNameRandom);
        contact.setPhoto(getBytesFromResource(imageKey));
        contact.setPhotoContentType("image/png");
        contact.setThumbNail(createThumbnail(imageKey, contact.getPhoto(), 32, contact.getPhotoContentType()));
    }

    private void createAndAddAddress(Contact contact) {
        String[] cityAndPostalCode = getCityAndPostalCode();
        Address address = new Address(getStreet(), cityAndPostalCode[0], cityAndPostalCode[1], "CH", random.nextBoolean(), (AddressType) getAddressType());
        contact.addAddress(address);
    }

    private void createAndAddPhoneNumber(Contact contact) {
        Phone phone = new Phone(getPhoneNumber(), (PhoneType) getPhoneType());
        contact.addPhone(phone);
    }

    private void createAndAddEmail(Contact contact) {
        Email email = new Email(contact.getFirstName() + "." + contact.getLastName() + "@" + getEmailUrl(), (EmailType) getEmailType());
        contact.addEmail(email);
    }

    private void addContactGroup(Contact contact) {
        ContactGroup group = contactGroupRepository.findContactGroupByName(getContactGroup().getName());
        group.addContacts(contact);
    }

    private void addContactRelation(Contact contact) {
        ContactRelation relation = contactRelationRepository.findContactRelationByContactRelationType(getContactRelation().getContactRelationType());
        relation.addContacts(contact);
    }


    public void initCodes() {
        if (codeRepository.count() == 0) {
            PhoneType phoneType = new PhoneType("Home", "H");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("Business", "B");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("School", "S");
            codeRepository.save(phoneType);
            phoneType = new PhoneType("Mobile", "M");
            codeRepository.save(phoneType);

            EmailType emailType = new EmailType("Home", "H");
            codeRepository.save(emailType);
            emailType = new EmailType("Business", "B");
            codeRepository.save(emailType);
            emailType = new EmailType("School", "S");
            codeRepository.save(emailType);

            AddressType addressType = new AddressType("Home", "H");
            codeRepository.save(addressType);
            addressType = new AddressType("Business", "B");
            codeRepository.save(addressType);
            addressType = new AddressType("School", "S");
            codeRepository.save(addressType);
        }
        if (contactRelationRepository.count() == 0) {
            ContactRelationType[] relations = ContactRelationType.values();
            for (ContactRelationType relation : relations) {
                ContactRelation contactRelation = new ContactRelation();
                contactRelation.setContactRelationType(relation);
                contactRelationRepository.save(contactRelation);
            }
        }
        if (contactGroupRepository.count() == 0) {
            ContactGroup contactGroup = new ContactGroup();
            contactGroup.setName("A-Contacts");
            contactGroupRepository.save(contactGroup);
            contactGroup = new ContactGroup();
            contactGroup.setName("B-Contacts");
            contactGroupRepository.save(contactGroup);
        }

        phoneTypes = codeRepository.findAllPhoneTypes();
        emailTypes = codeRepository.findAllEmailTypes();
        addressTypes = codeRepository.findAllAddressTypes();
        contactRelations = contactRelationRepository.findAll();
        contactGroups = contactGroupRepository.findAll();
        allContactsFirstAndLastName = contactService.findAllContactFirstLastNameView();
    }


    private final Random random = new Random(1);

    /* === MISC === */

    private String getIBAN() {
        return DummyData.IBANS[random.nextInt(DummyData.IBANS.length)];
    }

    private String[] getCompanies() {
        return DummyData.COMPANIES;
    }

    private String getCompany() {
        return DummyData.COMPANIES[random.nextInt(DummyData.COMPANIES.length)];
    }

    private String getBank() {
        return DummyData.BANKS[random.nextInt(DummyData.BANKS.length)];
    }

    private LocalDate getDate() {
        return LocalDate.now().minusDays(random.nextInt(60));
    }

    private LocalDate getPastDate(int bound) {
        return LocalDate.now().minusDays(random.nextInt(bound));
    }

    private LocalDate getFutureDate(int bound) {
        return LocalDate.now().plusDays(random.nextInt(bound));
    }

    private String getFirstName() {
        return DummyData.FIRST_NAMES[random.nextInt(DummyData.FIRST_NAMES.length)];
    }

    private String getMiddleName(int firstNameRandom) {
        int middleNameRandom = random.nextInt(DummyData.FIRST_NAMES.length);
        // both random must be male or female (odd or even)
        if (firstNameRandom % 2 != middleNameRandom % 2) {
            middleNameRandom = middleNameRandom + 1 >= DummyData.FIRST_NAMES.length ? middleNameRandom - 1 : middleNameRandom + 1;
        }
        return DummyData.FIRST_NAMES[middleNameRandom];
    }

    private String getStreet() {
        return DummyData.STREETS[random.nextInt(DummyData.STREETS.length)] + " " + getRandomInt(1, 50);
    }

    private String[] getCityAndPostalCode() {
        int cityRandom = (random.nextInt(DummyData.CITIES.length) / 2) * 2;
        return new String[]{DummyData.CITIES[cityRandom], DummyData.CITIES[cityRandom + 1]};
    }

    private String getLastName() {
        return DummyData.LAST_NAMES[random.nextInt(DummyData.LAST_NAMES.length)];
    }

    private String getImageSource(int firstNameRandom) {
        String imageResource = null;
        if (firstNameRandom % 2 != 0) {
            imageResource = "image/" + "female-avatar" + String.format("%02d", getRandomInt(1, 5)) + ".png";
        } else {
            imageResource = "image/" + "male-avatar" + String.format("%02d", getRandomInt(1, 5)) + ".png";
        }
        return imageResource;
    }

    /* === NUMBERS === */

    private Double getAmount() {
        return random.nextBoolean() ? getNegativeAmount() : getPositiveAmount();
    }

    private Double getPositiveAmount() {
        return random.nextDouble() * 20000;
    }

    private Double getNegativeAmount() {
        return random.nextDouble() * -20000;
    }

    private int getRandomInt(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    private Double getRandomDouble(int min, int max) {
        return min + (max - min) * random.nextDouble();
    }

    private String getPhoneNumber() {
        return String.format("79 %03d %02d %02d", random.nextInt(1000), random.nextInt(100), random.nextInt(100));
    }

    private Code getPhoneType() {
        return phoneTypes.get(random.nextInt(phoneTypes.size()));
    }

    private Code getEmailType() {
        return emailTypes.get(random.nextInt(emailTypes.size()));
    }

    private Code getAddressType() {
        return addressTypes.get(random.nextInt(addressTypes.size()));
    }

    private ContactRelation getContactRelation() {
        return contactRelations.get(random.nextInt(contactRelations.size()));
    }

    private ContactGroup getContactGroup() {
        return contactGroups.get(random.nextInt(contactGroups.size()));
    }

    private String getEmailUrl() {
        return DummyData.EMAIL_URLS[random.nextInt(DummyData.EMAIL_URLS.length)] + " " + getRandomInt(1, 50);
    }

    private byte[] getBytesFromResource(String imageKey) throws IOException, URISyntaxException {
        if (!imageMap.containsKey(imageKey)) {
            imageMap.put(imageKey, imageProcessingService.getBytesFromResource(getClass(), imageKey));
        }
        return imageMap.get(imageKey);
    }

    private byte[] createThumbnail(String imageKey, byte[] photo, int width, String contentType) throws IOException {
        if (!thumbNailMap.containsKey(imageKey)) {
            thumbNailMap.put(imageKey, imageProcessingService.createThumbnail(photo, width, contentType).toByteArray());
        }
        return thumbNailMap.get(imageKey);
    }


}
