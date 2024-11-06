package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.Quotation;
import com.deopraglabs.api_prysme.data.model.QuotationStatus;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public QuotationMapper(CustomerMapper customerMapper, UserMapper userMapper, UserRepository userRepository) {
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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
        vo.setCreatedBy(quotation.getCreatedBy() != null ? quotation.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(quotation.getLastModifiedBy() != null ? quotation.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Quotation convertFromVO(QuotationVO vo) {
        return updateFromVO(new Quotation(), vo);
    }

    public Quotation updateFromVO(Quotation quotation, QuotationVO quotationVO) {
        quotation.setCustomer(customerMapper.convertFromVO(quotationVO.getCustomer()));
        quotation.setSeller(userMapper.convertFromVO(quotationVO.getSeller()));
        quotation.setDateTime(quotationVO.getDateTime() != null ? quotationVO.getDateTime() : LocalDateTime.now());
        quotation.setQuotationStatus(quotationVO.getQuotationStatus() != null ? quotationVO.getQuotationStatus() : QuotationStatus.OPEN);
        quotation.setItems(Mapper.parseListObjects(quotationVO.getItems(), ItemProduct.class));
        quotation.setCreatedDate(quotationVO.getCreatedDate());
        quotation.setLastModifiedDate(quotationVO.getLastModifiedDate());
        quotation.setCreatedBy(userRepository.findByUsername(quotationVO.getCreatedBy()));
        quotation.setLastModifiedBy(userRepository.findByUsername(quotationVO.getLastModifiedBy()));

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