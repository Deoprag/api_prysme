package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.SalesOrder;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.QuotationRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SalesOrderMapper {

    private final UserRepository userRepository;
    private final QuotationMapper quotationMapper;
    private final CustomerRepository customerRepository;
    private final ItemProductMapper itemProductMapper;
    private final QuotationRepository quotationRepository;

    @Autowired
    public SalesOrderMapper(UserRepository userRepository, QuotationMapper quotationMapper, CustomerRepository customerMapper, ItemProductMapper itemProductMapper, QuotationRepository quotationRepository) {
        this.userRepository = userRepository;
        this.quotationMapper = quotationMapper;
        this.customerRepository = customerMapper;
        this.itemProductMapper = itemProductMapper;
        this.quotationRepository = quotationRepository;
    }

    public SalesOrderVO convertToVO(SalesOrder salesOrder) {
        final SalesOrderVO vo = new SalesOrderVO();

        vo.setKey(salesOrder.getId());
        vo.setQuotationId(salesOrder.getQuotation().getId());
        vo.setCustomerId(salesOrder.getCustomer().getId());
        vo.setCustomer(salesOrder.getCustomer().getName());
        vo.setSellerId(salesOrder.getSeller().getId());
        vo.setSeller(salesOrder.getSeller().getUsername());
        vo.setStatus(salesOrder.getStatus());
        vo.setItems(itemProductMapper.convertToItemProductVOs(salesOrder.getItems()));
        vo.setNotes(salesOrder.getNotes());
        vo.setCreatedDate(salesOrder.getCreatedDate());
        vo.setLastModifiedDate(salesOrder.getLastModifiedDate());
        vo.setCreatedBy(salesOrder.getCreatedBy() != null ? salesOrder.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(salesOrder.getLastModifiedBy() != null ? salesOrder.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public SalesOrder convertFromVO(SalesOrderVO vo) {
        return updateFromVO(new SalesOrder(), vo);
    }

    public SalesOrder updateFromVO(SalesOrder salesOrder, SalesOrderVO salesOrderVO) {
        salesOrder.setId(salesOrderVO.getKey());
        salesOrder.setQuotation(quotationRepository.findById(salesOrderVO.getQuotationId()).orElseThrow(() -> new CustomRuntimeException.QuotationNotFoundException(salesOrderVO.getQuotationId())));
        salesOrder.setCustomer(customerRepository.findById(salesOrderVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(salesOrderVO.getCustomerId())));
        salesOrder.setSeller(userRepository.findById(salesOrderVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(salesOrderVO.getSellerId())));
        salesOrder.setStatus(salesOrderVO.getStatus());
        salesOrder.setNotes(salesOrderVO.getNotes());
        salesOrder.setItems(itemProductMapper.convertFromItemProductVOs(salesOrderVO.getItems()));

        return salesOrder;
    }

    public List<SalesOrderVO> convertToSalesOrderVOs(List<SalesOrder> salesOrders) {
        final List<SalesOrderVO> vos = new ArrayList<>();
        for (final SalesOrder salesOrder : salesOrders) {
            vos.add(convertToVO(salesOrder));
        }
        return vos;
    }

    public List<SalesOrder> convertFromSalesOrderVOs(List<SalesOrderVO> vos) {
        final List<SalesOrder> salesOrders = new ArrayList<>();
        for (final SalesOrderVO vo : vos) {
            salesOrders.add(convertFromVO(vo));
        }
        return salesOrders;
    }
}