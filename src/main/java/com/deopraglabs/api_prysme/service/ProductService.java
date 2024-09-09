package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.ProductController;
import com.deopraglabs.api_prysme.data.vo.ProductVO;
import com.deopraglabs.api_prysme.mapper.custom.ProductMapper;
import com.deopraglabs.api_prysme.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final Logger logger = Logger.getLogger(ProductService.class.getName());

    public ProductVO save(ProductVO productVO) {
        logger.info("Saving product: " + productVO);
        if (productVO.getKey() > 0) {
            return ProductMapper.convertToVO(productRepository.save(ProductMapper.updateFromVO(
                    productRepository.findById(productVO.getKey())
                            .orElseThrow(() -> new NoSuchElementException("User not found")),
                    productVO
            )));
        } else {
            return ProductMapper.convertToVO(productRepository.save(ProductMapper.convertFromVO(productVO)));
        }
    }

    public List<ProductVO> findAll() {
        logger.info("Finding all products");
        final var products = ProductMapper.convertToProductVOs(productRepository.findAll());
        products.forEach(product -> product.add(linkTo(methodOn(ProductController.class).findById(product.getKey())).withSelfRel()));

        return products;
    }

    public ProductVO findById(long id) {
        logger.info("Finding product by id: " + id);
        return ProductMapper.convertToVO(productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found")))
                .add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting product: " + id);
        return productRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
