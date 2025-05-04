package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.dto.CustomerDTO;
import com.deopraglabs.api_prysme.data.dto.SalesOrderDTO;
import com.deopraglabs.api_prysme.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
@Tag(name = "Customer", description = "Endpoints for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<CustomerDTO> findAll() {
        return customerService.findAll();
    }

    @GetMapping(value = "/getCustomerCount",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getCustomerCount() { return customerService.getCustomerCount(); }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CustomerDTO findById(@PathVariable(value = "id") UUID id) {
        return customerService.findById(id);
    }
    
    @GetMapping(value = "/findAllBySellerId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<CustomerDTO> findAllBySellerId(@PathVariable(value = "id") UUID id) { return customerService.findAllBySellerId(id); }

    @GetMapping(value = "/getNewCustomersCount",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getNewCustomersCount() { return customerService.getNewCustomersCount(); }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CustomerDTO create(@RequestBody CustomerDTO customer) {
        return customerService.save(customer);
    }

    @GetMapping(value = "/removeFromWallet/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CustomerDTO removeFromWallet(@PathVariable(value = "id") UUID id) {
        return customerService.removeFromWallet(id);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CustomerDTO update(@RequestBody CustomerDTO customer) {
        return customerService.save(customer);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return customerService.delete(id);
    }
}
