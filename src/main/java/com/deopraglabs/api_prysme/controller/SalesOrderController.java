package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.service.SalesOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salesOrder")
@Tag(name = "Sales Order", description = "Endpoints for managing sales orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @Autowired
    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<SalesOrderVO> findAll() {
        return salesOrderService.findAll();
    }

    @GetMapping(value = "/findAllByCustomerId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<SalesOrderVO> findAllByCustomerId(@PathVariable(value = "id") long id) {
        return salesOrderService.findAllByCustomerId(id);
    }

    @GetMapping(value = "/findAllByTeamId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<SalesOrderVO> findAllByTeamId(@PathVariable(value = "id") long id) {
        return salesOrderService.findAllByTeamId(id);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SalesOrderVO findById(@PathVariable(value = "id") long id) {
        return salesOrderService.findById(id);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SalesOrderVO create(@RequestBody SalesOrderVO salesOrder) {
        return salesOrderService.save(salesOrder);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SalesOrderVO update(@RequestBody SalesOrderVO salesOrder) {
        return salesOrderService.save(salesOrder);
    }

    @GetMapping(
            value = "/approveById/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SalesOrderVO approveById(@PathVariable(value = "id") long id) {
        return salesOrderService.approveById(id);
    }

    @GetMapping(
            value = "/disapproveById/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SalesOrderVO disapproveById(@PathVariable(value = "id") long id) {
        return salesOrderService.disapproveById(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return salesOrderService.delete(id);
    }
}