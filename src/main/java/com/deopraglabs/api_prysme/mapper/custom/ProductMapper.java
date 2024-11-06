package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Product;
import com.deopraglabs.api_prysme.data.vo.ProductVO;
import com.deopraglabs.api_prysme.repository.ProductCategoryRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductMapper {

    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductMapper(ProductCategoryRepository productCategoryRepository, UserRepository userRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.userRepository = userRepository;
    }

    public ProductVO convertToVO(Product product) {
        final ProductVO vo = new ProductVO();

        vo.setKey(product.getId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());
        vo.setCategoryId(product.getCategory().getId());
        vo.setCategoryName(product.getCategory().getName());
        vo.setActive(product.isActive());
        vo.setCreatedDate(product.getCreatedDate());
        vo.setLastModifiedDate(product.getLastModifiedDate());
        vo.setCreatedBy(product.getCreatedBy() != null ? product.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(product.getLastModifiedBy() != null ? product.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Product convertFromVO(ProductVO productVO) {
        return updateFromVO(new Product(), productVO);
    }

    public Product updateFromVO(Product product, ProductVO productVO) {
        product.setName(productVO.getName());
        product.setDescription(productVO.getDescription());
        product.setPrice(productVO.getPrice());
        product.setCategory(productCategoryRepository.findById(productVO.getCategoryId())
                .orElseThrow(() -> new CustomRuntimeException.ProductCategoryNotFoundException(productVO.getCategoryId())));
        product.setActive(productVO.isActive());
        product.setCreatedDate(productVO.getCreatedDate());
        product.setLastModifiedDate(productVO.getLastModifiedDate());
        product.setCreatedBy(userRepository.findByUsername(productVO.getCreatedBy()));
        product.setLastModifiedBy(userRepository.findByUsername(productVO.getLastModifiedBy()));

        return product;
    }

    public List<ProductVO> convertToProductVOs(List<Product> products) {
        final List<ProductVO> listVO = new ArrayList<>();

        for (final Product product : products) {
            listVO.add(this.convertToVO(product));
        }

        return listVO;
    }

    public List<Product> convertFromProductVOs(List<ProductVO> productVOs) {
        final List<Product> listProduct = new ArrayList<>();

        for (final ProductVO productVO : productVOs) {
            listProduct.add(this.convertFromVO(productVO));
        }

        return listProduct;
    }
}
