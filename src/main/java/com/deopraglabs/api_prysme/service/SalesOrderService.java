package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.SalesOrderController;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.custom.SalesOrderMapper;
import com.deopraglabs.api_prysme.repository.SalesOrderRepository;
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
public class SalesOrderService {

    private final Logger logger = Logger.getLogger(SalesOrderService.class.getName());

    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderRepository salesOrderRepository;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository, SalesOrderMapper salesOrderMapper) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderMapper = salesOrderMapper;
    }

    public SalesOrderVO save(SalesOrderVO salesOrderVO) {
        logger.info("Saving salesOrder: " + salesOrderVO);
        final List<String> validations = validateSalesOrderInfo(salesOrderVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (salesOrderVO.getKey() > 0) {
            return salesOrderMapper.convertToVO(salesOrderRepository.save(salesOrderMapper.updateFromVO(
                    salesOrderRepository.findById(salesOrderVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(salesOrderVO.getKey())),
                    salesOrderVO
            ))).add(linkTo(methodOn(SalesOrderController.class).findById(salesOrderVO.getKey())).withSelfRel());
        } else {
            final var salesOrder = salesOrderRepository.save(salesOrderMapper.convertFromVO(salesOrderVO));
            return salesOrderMapper.convertToVO(salesOrderRepository.save(salesOrder))
                    .add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getId())).withSelfRel());
        }
    }

    public List<SalesOrderVO> findAll() {
        logger.info("Finding all salesOrders");
        final var salesOrders = salesOrderMapper.convertToSalesOrderVOs(salesOrderRepository.findAll());
        salesOrders.forEach(salesOrder -> salesOrder.add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getKey())).withSelfRel()));

        return salesOrders;
    }

    public SalesOrderVO findById(long id) {
        logger.info("Finding salesOrder by id: " + id);
        return salesOrderMapper.convertToVO(salesOrderRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(id)))
                .add(linkTo(methodOn(SalesOrderController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting salesOrder: " + id);
        return salesOrderRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateSalesOrderInfo(SalesOrderVO salesOrderVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(salesOrderVO, validations);
        validateUniqueFields(salesOrderVO, validations);

        return validations;
    }

    private void validateBasicFields(SalesOrderVO salesOrderVO, List<String> validations) {

    }

    private void validateUniqueFields(SalesOrderVO salesOrderVO, List<String> validations) {

    }
}
