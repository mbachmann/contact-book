package com.example.contactbook.web.rest;

import com.example.contactbook.model.ContactGroup;
import com.example.contactbook.repository.ContactGroupRepository;
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
 * REST controller for managing {@link com.example.contactbook.model.ContactGroup}.
 */
@Tag(name = "Configurations")
@RestController
@RequestMapping("/api")
public class ContactGroupResource implements HasLogger {

    private static final String ENTITY_NAME = "contactGroup";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ContactGroupRepository contactGroupRepository;

    public ContactGroupResource(ContactGroupRepository contactGroupRepository) {
        this.contactGroupRepository = contactGroupRepository;
    }

    /**
     * {@code POST  /contact-groups} : Create a new contactGroup.
     *
     * @param contactGroup the contactGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactGroup, or with status {@code 400 (Bad Request)} if the contactGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-groups")
    public ResponseEntity<ContactGroup> createContactGroup(@Valid @RequestBody ContactGroup contactGroup)
        throws URISyntaxException {
        getLogger().debug("REST request to save ContactGroup : {}", contactGroup);
        if (contactGroup.getId() != null) {
            throw new BadRequestAlertException("A new contactGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactGroup result = contactGroupRepository.save(contactGroup);
        return ResponseEntity
            .created(new URI("/api/contact-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-groups/:id} : Updates an existing contactGroup.
     *
     * @param id the id of the contactGroup to save.
     * @param contactGroup the contactGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactGroup,
     * or with status {@code 400 (Bad Request)} if the contactGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-groups/{id}")
    public ResponseEntity<ContactGroup> updateContactGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactGroup contactGroup
    ) throws URISyntaxException {
        getLogger().debug("REST request to update ContactGroup : {}, {}", id, contactGroup);
        if (contactGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactGroup result = contactGroupRepository.save(contactGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contact-groups} : get all the contactGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactGroups in body.
     */
    @PageableAsQueryParam
    @GetMapping("/contact-groups")
    public ResponseEntity<List<ContactGroup>> getAllContactGroups(@PageableDefault(size = 20) @Parameter(hidden = true) Pageable pageable) {
        getLogger().debug("REST request to get a page of ContactGroups");
        Page<ContactGroup> page = contactGroupRepository.findAllContactGroupsWithUsages(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-groups/:id} : get the "id" contactGroup.
     *
     * @param id the id of the contactGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-groups/{id}")
    public ResponseEntity<ContactGroup> getContactGroup(@PathVariable Long id) {
        getLogger().debug("REST request to get ContactGroup : {}", id);
        Optional<ContactGroup> contactGroup = contactGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contactGroup);
    }

    /**
     * {@code DELETE  /contact-groups/:id} : delete the "id" contactGroup.
     *
     * @param id the id of the contactGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-groups/{id}")
    public ResponseEntity<Void> deleteContactGroup(@PathVariable Long id) {
        getLogger().debug("REST request to delete ContactGroup : {}", id);
        contactGroupRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
