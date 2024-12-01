package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.NFController;
import com.deopraglabs.api_prysme.controller.QuotationController;
import com.deopraglabs.api_prysme.data.model.*;
import com.deopraglabs.api_prysme.data.vo.NFVO;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.custom.ItemProductMapper;
import com.deopraglabs.api_prysme.mapper.custom.NFMapper;
import com.deopraglabs.api_prysme.repository.ItemProductRepository;
import com.deopraglabs.api_prysme.repository.NFRepository;
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
public class NFService {

    private final Logger logger = Logger.getLogger(NFService.class.getName());

    private final NFMapper nFMapper;
    private final NFRepository nFRepository;
    private final ItemProductMapper itemProductMapper;
    private final ItemProductRepository itemProductRepository;

    @Autowired
    public NFService(NFRepository nFRepository, NFMapper nFMapper, ItemProductMapper itemProductMapper, ItemProductRepository itemProductRepository) {
        this.nFRepository = nFRepository;
        this.nFMapper = nFMapper;
        this.itemProductMapper = itemProductMapper;
        this.itemProductRepository = itemProductRepository;
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
            nFVO.setNfKey(Utils.generateRandomKey());
            final var nF = nFRepository.save(nFMapper.convertFromVO(nFVO));
            final List<ItemProduct> updatedItems = itemProductMapper.convertFromItemProductVOs(nFVO.getItems());

            itemProductRepository.deleteAll(nF.getSalesOrder().getItems());
            nF.getItems().clear();
            nF.getSalesOrder().getItems().clear();

            final List<ItemProduct> itemsWithNF = addNFToItems(updatedItems, nF);
            itemProductRepository.saveAll(itemsWithNF);

            nF.setItems(itemsWithNF);
            nF.getSalesOrder().setItems(itemsWithNF);
            nF.getSalesOrder().setStatus(OrderStatus.FINALIZED);
            nF.getSalesOrder().getQuotation().setQuotationStatus(QuotationStatus.CONVERTED_TO_ORDER);

            return nFMapper.convertToVO(nFRepository.save(nF))
                    .add(linkTo(methodOn(NFController.class).findById(nF.getId())).withSelfRel());
        }
    }

    public List<NFVO> findAllByCustomerId(long id) {
        logger.info("Finding all nFs by customer id " + id);
        final var nFs = nFMapper.convertToNFVOs(nFRepository.findAllByCustomerId(id));
        nFs.forEach(salesOrder -> salesOrder.add(linkTo(methodOn(QuotationController.class).findById(salesOrder.getKey())).withSelfRel()));

        return nFs;
    }

    public List<ItemProduct> addNFToItems(List<ItemProduct> items, NF nf) {
        items.forEach(item -> item.setNf(nf));
        items.forEach(item -> item.setSalesOrder(nf.getSalesOrder()));
        items.forEach(item -> item.setQuotation(nf.getSalesOrder().getQuotation()));
        return items;
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
