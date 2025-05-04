package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.dto.QuotationDTO;
import com.deopraglabs.api_prysme.service.QuotationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotation")
@Tag(name = "Quotation", description = "Endpoints for managing quotations")
public class QuotationController {

    private final QuotationService quotationService;

    @Autowired
    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<QuotationDTO> findAll() {
        return quotationService.findAll();
    }

    @GetMapping(value = "/findAllByCustomerId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<QuotationDTO> findAllByCustomerId(@PathVariable(value = "id") long id) {
        return quotationService.findAllByCustomerId(id);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuotationDTO findById(@PathVariable(value = "id") long id) {
        return quotationService.findById(id);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuotationDTO create(@RequestBody QuotationDTO quotation) {
        return quotationService.save(quotation);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuotationDTO update(@RequestBody QuotationDTO quotation) {
        return quotationService.save(quotation);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return quotationService.delete(id);
    }
}
