package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.NFDTO;
import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.NF;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.ItemProductRepository;
import com.deopraglabs.api_prysme.repository.SalesOrderRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NFMapperImpl implements Mapper<NF, NFDTO> {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final ItemProductRepository itemProductRepository;

    @Autowired
    public NFMapperImpl(CustomerRepository customerRepository, UserRepository userRepository,
                        SalesOrderRepository salesOrderRepository, ItemProductRepository itemProductRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.itemProductRepository = itemProductRepository;
    }

    @Override
    public NFDTO toDTO(NF entity) {
        final NFDTO dto = DozerMapper.parseObject(entity, NFDTO.class);
        
        // Set customer ID
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }
        
        // Set seller ID
        if (entity.getSeller() != null) {
            dto.setSellerId(entity.getSeller().getId());
        }
        
        // Set sales order ID
        if (entity.getSalesOrder() != null) {
            dto.setSalesOrderId(entity.getSalesOrder().getId());
        }
        
        // Set item product IDs
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            dto.setItemProductIds(entity.getItems().stream()
                    .map(ItemProduct::getId)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    @Override
    public NF toEntity(NFDTO dto) {
        final NF entity = DozerMapper.parseObject(dto, NF.class);
        
        // Set customer
        if (dto.getCustomerId() != null) {
            customerRepository.findById(dto.getCustomerId())
                    .ifPresent(entity::setCustomer);
        }
        
        // Set seller
        if (dto.getSellerId() != null) {
            userRepository.findById(dto.getSellerId())
                    .ifPresent(entity::setSeller);
        }
        
        // Set sales order
        if (dto.getSalesOrderId() != null) {
            salesOrderRepository.findById(dto.getSalesOrderId())
                    .ifPresent(entity::setSalesOrder);
        }
        
        // Set item products
        if (dto.getItemProductIds() != null && !dto.getItemProductIds().isEmpty()) {
            final List<ItemProduct> items = itemProductRepository.findAllById(dto.getItemProductIds());
            entity.setItems(items);
        }
        
        return entity;
    }

    @Override
    public List<NFDTO> toDTOList(List<NF> entities) {
        return DozerMapper.parseListObjects(entities, NFDTO.class);
    }

    @Override
    public List<NF> toEntityList(List<NFDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
