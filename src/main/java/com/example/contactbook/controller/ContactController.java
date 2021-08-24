package com.example.contactbook.controller;

import com.example.contactbook.controller.advice.ResourceNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.projection.ContactView;
import com.example.contactbook.model.projection.ContactViewList;
import com.example.contactbook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;


    @GetMapping("/list")
    protected List<ContactViewList> findAllContactSmall() {
        return contactService.findAllContactViewsList(null, Arrays.asList("A-Contacts", "B-Contacts"), null);
    }

    @GetMapping("/")
    protected List<ContactView> findAll() {
        return contactService.findAllContactViews();
    }


    @Operation(summary = "Get all contact with all relationships by its id", tags = "contacts-eager")
    @GetMapping("/eager")
    protected List<Contact> findAllWithEagerRelationShips() {
        return contactService.findAllContactsWithEagerRelationships();
    }

    @Operation(summary = "Get one contact with all relationships by its id", tags = "contacts-eager")
    @GetMapping("/eager/{id}")
    protected Contact findByIdWithEagerRelationShips(@PathVariable Long id) {
        Contact contact =  contactService.findContactByIdWithEagerRelationships(id);
        if (contact == null) throw new ResourceNotFoundException("findById not successful: contact with id=" + id + " not found");
        return contact;
    }

    @PostMapping("/new")
    protected Contact save(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @PutMapping("/edit/{id}")
    protected Contact edit(@PathVariable Long id, @RequestBody Contact contact) {
        Contact existingContact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("edit not successful: contact with id=" + id + " not found");

        return contactService.edit(contact);
    }

    @Operation(summary = "Get a contact by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the contact",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contact.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "contact not found",
                    content = @Content) })
    @GetMapping("/{id}")
    protected Contact findById(@Parameter(description = "id of contact to be searched") @PathVariable Long id) {
        Contact contact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("findById not successful: contact with id=" + id + " not found");
        return contact;
    }

    @DeleteMapping("/{id}/delete")
    protected ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Contact contact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("delete not successful: contact with id=" + id + " not found");
        return ResponseEntity.ok(contactService.deleteContactById(id));
    }

}
