package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.CustomerController;
import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import com.deopraglabs.api_prysme.data.vo.CustomerVO;
import com.deopraglabs.api_prysme.mapper.custom.CustomerMapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    private final Logger logger = Logger.getLogger(CustomerService.class.getName());

    public CustomerVO save(CustomerVO customerVO) {
        logger.info("Saving customer: " + customerVO);
        if (customerVO.getKey() > 0) {
            return customerMapper.convertToVO(customerRepository.save(customerMapper.updateFromVO(
                    customerRepository.findById(customerVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(customerVO.getKey())),
                    customerVO
            ))).add(linkTo(methodOn(CustomerController.class).findById(customerVO.getKey())).withSelfRel());
        } else {
            var customer = customerRepository.save(customerMapper.convertFromVO(customerVO));
            return customerMapper.convertToVO(customer)
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
        return customerRepository.isDeleted(id) > 0
                ? ResponseEntity.notFound().build()
                : customerRepository.softDeleteById(id, DatabaseUtils.generateRandomValue(id, 11)) > 0
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
