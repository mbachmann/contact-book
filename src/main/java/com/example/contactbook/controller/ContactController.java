package com.example.contactbook.controller;

import com.example.contactbook.controller.advice.ResourceNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.model.ContactSmall;
import com.example.contactbook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;


    @GetMapping("/contacts")
    protected List<ContactSmall> findAll() {
        return contactService.findAllContacts();
    }

    @PostMapping("/contacts/new")
    protected Contact save(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @PutMapping("/contacts/edit/{id}")
    protected Contact edit(@PathVariable Long id, @RequestBody Contact contact) {
        Contact existingContact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("edit not successful: contact with id=" + id + " not found");

        return contactService.edit(contact);
    }


    @GetMapping("/contacts/{id}")
    protected Contact findById(@PathVariable Long id) {
        Contact contact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("findById not successful: contact with id=" + id + " not found");
        return contact;
    }

    @DeleteMapping("/contacts/{id}/delete")
    protected ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Contact contact = contactService.findContactById(id);
        if (contact == null) throw new ResourceNotFoundException("delete not successful: contact with id=" + id + " not found");
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

}
