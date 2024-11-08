package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ProductCategory;
import com.deopraglabs.api_prysme.data.vo.ProductCategoryVO;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryMapper {

    private final UserRepository userRepository;

    @Autowired
    public ProductCategoryMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProductCategoryVO convertToVO(ProductCategory category) {
        final ProductCategoryVO vo = new ProductCategoryVO();

        vo.setKey(category.getId());
        vo.setName(category.getName());
        vo.setCreatedDate(category.getCreatedDate());
        vo.setLastModifiedDate(category.getLastModifiedDate());
        vo.setCreatedBy(category.getCreatedBy() != null ? category.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(category.getLastModifiedBy() != null ? category.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public ProductCategory convertFromVO(ProductCategoryVO categoryVO) {
        return updateFromVO(new ProductCategory(), categoryVO);
    }

    public ProductCategory updateFromVO(ProductCategory category, ProductCategoryVO categoryVO) {
        category.setName(categoryVO.getName());
        category.setCreatedDate(categoryVO.getCreatedDate());

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
