package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.NFController;
import com.deopraglabs.api_prysme.data.vo.NFVO;
import com.deopraglabs.api_prysme.mapper.custom.NFMapper;
import com.deopraglabs.api_prysme.repository.NFRepository;
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
public class NFService {

    private final Logger logger = Logger.getLogger(NFService.class.getName());

    private final NFMapper nFMapper;
    private final NFRepository nFRepository;

    @Autowired
    public NFService(NFRepository nFRepository, NFMapper nFMapper) {
        this.nFRepository = nFRepository;
        this.nFMapper = nFMapper;
    }

    public NFVO save(NFVO nFVO) {
        logger.info("Saving nF: " + nFVO);
        final List<String> validations = validateNFInfo(nFVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (nFVO.getKey() > 0) {
            return nFMapper.convertToVO(nFRepository.save(nFMapper.updateFromVO(
                    nFRepository.findById(nFVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.NFNotFoundException(nFVO.getKey())),
                    nFVO
            ))).add(linkTo(methodOn(NFController.class).findById(nFVO.getKey())).withSelfRel());
        } else {
            final var nF = nFRepository.save(nFMapper.convertFromVO(nFVO));
            return nFMapper.convertToVO(nFRepository.save(nF))
                    .add(linkTo(methodOn(NFController.class).findById(nF.getId())).withSelfRel());
        }
    }

    public List<NFVO> findAll() {
        logger.info("Finding all nFs");
        final var nFs = nFMapper.convertToNFVOs(nFRepository.findAll());
        nFs.forEach(nF -> nF.add(linkTo(methodOn(NFController.class).findById(nF.getKey())).withSelfRel()));

        return nFs;
    }

    public NFVO findById(long id) {
        logger.info("Finding nF by id: " + id);
        return nFMapper.convertToVO(nFRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.NFNotFoundException(id)))
                .add(linkTo(methodOn(NFController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting nF: " + id);
        return nFRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateNFInfo(NFVO nFVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(nFVO, validations);
        validateUniqueFields(nFVO, validations);

        return validations;
    }

    private void validateBasicFields(NFVO nFVO, List<String> validations) {

    }

    private void validateUniqueFields(NFVO nFVO, List<String> validations) {

    }
}
