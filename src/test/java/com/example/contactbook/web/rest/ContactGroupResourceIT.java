package com.example.contactbook.web.rest;

import com.example.contactbook.IntegrationTest;
import com.example.contactbook.TestUtil;
import com.example.contactbook.model.ContactGroup;
import com.example.contactbook.repository.ContactGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContactGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class ContactGroupResourceIT {

    private static final Integer DEFAULT_REMOTE_ID = 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_USAGE = 1;
    private static final Integer UPDATED_USAGE = 2;

    private static final String ENTITY_API_URL = "/api/contact-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactGroupRepository contactGroupRepository;


    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactGroupMockMvc;

    private ContactGroup contactGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactGroup createEntity(EntityManager em) {
        ContactGroup contactGroup = new ContactGroup().name(DEFAULT_NAME).usage(DEFAULT_USAGE);
        return contactGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactGroup createUpdatedEntity(EntityManager em) {
        ContactGroup contactGroup = new ContactGroup().name(UPDATED_NAME).usage(UPDATED_USAGE);
        return contactGroup;
    }

    @BeforeEach
    public void initTest() {
        contactGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createContactGroup() throws Exception {
        int databaseSizeBeforeCreate = contactGroupRepository.findAll().size();
        // Create the ContactGroup
        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isCreated());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void createContactGroupWithExistingId() throws Exception {
        // Create the ContactGroup with an existing ID
        contactGroup.setId(1L);

        int databaseSizeBeforeCreate = contactGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactGroupRepository.findAll().size();
        // set the field null
        contactGroup.setName(null);

        restContactGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isBadRequest());

        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactGroups() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get all the contactGroupList
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)));
    }

    @Test
    @Transactional
    void getContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        // Get the contactGroup
        restContactGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, contactGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE));
    }

    @Test
    @Transactional
    void getNonExistingContactGroup() throws Exception {
        // Get the contactGroup
        restContactGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();

        // Update the contactGroup
        ContactGroup updatedContactGroup = contactGroupRepository.findById(contactGroup.getId()).get();
        // Disconnect from session so that the updates on updatedContactGroup are not directly saved in db
        em.detach(updatedContactGroup);
        updatedContactGroup.name(UPDATED_NAME).usage(UPDATED_USAGE);

        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isOk());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
        ContactGroup testContactGroup = contactGroupList.get(contactGroupList.size() - 1);
        assertThat(testContactGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactGroup.getUsage()).isEqualTo(UPDATED_USAGE);
    }

    @Test
    @Transactional
    void putNonExistingContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactGroup() throws Exception {
        int databaseSizeBeforeUpdate = contactGroupRepository.findAll().size();
        contactGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactGroup in the database
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeUpdate);
    }


    @Test
    @Transactional
    void deleteContactGroup() throws Exception {
        // Initialize the database
        contactGroupRepository.saveAndFlush(contactGroup);

        int databaseSizeBeforeDelete = contactGroupRepository.findAll().size();

        // Delete the contactGroup
        restContactGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactGroup> contactGroupList = contactGroupRepository.findAll();
        assertThat(contactGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
