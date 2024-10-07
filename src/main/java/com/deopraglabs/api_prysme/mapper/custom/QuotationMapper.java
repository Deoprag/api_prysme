package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.Quotation;
import com.deopraglabs.api_prysme.data.model.QuotationStatus;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuotationMapper {

    private final CustomerMapper customerMapper;
    private final UserMapper userMapper;

    @Autowired
    public QuotationMapper(CustomerMapper customerMapper, UserMapper userMapper) {
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
    }

    public QuotationVO convertToVO(Quotation quotation) {
        final QuotationVO vo = new QuotationVO();

        vo.setKey(quotation.getId());
        vo.setCustomer(customerMapper.convertToVO(quotation.getCustomer()));
        vo.setSeller(userMapper.convertToVO(quotation.getSeller()));
        vo.setDateTime(quotation.getDateTime());
        vo.setQuotationStatus(quotation.getQuotationStatus());
        vo.setItems(Mapper.parseListObjects(vo.getItems(), ItemProductVO.class));
        vo.setCreatedDate(quotation.getCreatedDate());
        vo.setLastModifiedDate(quotation.getLastModifiedDate());

        return vo;
    }

    public Quotation convertFromVO(QuotationVO vo) {
        return updateFromVO(new Quotation(), vo);
    }

    public Quotation updateFromVO(Quotation quotation, QuotationVO vo) {
        quotation.setCustomer(customerMapper.convertFromVO(vo.getCustomer()));
        quotation.setSeller(userMapper.convertFromVO(vo.getSeller()));
        quotation.setDateTime(vo.getDateTime() != null ? vo.getDateTime() : LocalDateTime.now());
        quotation.setQuotationStatus(vo.getQuotationStatus() != null ? vo.getQuotationStatus() : QuotationStatus.OPEN);
        quotation.setItems(Mapper.parseListObjects(vo.getItems(), ItemProduct.class));
        quotation.setCreatedDate(vo.getCreatedDate() != null ? vo.getCreatedDate() : new Date());
        quotation.setLastModifiedDate(vo.getLastModifiedDate() != null ? vo.getLastModifiedDate() : new Date());

        return quotation;
    }

    public List<QuotationVO> convertToQuotationVOs(List<Quotation> quotations) {
        final List<QuotationVO> vos = new ArrayList<>();
        for (final Quotation quotation : quotations) {
            vos.add(convertToVO(quotation));
        }
        return vos;
    }

    public List<Quotation> convertFromQuotationVOs(List<QuotationVO> vos) {
        final List<Quotation> quotations = new ArrayList<>();
        for (final QuotationVO vo : vos) {
            quotations.add(convertFromVO(vo));
        }
        return quotations;
    }
}