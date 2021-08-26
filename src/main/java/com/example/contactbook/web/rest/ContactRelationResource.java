package com.example.contactbook.web.rest;

import com.example.contactbook.model.ContactRelation;
import com.example.contactbook.repository.ContactRelationRepository;
import com.example.contactbook.utils.HasLogger;
import com.example.contactbook.web.rest.exception.BadRequestAlertException;
import com.example.contactbook.web.rest.utils.HeaderUtil;
import com.example.contactbook.web.rest.utils.PaginationUtil;
import com.example.contactbook.web.rest.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link ContactRelation}.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Configurations")
public class ContactRelationResource implements HasLogger {

    private static final String ENTITY_NAME = "contactRelation";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ContactRelationRepository contactRelationRepository;

    public ContactRelationResource(ContactRelationRepository contactRelationRepository) {
        this.contactRelationRepository = contactRelationRepository;
    }

    /**
     * {@code POST  /contact-relations} : Create a new contactRelation.
     *
     * @param contactRelation the contactRelation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactRelation, or with status {@code 400 (Bad Request)} if the contactRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-relations")
    public ResponseEntity<ContactRelation> createContactRelation(@Valid @RequestBody ContactRelation contactRelation)
        throws URISyntaxException {
        getLogger().debug("REST request to save ContactRelation : {}", contactRelation);
        if (contactRelation.getId() != null) {
            throw new BadRequestAlertException("A new contactRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactRelation result = contactRelationRepository.save(contactRelation);
        return ResponseEntity
            .created(new URI("/api/contact-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-relations/:id} : Updates an existing contactRelation.
     *
     * @param id the id of the contactRelation to save.
     * @param contactRelation the contactRelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactRelation,
     * or with status {@code 400 (Bad Request)} if the contactRelation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactRelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-relations/{id}")
    public ResponseEntity<ContactRelation> updateContactRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactRelation contactRelation
    ) throws URISyntaxException {
        getLogger().debug("REST request to update ContactRelation : {}, {}", id, contactRelation);
        if (contactRelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactRelation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactRelation result = contactRelationRepository.save(contactRelation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactRelation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contact-relations} : get all the contactRelations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactRelations in body.
     */
    @PageableAsQueryParam
    @GetMapping("/contact-relations")
    public ResponseEntity<List<ContactRelation>> getAllContactRelations(@PageableDefault(size = 20) @Parameter(hidden = true) Pageable pageable) {
        getLogger().debug("REST request to get a page of ContactRelations");
        Page<ContactRelation> page = contactRelationRepository.findAllContactRelationsWithUsages(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-relations/:id} : get the "id" contactRelation.
     *
     * @param id the id of the contactRelation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactRelation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-relations/{id}")
    public ResponseEntity<ContactRelation> getContactRelation(@PathVariable Long id) {
        getLogger().debug("REST request to get ContactRelation : {}", id);
        Optional<ContactRelation> contactRelation = contactRelationRepository.findContactRelationById(id);
        return ResponseUtil.wrapOrNotFound(contactRelation);
    }

    /**
     * {@code DELETE  /contact-relations/:id} : delete the "id" contactRelation.
     *
     * @param id the id of the contactRelation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-relations/{id}")
    public ResponseEntity<Void> deleteContactRelation(@PathVariable Long id) {
        getLogger().debug("REST request to delete ContactRelation : {}", id);
        contactRelationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
