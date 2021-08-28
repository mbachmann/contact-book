package com.example.contactbook.web.rest;

import com.example.contactbook.datainit.DatabaseBootstrap;
import com.example.contactbook.datainit.DummyDataService;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactViewList;
import com.example.contactbook.service.ContactService;
import com.example.contactbook.utils.HasLogger;
import com.example.contactbook.web.rest.exception.BadRequestAlertException;
import com.example.contactbook.web.rest.utils.HeaderUtil;
import com.example.contactbook.web.rest.utils.PaginationUtil;
import com.example.contactbook.web.rest.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.dialect.Database;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Tag(name = "Contacts")
@RestController
@RequestMapping("/api")
public class ContactResource implements HasLogger {

    @Autowired
    private ContactService contactService;

    @Autowired
    private DatabaseBootstrap databaseBootstrap;


    private static final String ENTITY_NAME = "contact";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${contactapp.data-password}")
    private String dataPassword;

    @PageableAsQueryParam
    @GetMapping("/contacts/list")
    protected ResponseEntity<List<ContactViewList>> findAllContactViewList(
            @PageableDefault(size = 20) @Parameter(hidden = true) Pageable pageable,
            @Parameter(description = "full text filter") @RequestParam(required = false) String filter,
            @Parameter(description = "comma separated list of groups, * for all, - not assigned") @RequestParam(required = false, defaultValue = "*") String groups,
            @Parameter(description = "comma separated list of relations, * for, - not assigned") @RequestParam(required = false, defaultValue = "*") String relations
    ) {
        List<String> groupsList = groups == null ? Arrays.asList("*") : Arrays.asList(groups.split(","));
        List<String> relationsList = relations == null ? Arrays.asList("*") : Arrays.asList(relations.split(","));
        Page<ContactViewList> page = contactService.findAllContactViewsList(pageable, filter, groupsList, relationsList);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact} : get all the contacts (without eager information).
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contacts in body.
     */
    @PageableAsQueryParam
    @GetMapping("/contacts")
    protected ResponseEntity<List<Contact>> findAll(
            @PageableDefault(size = 20, sort = {"company", "lastName", "firstName"}, direction = Sort.Direction.ASC) @Parameter(hidden = true) Pageable pageable,
            @Parameter(description = "full text filter") @RequestParam(required = false) String filter,
            @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        getLogger().debug("REST request to get a page of Contact");
        Page<Contact> page;
        if (eagerload) {
            page = contactService.findAllContactsWithEagerRelationships(pageable, filter);
        } else {
            page = contactService.findAll(pageable, filter);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code POST  /contact} : Create a new contact.
     *
     * @param contact the contact to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contact, or with status {@code 400 (Bad Request)} if the contact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contacts")
    protected ResponseEntity<Contact> save(@RequestBody @Valid Contact contact) throws URISyntaxException {
        getLogger().debug("REST request to save Contact : {}", contact);
        if (contact.getId() != null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contact result = contactService.save(contact);
        return ResponseEntity
                .created(new URI("/api/contact/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /contact/:id} : Updates an existing contact.
     *
     * @param id      the id of the contact to save.
     * @param contact the contact to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contact,
     * or with status {@code 400 (Bad Request)} if the contact is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contact couldn't be updated.
     */
    @PutMapping("/contacts/{id}")
    protected ResponseEntity<Contact> edit(@PathVariable Long id, @RequestBody @Valid Contact contact) {
        getLogger().debug("REST request to update Contact : {}, {}", id, contact);
        if (contact.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contact.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (contactService.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Contact result = contactService.edit(contact);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contact.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /contact/:id} : get the "id" contact.
     *
     * @param id the id of the contact to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contact, or with status {@code 404 (Not Found)}.
     */
    @Operation(summary = "Get a contact by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the contact",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contact.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "contact not found",
                    content = @Content)})
    @GetMapping("/contacts/{id}")
    protected ResponseEntity<Contact> findById(@Parameter(description = "id of contact to be searched") @PathVariable Long id) {
        getLogger().debug("REST request to get Contact : {}", id);

        Optional<Contact> contact = contactService.findContactByIdWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(contact);
    }

    /**
     * {@code DELETE  /contact/:id} : delete the "id" contact.
     *
     * @param id the id of the contact to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contacts/{id}")
    protected ResponseEntity<Boolean> delete(@PathVariable Long id) {
        if (contactService.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ResponseEntity.ok(contactService.deleteContactById(id));
    }

    /**
     * {@code DELETE  /contact} : delete all contacts
     *
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @Operation(summary = "Delete all contacts")
    @DeleteMapping("/contacts")
    protected ResponseEntity<String> deleteAll(@Parameter(description = "delete all contacts password") @RequestParam(required = true, defaultValue = "password") String password) {
        if (!password.equals(dataPassword)) {
            throw new BadRequestAlertException("password not correct", ENTITY_NAME, "wrongpassword");
        }
        return ResponseEntity.ok(contactService.deleteAll());
    }

    /**
     * {@code DELETE  /contact} : batch create random contacts
     *
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @Operation(summary = "Batch create random contacts")
    @PatchMapping("/contacts/create")
    protected ResponseEntity<String> createContacts(
            @Parameter(description = "amount of contacts to create (1 ... 10000)") @Min(1) @Max(10000) @RequestParam(required = true, defaultValue = "1") Integer amount,
            @Parameter(description = "create contacts password") @RequestParam(required = true, defaultValue = "password") String password
    ) throws IOException, URISyntaxException {
        if (!password.equals(dataPassword)) {
            throw new BadRequestAlertException("password not correct", ENTITY_NAME, "wrongpassword");
        }
        return ResponseEntity.ok(databaseBootstrap.createContacts(amount));
    }

}
