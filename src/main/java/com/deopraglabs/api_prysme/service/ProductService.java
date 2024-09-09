package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.vo.ProductVO;
import com.deopraglabs.api_prysme.mapper.custom.ProductMapper;
import com.deopraglabs.api_prysme.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final Logger logger = Logger.getLogger(ProductService.class.getName());

    public ProductVO save(ProductVO productVO) {
        logger.info("Saving product: " + productVO);
        if (productVO.getId() > 0) {
            return ProductMapper.convertToVO(productRepository.save(ProductMapper.updateFromVO(
                    productRepository.findById(productVO.getId()).orElseThrow(),
                    productVO
            )));
        } else {
            return ProductMapper.convertToVO(productRepository.save(ProductMapper.convertFromVO(productVO)));
        }
    }

    public List<ProductVO> findAll() {
        logger.info("Finding all products");
        return ProductMapper.convertToProductVOs(productRepository.findAll());
    }

    public ProductVO findById(long id) {
        logger.info("Finding product by id: " + id);
        return ProductMapper.convertToVO(productRepository.findById(id).orElseThrow());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting product: " + id);
        return productRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
