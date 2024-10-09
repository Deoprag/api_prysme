package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.vo.NFVO;
import com.deopraglabs.api_prysme.service.NFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/nF")
public class NFController {

    private final NFService nFService;

    @Autowired
    public NFController(NFService nFService) {
        this.nFService = nFService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<NFVO> findAll() {
        return nFService.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public NFVO findById(@PathVariable(value = "id") long id) {
        return nFService.findById(id);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public NFVO create(@RequestBody NFVO nF) {
        return nFService.save(nF);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public NFVO update(@RequestBody NFVO nF) {
        return nFService.save(nF);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return nFService.delete(id);
    }
}

