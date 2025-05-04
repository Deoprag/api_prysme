package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.SalesOrderDTO;
import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.NF;
import com.deopraglabs.api_prysme.data.model.SalesOrder;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesOrderMapperImpl implements Mapper<SalesOrder, SalesOrderDTO> {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final QuotationRepository quotationRepository;
    private final ItemProductRepository itemProductRepository;
    private final NFRepository nfRepository;

    @Autowired
    public SalesOrderMapperImpl(CustomerRepository customerRepository, UserRepository userRepository,
                              QuotationRepository quotationRepository, ItemProductRepository itemProductRepository,
                              NFRepository nfRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.quotationRepository = quotationRepository;
        this.itemProductRepository = itemProductRepository;
        this.nfRepository = nfRepository;
    }

    @Override
    public SalesOrderDTO toDTO(SalesOrder entity) {
        final SalesOrderDTO dto = DozerMapper.parseObject(entity, SalesOrderDTO.class);
        
        // Set customer ID
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }
        
        // Set sales rep ID
        if (entity.getSalesRep() != null) {
            dto.setSalesRepId(entity.getSalesRep().getId());
        }
        
        // Set quotation ID
        if (entity.getQuotation() != null) {
            dto.setQuotationId(entity.getQuotation().getId());
        }
        
        // Set item product IDs
        if (entity.getItems() != null && !entity.getItems().isEmpty()) {
            dto.setItemProductIds(entity.getItems().stream()
                    .map(ItemProduct::getId)
                    .collect(Collectors.toList()));
        }
        
        // Set NF IDs
        if (entity.getNfs() != null && !entity.getNfs().isEmpty()) {
            dto.setNfIds(entity.getNfs().stream()
                    .map(NF::getId)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    @Override
    public SalesOrder toEntity(SalesOrderDTO dto) {
        final SalesOrder entity = DozerMapper.parseObject(dto, SalesOrder.class);
        
        // Set customer
        if (dto.getCustomerId() != null) {
            customerRepository.findById(dto.getCustomerId())
                    .ifPresent(entity::setCustomer);
        }
        
        // Set sales rep
        if (dto.getSalesRepId() != null) {
            userRepository.findById(dto.getSalesRepId())
                    .ifPresent(entity::setSalesRep);
        }
        
        // Set quotation
        if (dto.getQuotationId() != null) {
            quotationRepository.findById(dto.getQuotationId())
                    .ifPresent(entity::setQuotation);
        }
        
        // Set item products
        if (dto.getItemProductIds() != null && !dto.getItemProductIds().isEmpty()) {
            final List<ItemProduct> items = itemProductRepository.findAllById(dto.getItemProductIds());
            entity.setItems(items);
        }
        
        // Set NFs
        if (dto.getNfIds() != null && !dto.getNfIds().isEmpty()) {
            final List<NF> nfs = nfRepository.findAllById(dto.getNfIds());
            entity.setNfs(nfs);
        }
        
        return entity;
    }

    @Override
    public List<SalesOrderDTO> toDTOList(List<SalesOrder> entities) {
        return DozerMapper.parseListObjects(entities, SalesOrderDTO.class);
    }

    @Override
    public List<SalesOrder> toEntityList(List<SalesOrderDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
