package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.CustomerController;
import com.deopraglabs.api_prysme.data.model.Cart;
import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import com.deopraglabs.api_prysme.data.vo.CustomerVO;
import com.deopraglabs.api_prysme.mapper.custom.CustomerMapper;
import com.deopraglabs.api_prysme.repository.CartRepository;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class CustomerService {

    private final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.cartRepository = cartRepository;
    }

    public CustomerVO save(CustomerVO customerVO) {
        logger.info("Saving customer: " + customerVO);
        final List<String> validations = validateCustomerInfo(customerVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (customerVO.getKey() > 0) {
            return customerMapper.convertToVO(customerRepository.save(customerMapper.updateFromVO(
                    customerRepository.findById(customerVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(customerVO.getKey())),
                    customerVO
            ))).add(linkTo(methodOn(CustomerController.class).findById(customerVO.getKey())).withSelfRel());
        } else {
            final var customer = customerRepository.save(customerMapper.convertFromVO(customerVO));
            customer.setCart(cartRepository.save(new Cart()));
            return customerMapper.convertToVO(customerRepository.save(customer))
                    .add(linkTo(methodOn(CustomerController.class).findById(customer.getId())).withSelfRel());
        }
    }

    public List<CustomerVO> findAll() {
        logger.info("Finding all customers");
        final var customers = customerMapper.convertToCustomerVOs(customerRepository.findAllByCustomerStatusNot(CustomerStatus.DELETED));
        customers.forEach(customer -> customer.add(linkTo(methodOn(CustomerController.class).findById(customer.getKey())).withSelfRel()));

        return customers;
    }

    public CustomerVO findById(long id) {
        logger.info("Finding customer by id: " + id);
        return customerMapper.convertToVO(customerRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(id)))
                .add(linkTo(methodOn(CustomerController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting customer: " + id);
        if (customerRepository.isDeleted(id) > 0) return ResponseEntity.notFound().build();
        return customerRepository.softDeleteById(id, DatabaseUtils.generateRandomValue(id, 11)) > 0
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateCustomerInfo(CustomerVO customerVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(customerVO, validations);
        validateUniqueFields(customerVO, validations);

        return validations;
    }

    private void validateBasicFields(CustomerVO customerVO, List<String> validations) {
        Utils.checkField(validations, Utils.isEmpty(customerVO.getCpfCnpj()), "CPF/CNPJ is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getName()), "Name is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getEmail()), "Email is required.");
        Utils.checkField(validations, customerVO.getBirthFoundationDate() == null, "Birth/Foundation Date is required.");
        Utils.checkField(validations, customerVO.getCustomerStatus() == null, "Customer Status is required.");
        Utils.checkField(validations, customerVO.getPhoneNumbers().isEmpty(), "Add at least one phone number.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getStreet()), "Street is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getNumber()), "Number is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getNeighborhood()), "Neighborhood is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getCity()), "City is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getState()), "State is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getPostalCode()), "Postal Code is required.");
        Utils.checkField(validations, Utils.isEmpty(customerVO.getAddress().getCountry()), "Country is required.");
    }

    // TODO ARRUMAR PROBLEMA DE VERIFICAÇÃO DAS CONSULTAS
    private void validateUniqueFields(CustomerVO customerVO, List<String> validations) {
        if (!Utils.isEmpty(customerVO.getCpfCnpj())) {
            var customer = customerRepository.findByCpfCnpj(customerVO.getCpfCnpj());
            if (customer != null && customer.getId() != customerVO.getKey()) {
                validations.add("CPF/CNPJ '" + customerVO.getCpfCnpj() + "' is already associated with another account.");
            }
        }

        if (!Utils.isEmpty(customerVO.getEmail())) {
            var customer = customerRepository.findByEmail(customerVO.getEmail());
            if (customer != null && customer.getId() != customerVO.getKey()) {
                validations.add("Email '" + customerVO.getEmail() + "' is already associated with another account.");
            }
        }

        if (!Utils.isEmpty(customerVO.getStateRegistration())) {
            var customer = customerRepository.findByStateRegistration(customerVO.getStateRegistration());
            if (customer != null && customer.getId() != customerVO.getKey()) {
                validations.add("State Registration '" + customerVO.getStateRegistration() + "' is already associated with another account.");
            }
        }
    }
}
