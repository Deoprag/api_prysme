package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.QuotationController;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
import com.deopraglabs.api_prysme.mapper.custom.QuotationMapper;
import com.deopraglabs.api_prysme.repository.QuotationRepository;
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
public class QuotationService {

    private final Logger logger = Logger.getLogger(QuotationService.class.getName());

    private final QuotationMapper quotationMapper;
    private final QuotationRepository quotationRepository;

    @Autowired
    public QuotationService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    public QuotationVO save(QuotationVO quotationVO) {
        logger.info("Saving quotation: " + quotationVO);
        final List<String> validations = validateQuotationInfo(quotationVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (quotationVO.getKey() > 0) {
            return quotationMapper.convertToVO(quotationRepository.save(quotationMapper.updateFromVO(
                    quotationRepository.findById(quotationVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.QuotationNotFoundException(quotationVO.getKey())),
                    quotationVO
            ))).add(linkTo(methodOn(QuotationController.class).findById(quotationVO.getKey())).withSelfRel());
        } else {
            final var quotation = quotationRepository.save(quotationMapper.convertFromVO(quotationVO));
            return quotationMapper.convertToVO(quotationRepository.save(quotation))
                    .add(linkTo(methodOn(QuotationController.class).findById(quotation.getId())).withSelfRel());
        }
    }

    public List<QuotationVO> findAll() {
        logger.info("Finding all quotations");
        final var quotations = quotationMapper.convertToQuotationVOs(quotationRepository.findAll());
        quotations.forEach(quotation -> quotation.add(linkTo(methodOn(QuotationController.class).findById(quotation.getKey())).withSelfRel()));

        return quotations;
    }

    public QuotationVO findById(long id) {
        logger.info("Finding quotation by id: " + id);
        return quotationMapper.convertToVO(quotationRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.QuotationNotFoundException(id)))
                .add(linkTo(methodOn(QuotationController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting quotation: " + id);
        return quotationRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateQuotationInfo(QuotationVO quotationVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(quotationVO, validations);
        validateUniqueFields(quotationVO, validations);

        return validations;
    }

    private void validateBasicFields(QuotationVO quotationVO, List<String> validations) {

    }

    private void validateUniqueFields(QuotationVO quotationVO, List<String> validations) {
        
    }
}
