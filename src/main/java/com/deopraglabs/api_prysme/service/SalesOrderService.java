package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.QuotationController;
import com.deopraglabs.api_prysme.controller.SalesOrderController;
import com.deopraglabs.api_prysme.data.model.*;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.custom.ItemProductMapper;
import com.deopraglabs.api_prysme.mapper.custom.SalesOrderMapper;
import com.deopraglabs.api_prysme.repository.ItemProductRepository;
import com.deopraglabs.api_prysme.repository.SalesOrderRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    private final ItemProductMapper itemProductMapper;
    private final ItemProductRepository itemProductRepository;
    private final UserRepository userRepository;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository, SalesOrderMapper salesOrderMapper, ItemProductMapper itemProductMapper, ItemProductRepository itemProductRepository, UserRepository userRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderMapper = salesOrderMapper;
        this.itemProductMapper = itemProductMapper;
        this.itemProductRepository = itemProductRepository;
        this.userRepository = userRepository;
    }

    public SalesOrderVO save(SalesOrderVO salesOrderVO) {
        logger.info("Saving salesOrder: " + salesOrderVO);
        final List<String> validations = validateSalesOrderInfo(salesOrderVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (salesOrderVO.getKey() > 0) {
            var salesOrder = salesOrderRepository.findById(salesOrderVO.getKey())
                    .orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(salesOrderVO.getKey()));

            salesOrderMapper.updateFromVO(salesOrder, salesOrderVO);
            salesOrder = salesOrderRepository.save(salesOrder);

            return salesOrderMapper.convertToVO(salesOrder).add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getId())).withSelfRel());

        } else {
            final var salesOrder = salesOrderRepository.save(salesOrderMapper.convertFromVO(salesOrderVO));

            final List<ItemProduct> updatedItems = itemProductMapper.convertFromItemProductVOs(salesOrderVO.getItems());

            itemProductRepository.deleteAll(salesOrder.getQuotation().getItems());
            salesOrder.getItems().clear();
            salesOrder.getQuotation().getItems().clear();

            final List<ItemProduct> itemsWithSalesOrder = addSalesOrderToItems(updatedItems, salesOrder);
            itemProductRepository.saveAll(itemsWithSalesOrder);

            salesOrder.setItems(itemsWithSalesOrder);
            salesOrder.getQuotation().setQuotationStatus(QuotationStatus.CONVERTED_TO_ORDER);
            salesOrder.setStatus(OrderStatus.PENDING);

            return salesOrderMapper.convertToVO(salesOrderRepository.save(salesOrder))
                    .add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getId())).withSelfRel());
        }
    }

    public SalesOrderVO approveById(long id) {
        var salesOrder = salesOrderRepository.findById(id).orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(id));
        salesOrder.setStatus(OrderStatus.CONFIRMED);

        return salesOrderMapper.convertToVO(salesOrderRepository.save(salesOrder))
                .add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getId())).withSelfRel());
    }

    public SalesOrderVO disapproveById(long id) {
        var salesOrder = salesOrderRepository.findById(id).orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(id));
        salesOrder.setStatus(OrderStatus.CANCELED);

        return salesOrderMapper.convertToVO(salesOrderRepository.save(salesOrder))
                .add(linkTo(methodOn(SalesOrderController.class).findById(salesOrder.getId())).withSelfRel());
    }

    public List<ItemProduct> addSalesOrderToItems(List<ItemProduct> items, SalesOrder salesOrder) {
        items.forEach(item -> item.setSalesOrder(salesOrder));
        return items;
    }

    public List<SalesOrderVO> findAllByCustomerId(long id) {
        logger.info("Finding all salesOrders by customer id " + id);
        final var salesOrders = salesOrderMapper.convertToSalesOrderVOs(salesOrderRepository.findAllByCustomerId(id));
        salesOrders.forEach(salesOrder -> salesOrder.add(linkTo(methodOn(QuotationController.class).findById(salesOrder.getKey())).withSelfRel()));

        return salesOrders;
    }

    public List<SalesOrderVO> findAllByTeamId(long id) {
        final var user = userRepository.findById(id).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(id));
        logger.info("Finding all salesOrders by team id " + user.getTeam().getId());

        final var salesOrders = salesOrderMapper.convertToSalesOrderVOs(salesOrderRepository.findAllBySellerId(id));
        salesOrders.forEach(salesOrder -> salesOrder.add(linkTo(methodOn(QuotationController.class).findById(salesOrder.getKey())).withSelfRel()));

        return salesOrders;
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
