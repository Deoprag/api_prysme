package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.ProductDTO;
import com.deopraglabs.api_prysme.data.model.Product;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
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

    @Autowired
    public ProductMapperImpl(ProductCategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductDTO toDTO(Product entity) {
        final ProductDTO dto = DozerMapper.parseObject(entity, ProductDTO.class);
        
        // Set category ID
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }
        
        // Set created by and last modified by user IDs
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
        }
        
        if (entity.getLastModifiedBy() != null) {
            dto.setLastModifiedById(entity.getLastModifiedBy().getId());
        }
        
        return dto;
    }
    
    @Override
    public Product toEntity(ProductDTO dto) {
        final Product entity = DozerMapper.parseObject(dto, Product.class);
        
        // Set category
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .ifPresent(entity::setCategory);
        }
        
        // Set created by and last modified by users
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
        return DozerMapper.parseListObjects(entities, ProductDTO.class);
    }

    @Override
    public List<Product> toEntityList(List<ProductDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
