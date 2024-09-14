package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.ProductController;
import com.deopraglabs.api_prysme.data.model.ProductCategory;
import com.deopraglabs.api_prysme.data.vo.ProductCategoryVO;
import com.deopraglabs.api_prysme.mapper.custom.ProductCategoryMapper;
import com.deopraglabs.api_prysme.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class ProductCategoryService {

    private final Logger logger = Logger.getLogger(ProductService.class.getName());

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    public ProductCategoryVO save(ProductCategoryVO productVO) {
        logger.info("Saving product: " + productVO);
        if (productVO.getKey() > 0) {
            return productCategoryMapper.convertToVO(productCategoryRepository.save(productCategoryMapper.updateFromVO(
                    productCategoryRepository.findById(productVO.getKey())
                            .orElseThrow(() -> new NoSuchElementException("User not found")),
                    productVO
            )));
        } else {
            return productCategoryMapper.convertToVO(productCategoryRepository.save(productCategoryMapper.convertFromVO(productVO)));
        }
    }

    public List<ProductCategoryVO> findAll() {
        logger.info("Finding all products");
        final var products = productCategoryMapper.convertToProductCategoryVOs(productCategoryRepository.findAll());
        products.forEach(product -> product.add(linkTo(methodOn(ProductController.class).findById(product.getKey())).withSelfRel()));

        return products;
    }

    public ProductCategoryVO findById(long id) {
        logger.info("Finding product by id: " + id);
        return productCategoryMapper.convertToVO(productCategoryRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found")))
                .add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting product: " + id);
        return productCategoryRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
