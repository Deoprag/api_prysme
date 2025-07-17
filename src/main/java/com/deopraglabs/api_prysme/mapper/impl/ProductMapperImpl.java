package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.ProductDTO;
import com.deopraglabs.api_prysme.data.dto.ProductRequestDTO;
import com.deopraglabs.api_prysme.data.dto.ProductResponseDTO;
import com.deopraglabs.api_prysme.data.model.Product;
import com.deopraglabs.api_prysme.mapper.DynamicMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.ProductCategoryRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapperImpl implements Mapper<Product, ProductDTO> {

    private final ProductCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final DynamicMapper dynamicMapper;

    @Autowired
    public ProductMapperImpl(ProductCategoryRepository categoryRepository, UserRepository userRepository, DynamicMapper dynamicMapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.dynamicMapper = dynamicMapper;
    }

    @Override
    public ProductDTO toDTO(Product entity) {
        return dynamicMapper.toDTO(entity, ProductDTO.class);
    }
    
    @Override
    public Product toEntity(ProductDTO dto) {
        Product entity = dynamicMapper.toEntity(dto, Product.class);
        
        // Resolve entity references from IDs
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .ifPresent(entity::setCategory);
        }
        
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(entity::setCreatedBy);
        }
        
        if (dto.getLastModifiedById() != null) {
            userRepository.findById(dto.getLastModifiedById())
                    .ifPresent(entity::setLastModifiedBy);
        }
        
        return entity;
    }

    @Override
    public List<ProductDTO> toDTOList(List<Product> entities) {
        return dynamicMapper.toDTOList(entities, ProductDTO.class);
    }

    @Override
    public List<Product> toEntityList(List<ProductDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO toResponseDTO(Product entity) {
        return dynamicMapper.toDTO(entity, ProductResponseDTO.class);
    }

    public Product fromRequestDTO(ProductRequestDTO dto) {
        Product entity = dynamicMapper.toEntity(dto, Product.class);
        
        // Resolve entity references from IDs
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .ifPresent(entity::setCategory);
        }
        
        return entity;
    }

    public List<ProductResponseDTO> toResponseDTOList(List<Product> entities) {
        return dynamicMapper.toDTOList(entities, ProductResponseDTO.class);
    }
}
