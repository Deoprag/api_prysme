package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.dto.ContactDTO;
import com.deopraglabs.api_prysme.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact")
@Tag(name = "Contact", description = "Endpoints for managing contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ContactDTO> findAll() {
        return contactService.findAll();
    }

    @GetMapping(value = "/findAllByCustomerId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ContactDTO> findAllByCustomerId(@PathVariable(value = "id") UUID id) {
        return contactService.findAllByCustomerId(id);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ContactDTO findById(@PathVariable(value = "id") long id) {
        return contactService.findById(id);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ContactDTO create(@RequestBody ContactDTO contact) {
        return contactService.save(contact);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ContactDTO update(@RequestBody ContactDTO contact) {
        return contactService.save(contact);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return contactService.delete(id);
    }
}
