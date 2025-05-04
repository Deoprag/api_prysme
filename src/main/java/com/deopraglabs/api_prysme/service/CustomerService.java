package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.CustomerController;
import com.deopraglabs.api_prysme.data.dto.CustomerDTO;
import com.deopraglabs.api_prysme.data.enums.CustomerStatus;
import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class CustomerService {

    private final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        logger.info("Saving customer: " + customerDTO);
        final List<String> validations = validateCustomerInfo(customerDTO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (customerDTO.getId() != null) {
            Customer customer = customerRepository.findById(customerDTO.getId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(customerDTO.getId()));
            var seller = customer.getSeller();
            customer = customerMapper.updateFromDTO(customer, customerDTO);
            if (customer.getSeller() != seller) customer.setCustomerStatus(CustomerStatus.NEW);
            customerRepository.save(customer);

            return customerMapper.convertToDTO(customer).add(linkTo(methodOn(CustomerController.class).findById(customerDTO.getId())).withSelfRel());
        } else {
            final var customer = customerRepository.save(customerMapper.convertFromDTO(customerDTO));
            return customerMapper.convertToDTO(customer)
                    .add(linkTo(methodOn(CustomerController.class).findById(customer.getId())).withSelfRel());
        }
    }

    public List<CustomerDTO> findAll() {
        logger.info("Finding all customers");
        final var customers = customerMapper.convertToCustomerDTOs(customerRepository.findAllByCustomerStatusNot(CustomerStatus.DELETED));
        customers.forEach(customer -> customer.add(linkTo(methodOn(CustomerController.class).findById(customer.getId())).withSelfRel()));

        return customers;
    }

    public List<CustomerDTO> findAllBySellerId(UUID id) {
        logger.info("Finding all customers by seller: " + id);
        final var customers = customerMapper.convertToCustomerDTOs(customerRepository.findAllBySellerId(id));
        customers.forEach(customer -> customer.add(linkTo(methodOn(CustomerController.class).findById(customer.getId())).withSelfRel()));

        return customers;
    }

    public CustomerDTO findById(UUID id) {
        logger.info("Finding customer by id: " + id);
        return customerMapper.convertToDTO(customerRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(id)))
                .add(linkTo(methodOn(CustomerController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting customer: " + id);
        if (customerRepository.isDeleted(id) > 0) return ResponseEntity.notFound().build();
        return customerRepository.softDeleteById(id, DatabaseUtils.generateRandomValue(id, 14)) > 0
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getCustomerCount() {
        logger.info("Counting customers...");
        final HashMap<String, Long> customerCount = new HashMap<>();
        customerCount.put("value", customerRepository.countCustomerByCustomerStatusNot(CustomerStatus.DELETED));
        return ResponseEntity.ok(customerCount);
    }

    public CustomerDTO removeFromWallet(UUID id) {
        logger.info("Removing customer from wallet: " + id);
        final var customer = customerRepository.findById(id).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(id));
        customer.setSeller(customer.getSeller().getTeam().getManager());
        customer.setCustomerStatus(CustomerStatus.FINALIZED);

        return customerMapper.convertToDTO(customerRepository.save(customer))
                .add(linkTo(methodOn(CustomerController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> getNewCustomersCount() {
        logger.info("Counting NEW customers...");
        final HashMap<String, Long> newCustomersCount = new HashMap<>();
        newCustomersCount.put("value", customerRepository.countCustomerByCustomerStatus(CustomerStatus.NEW));
        return ResponseEntity.ok(newCustomersCount);
    }

    private List<String> validateCustomerInfo(CustomerDTO customerDTO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(customerDTO, validations);
        validateUniqueFields(customerDTO, validations);

        return validations;
    }

    private void validateBasicFields(CustomerDTO customerDTO, List<String> validations) {
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getDocument()), "CPF/CNPJ is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getName()), "Name is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getEmail()), "Email is required.");
        Utils.checkField(validations, customerDTO.getBirthDate() == null, "Birth/Foundation Date is required.");
        Utils.checkField(validations, customerDTO.getCustomerStatus() == null, "Customer Status is required.");
        Utils.checkField(validations, customerDTO.getPhoneNumbers().isEmpty(), "Add at least one phone number.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getStreet()), "Street is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getNumber()), "Number is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getNeighborhood()), "Neighborhood is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getCity()), "City is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getState()), "State is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getPostalCode()), "Postal Code is required.");
        Utils.checkField(validations, Utils.isEmpty(customerDTO.getAddress().getCountry()), "Country is required.");
    }

    private void validateUniqueFields(CustomerDTO customerDTO, List<String> validations) {
        if (!Utils.isEmpty(customerDTO.getDocument())
                && customerRepository.findByCpfCnpjAndIdNot(customerDTO.getDocument(), customerDTO.getId()) != null) {
            // NÃO TA CAINDO DENTRO DO IF
            validations.add("CPF/CNPJ '" + customerDTO.getDocument() + "' is already associated with another account.");
        }

        if (!Utils.isEmpty(customerDTO.getEmail())
                && customerRepository.findByEmailAndIdNot(customerDTO.getEmail(), customerDTO.getId()) != null) {
            validations.add("Email '" + customerDTO.getEmail() + "' is already associated with another account.");
        }

        if (!Utils.isEmpty(customerDTO.getStateRegistration())
                && customerRepository.findByStateRegistrationAndIdNot(customerDTO.getStateRegistration(), customerDTO.getId()) != null) {
            validations.add("State Registration '" + customerDTO.getStateRegistration() + "' is already associated with another account.");
        }
    }
}
