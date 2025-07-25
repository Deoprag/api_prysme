package com.deopraglabs.api_prysme.service;


import com.deopraglabs.api_prysme.data.dto.ProductRequestDTO;
import com.deopraglabs.api_prysme.data.dto.ProductResponseDTO;
import com.deopraglabs.api_prysme.mapper.impl.ProductMapperImpl;
import com.deopraglabs.api_prysme.repository.ProductRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;



@Service
@Transactional
public class ProductService {

    private final Logger logger = Logger.getLogger(ProductService.class.getName());

    private final ProductRepository productRepository;
    private final ProductMapperImpl productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapperImpl productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDTO save(ProductRequestDTO productRequestDTO) {
        logger.info("Saving product: " + productRequestDTO);
        final List<String> validations = validateProductInfo(productRequestDTO, null);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        final var entity = productMapper.fromRequestDTO(productRequestDTO);
        final var savedEntity = productRepository.save(entity);
        return productMapper.toResponseDTO(savedEntity);
    }

    public ProductResponseDTO update(UUID id, ProductRequestDTO productRequestDTO) {
        logger.info("Updating product with id: " + id);
        final var existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
                
        final List<String> validations = validateProductInfo(productRequestDTO, id);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }
        
        final var updatedEntity = productMapper.fromRequestDTO(productRequestDTO);
        updatedEntity.setId(id);
        updatedEntity.setCreatedDate(existingEntity.getCreatedDate());
        updatedEntity.setCreatedBy(existingEntity.getCreatedBy());
        
        final var savedEntity = productRepository.save(updatedEntity);
        return productMapper.toResponseDTO(savedEntity);
    }

    public List<ProductResponseDTO> findAll() {
        logger.info("Finding all products");
        return productMapper.toResponseDTOList(productRepository.findAll());
    }

    public ProductResponseDTO findById(UUID id) {
        logger.info("Finding product by id: " + id);
        final var entity = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        return productMapper.toResponseDTO(entity);
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting product: " + id);
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Business Rules - Restored and adapted for DTOs
    private List<String> validateProductInfo(ProductRequestDTO productRequestDTO, UUID existingProductId) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(productRequestDTO, validations);
        validateUniqueFields(productRequestDTO, validations, existingProductId);

        return validations;
    }

    private void validateBasicFields(ProductRequestDTO productRequestDTO, List<String> validations) {
        Utils.checkField(validations, Utils.isEmpty(productRequestDTO.getName()), "Product name is required");
        Utils.checkField(validations, productRequestDTO.getPrice() == null, "Product price is required");
        Utils.checkField(validations, productRequestDTO.getPrice() != null && productRequestDTO.getPrice().signum() <= 0, "Product price must be greater than zero");
        Utils.checkField(validations, productRequestDTO.getCategoryId() == null, "Product category is required");
    }

    private void validateUniqueFields(ProductRequestDTO productRequestDTO, List<String> validations, UUID existingProductId) {
        // Check if product name is unique
        if (!Utils.isEmpty(productRequestDTO.getName())) {
            boolean nameExists = productRepository.findAll().stream()
                    .anyMatch(product -> product.getName().equalsIgnoreCase(productRequestDTO.getName()) 
                            && !product.getId().equals(existingProductId));
            if (nameExists) {
                validations.add("Product name already exists");
            }
        }
    }
}
