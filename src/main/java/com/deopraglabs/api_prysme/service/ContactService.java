package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.ContactController;
import com.deopraglabs.api_prysme.data.vo.ContactVO;
import com.deopraglabs.api_prysme.mapper.custom.ContactMapper;
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
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class ContactService {

    private final Logger logger = Logger.getLogger(ContactService.class.getName());

    private final ContactMapper contactMapper;
    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final HalConfiguration applicationJsonHalConfiguration;

    @Autowired
    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper, CustomerRepository customerRepository, HalConfiguration applicationJsonHalConfiguration) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.customerRepository = customerRepository;
        this.applicationJsonHalConfiguration = applicationJsonHalConfiguration;
    }

    public ContactVO save(ContactVO contactVO) {
        logger.info("Saving contact: " + contactVO);
        final List<String> validations = validateContactInfo(contactVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (contactVO.getKey() > 0) {
            return contactMapper.convertToVO(contactRepository.save(contactMapper.updateFromVO(
                    contactRepository.findById(contactVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.ContactNotFoundException(contactVO.getKey())),
                    contactVO
            ))).add(linkTo(methodOn(ContactController.class).findById(contactVO.getKey())).withSelfRel());
        } else {
            final var contact = contactRepository.save(contactMapper.convertFromVO(contactVO));
            customerRepository.updateCustomerStatus(contact.getCustomer().getId(), contact.getCustomerStatus());
            return contactMapper.convertToVO(contactRepository.save(contact))
                    .add(linkTo(methodOn(ContactController.class).findById(contact.getId())).withSelfRel());
        }
    }

    public List<ContactVO> findAll() {
        logger.info("Finding all contacts");
        final var contacts = contactMapper.convertToContactVOs(contactRepository.findAll());
        contacts.forEach(contact -> contact.add(linkTo(methodOn(ContactController.class).findById(contact.getKey())).withSelfRel()));

        return contacts;
    }

    public List<ContactVO> findAllByCustomerId(long customerId) {
        try {
            logger.info("Finding all contacts by customer id");
            final var customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(customerId));

            final var contactList = contactRepository.findAllByCustomer(customer);

            final var contacts = contactMapper.convertToContactVOs(contactList.isEmpty() ? new ArrayList<>() : contactList);
            contacts.forEach(contact -> contact.add(linkTo(methodOn(ContactController.class).findById(contact.getKey())).withSelfRel()));

            return contacts;
        } catch (Exception e) {
            logger.info("Error finding contacts by customer id" + e);
            return new ArrayList<>();
        }
    }

    public ContactVO findById(long id) {
        logger.info("Finding contact by id: " + id);
        return contactMapper.convertToVO(contactRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.ContactNotFoundException(id)))
                .add(linkTo(methodOn(ContactController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting contact: " + id);
        return contactRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateContactInfo(ContactVO contactVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(contactVO, validations);
        validateUniqueFields(contactVO, validations);

        return validations;
    }

    private void validateBasicFields(ContactVO contactVO, List<String> validations) {
    }

    private void validateUniqueFields(ContactVO contactVO, List<String> validations) {
    }
}
