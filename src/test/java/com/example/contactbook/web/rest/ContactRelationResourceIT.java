package com.example.contactbook.web.rest;


import com.example.contactbook.IntegrationTest;
import com.example.contactbook.TestUtil;
import com.example.contactbook.model.ContactRelation;
import com.example.contactbook.model.enums.ContactRelationType;
import com.example.contactbook.repository.ContactRelationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.hasItem;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContactRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class ContactRelationResourceIT {

    private static final ContactRelationType DEFAULT_CONTACT_RELATION_TYPE = ContactRelationType.CUSTOMER;
    private static final ContactRelationType UPDATED_CONTACT_RELATION_TYPE = ContactRelationType.CREDITOR;

    private static final String ENTITY_API_URL = "/api/contact-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRelationRepository contactRelationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactRelationMockMvc;

    private ContactRelation contactRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRelation createEntity(EntityManager em) {
        ContactRelation contactRelation = new ContactRelation().contactRelationType(DEFAULT_CONTACT_RELATION_TYPE);
        return contactRelation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRelation createUpdatedEntity(EntityManager em) {
        ContactRelation contactRelation = new ContactRelation().contactRelationType(UPDATED_CONTACT_RELATION_TYPE);
        return contactRelation;
    }

    @BeforeEach
    public void initTest() {
        contactRelation = createEntity(em);
    }

    @Test
    @Transactional
    void createContactRelation() throws Exception {
        int databaseSizeBeforeCreate = contactRelationRepository.findAll().size();
        // Create the ContactRelation
        restContactRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isCreated());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeCreate + 1);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(DEFAULT_CONTACT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void createContactRelationWithExistingId() throws Exception {
        // Create the ContactRelation with an existing ID
        contactRelation.setId(1L);

        int databaseSizeBeforeCreate = contactRelationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactRelationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactRelations() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        // Get all the contactRelationList
        restContactRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactRelationType").value(hasItem(DEFAULT_CONTACT_RELATION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        // Get the contactRelation
        restContactRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, contactRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactRelation.getId().intValue()))
            .andExpect(jsonPath("$.contactRelationType").value(DEFAULT_CONTACT_RELATION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContactRelation() throws Exception {
        // Get the contactRelation
        restContactRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();

        // Update the contactRelation
        ContactRelation updatedContactRelation = contactRelationRepository.findById(contactRelation.getId()).get();
        // Disconnect from session so that the updates on updatedContactRelation are not directly saved in db
        em.detach(updatedContactRelation);
        updatedContactRelation.contactRelationType(UPDATED_CONTACT_RELATION_TYPE);

        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isOk());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
        ContactRelation testContactRelation = contactRelationList.get(contactRelationList.size() - 1);
        assertThat(testContactRelation.getContactRelationType()).isEqualTo(UPDATED_CONTACT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactRelation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactRelation() throws Exception {
        int databaseSizeBeforeUpdate = contactRelationRepository.findAll().size();
        contactRelation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactRelationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactRelation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactRelation in the database
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeUpdate);
    }


    @Test
    @Transactional
    void deleteContactRelation() throws Exception {
        // Initialize the database
        contactRelationRepository.saveAndFlush(contactRelation);

        int databaseSizeBeforeDelete = contactRelationRepository.findAll().size();

        // Delete the contactRelation
        restContactRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactRelation> contactRelationList = contactRelationRepository.findAll();
        assertThat(contactRelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
