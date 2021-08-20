package com.example.contactbook.controller;

import com.example.contactbook.AbstractTest;
import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContactControllerTest extends AbstractTest {

    @Autowired
    ContactRepository customerRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getCustomersList() throws Exception {
        String uri = "/contacts/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();

        Contact[] contactList = super.mapFromJson(response, Contact[].class);
        assertTrue(contactList.length > 0);
        assertEquals(contactList[0].getFirstName(), "Anna");
        assertEquals(contactList[1].getFirstName(), "Felix");
    }

    @Test
    public void getOneCustomer() throws Exception {
        String uri = "/contacts/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();
        Contact contact = super.mapFromJson(response, Contact.class);
        assertEquals(contact.getFirstName(), "Anna");
    }

    @Test
    public void postOneCustomer() throws Exception {
        String uri = "/contacts/new";

        Contact contact= new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");

        String json = super.mapToJson(contact);

        MvcResult postMvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andReturn();

        int status = postMvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = postMvcResult.getResponse().getContentAsString();
        Contact postCustomer = super.mapFromJson(response, Contact.class);
        assertEquals(postCustomer.getFirstName(), contact.getFirstName());
    }
}
