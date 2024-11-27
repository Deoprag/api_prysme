package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.Quotation;
import com.deopraglabs.api_prysme.data.model.QuotationStatus;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
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
    private final ItemProductMapper itemProductMapper;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public QuotationMapper(CustomerMapper customerMapper, UserMapper userMapper, UserRepository userRepository, ItemProductMapper itemProductMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
        this.itemProductMapper = itemProductMapper;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public QuotationVO convertToVO(Quotation quotation) {
        final QuotationVO vo = new QuotationVO();

        vo.setKey(quotation.getId());
        vo.setCustomerId(quotation.getCustomer().getId());
        vo.setCustomer(quotation.getCustomer().getName());
        vo.setSellerId(quotation.getSeller().getId());
        vo.setSeller(quotation.getSeller().getUsername());
        vo.setDateTime(quotation.getDateTime());
        vo.setQuotationStatus(quotation.getQuotationStatus());
        vo.setItems(itemProductMapper.convertToItemProductVOs(quotation.getItems()));
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
        quotation.setCustomer(customerRepository.findById(quotationVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(quotationVO.getCustomerId())));
        quotation.setSeller(userRepository.findById(quotationVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(quotationVO.getSellerId())));
        quotation.setDateTime(quotationVO.getDateTime() != null ? quotationVO.getDateTime() : LocalDateTime.now());
        quotation.setQuotationStatus(quotationVO.getQuotationStatus() != null ? quotationVO.getQuotationStatus() : QuotationStatus.OPEN);
        quotation.setItems(itemProductMapper.convertFromItemProductVOs(quotationVO.getItems()));

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