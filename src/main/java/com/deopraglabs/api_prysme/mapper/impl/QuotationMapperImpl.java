package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.QuotationDTO;
import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.Quotation;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.ItemProductRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuotationMapperImpl implements Mapper<Quotation, QuotationDTO> {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ItemProductRepository itemProductRepository;

    @Autowired
    public QuotationMapperImpl(CustomerRepository customerRepository, UserRepository userRepository,
                              ItemProductRepository itemProductRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.itemProductRepository = itemProductRepository;
    }

    @Override
    public QuotationDTO toDTO(Quotation entity) {
        final QuotationDTO dto = DozerMapper.parseObject(entity, QuotationDTO.class);
        
        // Set customer ID
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }
        
        // Set sales rep ID
        if (entity.getSalesRep() != null) {
            dto.setSalesRepId(entity.getSalesRep().getId());
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
    public Quotation toEntity(QuotationDTO dto) {
        final Quotation entity = DozerMapper.parseObject(dto, Quotation.class);
        
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
        
        // Set item products
        if (dto.getItemProductIds() != null && !dto.getItemProductIds().isEmpty()) {
            final List<ItemProduct> items = itemProductRepository.findAllById(dto.getItemProductIds());
            entity.setItems(items);
        }
        
        return entity;
    }

    @Override
    public List<QuotationDTO> toDTOList(List<Quotation> entities) {
        return DozerMapper.parseListObjects(entities, QuotationDTO.class);
    }

    @Override
    public List<Quotation> toEntityList(List<QuotationDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
