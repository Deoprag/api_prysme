package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.repository.NFRepository;
import com.deopraglabs.api_prysme.repository.ProductRepository;
import com.deopraglabs.api_prysme.repository.QuotationRepository;
import com.deopraglabs.api_prysme.repository.SalesOrderRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemProductMapper {

    private final ProductRepository productRepository;
    private final QuotationRepository quotationRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final NFRepository nfRepository;

    public ItemProductMapper(ProductRepository productRepository, QuotationRepository quotationRepository, SalesOrderRepository salesOrderRepository, NFRepository nfRepository) {
        this.productRepository = productRepository;
        this.quotationRepository = quotationRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.nfRepository = nfRepository;
    }

    public ItemProductVO convertToVO(ItemProduct itemProduct) {
        final ItemProductVO vo = new ItemProductVO();

        vo.setKey(itemProduct.getId());
        vo.setProductId(itemProduct.getProduct().getId());
        vo.setProduct(itemProduct.getProduct().getName());
        vo.setQuantity(itemProduct.getQuantity());
        if (itemProduct.getQuotation() != null) {
            vo.setQuotationId(itemProduct.getQuotation().getId());
        }
        if (itemProduct.getSalesOrder() != null) {
            vo.setSalesOrderId(itemProduct.getSalesOrder().getId());
        }
        if (itemProduct.getNf() != null) {
            vo.setNfId(itemProduct.getNf().getId());
        }
        vo.setPrice(itemProduct.getPrice());

        return vo;
    }

    public ItemProduct convertFromVO(ItemProductVO itemProductVO) {
        return updateFromVO(new ItemProduct(), itemProductVO);
    }

    public ItemProduct updateFromVO(ItemProduct itemProduct, ItemProductVO itemProductVO) {

        itemProduct.setProduct(productRepository.findById(itemProductVO.getProductId()).orElseThrow(() -> new CustomRuntimeException.ProductNotFoundException(itemProductVO.getProductId())));
        itemProduct.setQuantity(itemProductVO.getQuantity());
        if (itemProductVO.getQuotationId() > 0) {
            itemProduct.setQuotation(quotationRepository.findById(itemProductVO.getQuotationId()).orElse(null));
        }
        if (itemProductVO.getSalesOrderId() > 0) {
            itemProduct.setSalesOrder(salesOrderRepository.findById(itemProductVO.getSalesOrderId()).orElse(null));
        }
        if (itemProductVO.getNfId() > 0) {
            itemProduct.setNf(nfRepository.findById(itemProductVO.getNfId()).orElse(null));
        }
        itemProduct.setPrice(itemProductVO.getPrice());

        return itemProduct;
    }

    public List<ItemProductVO> convertToItemProductVOs(List<ItemProduct> itemProducts) {
        final List<ItemProductVO> listVO = new ArrayList<>();

        for (final ItemProduct itemProduct : itemProducts) {
            listVO.add(this.convertToVO(itemProduct));
        }

        return listVO;
    }

    public List<ItemProduct> convertFromItemProductVOs(List<ItemProductVO> itemProductVOs) {
        final List<ItemProduct> listItemProduct = new ArrayList<>();

        for (final ItemProductVO itemProductVO : itemProductVOs) {
            listItemProduct.add(this.convertFromVO(itemProductVO));
        }

        return listItemProduct;
    }
}
