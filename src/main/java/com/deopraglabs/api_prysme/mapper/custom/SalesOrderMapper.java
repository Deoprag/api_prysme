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

    @Autowired
    public SalesOrderMapper(UserMapper userMapper, QuotationMapper quotationMapper, CustomerMapper customerMapper) {
        this.userMapper = userMapper;
        this.quotationMapper = quotationMapper;
        this.customerMapper = customerMapper;
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

        return vo;
    }

    public SalesOrder convertFromVO(SalesOrderVO vo) {
        return updateFromVO(new SalesOrder(), vo);
    }

    public SalesOrder updateFromVO(SalesOrder salesOrder, SalesOrderVO vo) {
        salesOrder.setId(vo.getKey());
        salesOrder.setQuotation(quotationMapper.convertFromVO(vo.getQuotation()));
        salesOrder.setCustomer(customerMapper.convertFromVO(vo.getCustomer()));
        salesOrder.setSeller(userMapper.convertFromVO(vo.getSeller()));
        salesOrder.setDateTime(vo.getDateTime());
        salesOrder.setStatus(vo.getStatus());
        salesOrder.setItems(Mapper.parseListObjects(vo.getItems(), ItemProduct.class));
        salesOrder.setCreatedDate(vo.getCreatedDate() != null ? vo.getCreatedDate() : new Date());
        salesOrder.setLastModifiedDate(vo.getLastModifiedDate() != null ? vo.getLastModifiedDate() : new Date());

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