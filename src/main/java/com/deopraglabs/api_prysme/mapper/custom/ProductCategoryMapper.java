package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ProductCategory;
import com.deopraglabs.api_prysme.data.vo.ProductCategoryVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryMapper {

    public ProductCategoryVO convertToVO(ProductCategory category) {
        final ProductCategoryVO vo = new ProductCategoryVO();

        vo.setKey(category.getId());
        vo.setName(category.getName());
        vo.setCreatedDate(category.getCreatedDate());
        vo.setLastModifiedDate(category.getLastModifiedDate());

        return vo;
    }

    public ProductCategory convertFromVO(ProductCategoryVO categoryVO) {
        return updateFromVO(new ProductCategory(), categoryVO);
    }

    public ProductCategory updateFromVO(ProductCategory category, ProductCategoryVO categoryVO) {
        category.setName(categoryVO.getName());
        category.setCreatedDate(categoryVO.getCreatedDate());
        category.setLastModifiedDate(categoryVO.getLastModifiedDate());

        return category;
    }

    public List<ProductCategoryVO> convertToProductCategoryVOs(List<ProductCategory> categories) {
        final List<ProductCategoryVO> listVO = new ArrayList<>();

        for (final ProductCategory category : categories) {
            listVO.add(this.convertToVO(category));
        }

        return listVO;
    }

    public List<ProductCategory> convertFromProductCategoryVOs(List<ProductCategoryVO> categoryVOs) {
        final List<ProductCategory> listCategory = new ArrayList<>();

        for (final ProductCategoryVO categoryVO : categoryVOs) {
            listCategory.add(this.convertFromVO(categoryVO));
        }

        return listCategory;
    }
}
