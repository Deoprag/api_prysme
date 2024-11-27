package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.SalesOrder;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
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

    @Autowired
    public SalesOrderMapper(UserRepository userRepository, QuotationMapper quotationMapper, CustomerRepository customerMapper, ItemProductMapper itemProductMapper) {
        this.userRepository = userRepository;
        this.quotationMapper = quotationMapper;
        this.customerRepository = customerMapper;
        this.itemProductMapper = itemProductMapper;
    }

    public SalesOrderVO convertToVO(SalesOrder salesOrder) {
        final SalesOrderVO vo = new SalesOrderVO();

        vo.setKey(salesOrder.getId());
        vo.setQuotation(quotationMapper.convertToVO(salesOrder.getQuotation()));
        vo.setCustomerId(salesOrder.getCustomer().getId());
        vo.setCustomer(salesOrder.getCustomer().getName());
        vo.setSellerId(salesOrder.getSeller().getId());
        vo.setSeller(salesOrder.getSeller().getUsername());
        vo.setDateTime(salesOrder.getDateTime());
        vo.setStatus(salesOrder.getStatus());
        vo.setItems(itemProductMapper.convertToItemProductVOs(salesOrder.getItems()));
        vo.setCreatedDate(salesOrder.getCreatedDate());
        vo.setLastModifiedDate(salesOrder.getLastModifiedDate());
        vo.setCreatedBy(salesOrder.getCreatedBy() != null ? salesOrder.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(salesOrder.getLastModifiedBy() != null ? salesOrder.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public SalesOrder convertFromVO(SalesOrderVO vo) {
        return updateFromVO(new SalesOrder(), vo);
    }

    public SalesOrder updateFromVO(SalesOrder salesOrder, SalesOrderVO salerOrderVO) {
        salesOrder.setId(salerOrderVO.getKey());
        salesOrder.setQuotation(quotationMapper.convertFromVO(salerOrderVO.getQuotation()));
        salesOrder.setCustomer(customerRepository.findById(salerOrderVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(salerOrderVO.getCustomerId())));
        salesOrder.setSeller(userRepository.findById(salerOrderVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(salerOrderVO.getSellerId())));
        salesOrder.setDateTime(salerOrderVO.getDateTime());
        salesOrder.setStatus(salerOrderVO.getStatus());
        salesOrder.setItems(itemProductMapper.convertFromItemProductVOs(salerOrderVO.getItems()));

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