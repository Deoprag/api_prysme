package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.ContactController;
import com.deopraglabs.api_prysme.data.dto.ContactDTO;
import com.deopraglabs.api_prysme.repository.ContactRepository;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class ContactService {

    private final Logger logger = Logger.getLogger(ContactService.class.getName());

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final HalConfiguration applicationJsonHalConfiguration;

    @Autowired
    public ContactService(ContactRepository contactRepository, CustomerRepository customerRepository, HalConfiguration applicationJsonHalConfiguration) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
        this.applicationJsonHalConfiguration = applicationJsonHalConfiguration;
    }

    public ContactDTO save(ContactDTO contactDTO) {
        logger.info("Saving contact: " + contactDTO);
        final List<String> validations = validateContactInfo(contactDTO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (contactDTO.getId() > 0) {
            return contactMapper.convertToDTO(contactRepository.save(contactMapper.updateFromDTO(
                    contactRepository.findById(contactDTO.getId())
                            .orElseThrow(() -> new CustomRuntimeException.ContactNotFoundException(contactDTO.getId())),
                    contactDTO
            ))).add(linkTo(methodOn(ContactController.class).findById(contactDTO.getId())).withSelfRel());
        } else {
            final var contact = contactRepository.save(contactMapper.convertFromDTO(contactDTO));
            customerRepository.updateCustomerStatus(contact.getCustomer().getId(), contact.getCustomerStatus());
            return contactMapper.convertToDTO(contactRepository.save(contact))
                    .add(linkTo(methodOn(ContactController.class).findById(contact.getId())).withSelfRel());
        }
    }

    public List<ContactDTO> findAll() {
        logger.info("Finding all contacts");
        final var contacts = contactMapper.convertToContactDTOs(contactRepository.findAll());
        contacts.forEach(contact -> contact.add(linkTo(methodOn(ContactController.class).findById(contact.getId())).withSelfRel()));

        return contacts;
    }

    public List<ContactDTO> findAllByCustomerId(UUID customerId) {
        try {
            logger.info("Finding all contacts by customer id");
            final var customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(customerId));

            final var contactList = contactRepository.findAllByCustomer(customer);

            final var contacts = contactMapper.convertToContactDTOs(contactList.isEmpty() ? new ArrayList<>() : contactList);
            contacts.forEach(contact -> contact.add(linkTo(methodOn(ContactController.class).findById(contact.getId())).withSelfRel()));

            return contacts;
        } catch (Exception e) {
            logger.info("Error finding contacts by customer id" + e);
            return new ArrayList<>();
        }
    }

    public ContactDTO findById(long id) {
        logger.info("Finding contact by id: " + id);
        return contactMapper.convertToDTO(contactRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.ContactNotFoundException(id)))
                .add(linkTo(methodOn(ContactController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting contact: " + id);
        return contactRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateContactInfo(ContactDTO contactDTO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(contactDTO, validations);
        validateUniqueFields(contactDTO, validations);

        return validations;
    }

    private DTOid validateBasicFields(ContactDTO contactDTO, List<String> validations) {
    }

    private DTOid validateUniqueFields(ContactDTO contactDTO, List<String> validations) {
    }
}
