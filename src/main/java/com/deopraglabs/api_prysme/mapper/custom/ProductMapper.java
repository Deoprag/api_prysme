package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Product;
import com.deopraglabs.api_prysme.data.vo.ProductVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductMapper {

    public static ProductVO convertToVO(Product product) {
        final ProductVO vo = new ProductVO();

        vo.setKey(product.getId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCategory(product.getCategory());
        vo.setActive(product.isActive());

        return vo;
    }

    public static Product convertFromVO(ProductVO productVO) {
        return updateFromVO(new Product(), productVO);
    }

    public static Product updateFromVO(Product product, ProductVO productVO) {
        product.setName(productVO.getName());
        product.setDescription(productVO.getDescription());
        product.setPrice(productVO.getPrice());
        product.setStock(productVO.getStock());
        product.setCategory(productVO.getCategory());
        product.setActive(productVO.isActive());

        return product;
    }

    public static List<ProductVO> convertToProductVOs(List<Product> products) {
        final List<ProductVO> listVO = new ArrayList<>();

        for (final Product product : products) {
            listVO.add(ProductMapper.convertToVO(product));
        }

        return listVO;
    }

    public static List<Product> convertFromProductVOs(List<ProductVO> productVOs) {
        final List<Product> listProduct = new ArrayList<>();

        for (final ProductVO productVO : productVOs) {
            listProduct.add(ProductMapper.convertFromVO(productVO));
        }

        return listProduct;
    }
}
