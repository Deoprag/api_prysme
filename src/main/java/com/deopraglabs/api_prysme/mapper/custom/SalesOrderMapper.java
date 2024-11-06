package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.SalesOrder;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.SalesOrderVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SalesOrderMapper {

    private final UserMapper userMapper;
    private final QuotationMapper quotationMapper;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;

    @Autowired
    public SalesOrderMapper(UserMapper userMapper, QuotationMapper quotationMapper, CustomerMapper customerMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.quotationMapper = quotationMapper;
        this.customerMapper = customerMapper;
        this.userRepository = userRepository;
    }

    public SalesOrderVO convertToVO(SalesOrder salesOrder) {
        final SalesOrderVO vo = new SalesOrderVO();

        vo.setKey(salesOrder.getId());
        vo.setQuotation(quotationMapper.convertToVO(salesOrder.getQuotation()));
        vo.setCustomer(customerMapper.convertToVO(salesOrder.getCustomer()));
        vo.setDateTime(salesOrder.getDateTime());
        vo.setStatus(salesOrder.getStatus());
        vo.setItems(Mapper.parseListObjects(vo.getItems(), ItemProductVO.class));
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
        salesOrder.setCustomer(customerMapper.convertFromVO(salerOrderVO.getCustomer()));
        salesOrder.setSeller(userMapper.convertFromVO(salerOrderVO.getSeller()));
        salesOrder.setDateTime(salerOrderVO.getDateTime());
        salesOrder.setStatus(salerOrderVO.getStatus());
        salesOrder.setItems(Mapper.parseListObjects(salerOrderVO.getItems(), ItemProduct.class));
        salesOrder.setCreatedDate(salerOrderVO.getCreatedDate());
        salesOrder.setLastModifiedDate(salerOrderVO.getLastModifiedDate());
        salesOrder.setCreatedBy(userRepository.findByUsername(salerOrderVO.getCreatedBy()));
        salesOrder.setLastModifiedBy(userRepository.findByUsername(salerOrderVO.getLastModifiedBy()));

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