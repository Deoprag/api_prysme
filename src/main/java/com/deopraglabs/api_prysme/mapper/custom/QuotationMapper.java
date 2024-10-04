package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Quotation;
import com.deopraglabs.api_prysme.data.model.QuotationStatus;
import com.deopraglabs.api_prysme.data.vo.QuotationVO;
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

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuotationMapper(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    public QuotationVO convertToVO(Quotation quotation) {
        final QuotationVO vo = new QuotationVO();

        vo.setKey(quotation.getId());
        vo.setCustomerId(quotation.getCustomer().getId());
        vo.setCustomerName(quotation.getCustomer().getName());
        vo.setSellerId(quotation.getSeller().getId());
        vo.setSellerName(quotation.getSeller().getFirstName() + " " + quotation.getSeller().getLastName());
        vo.setDateTime(quotation.getDateTime());
        vo.setQuotationStatus(quotation.getQuotationStatus());
        vo.setItems(quotation.getItems());
        vo.setCreatedDate(quotation.getCreatedDate());
        vo.setLastModifiedDate(quotation.getLastModifiedDate());

        return vo;
    }

    public Quotation convertFromVO(QuotationVO quotationVO) {
        return updateFromVO(new Quotation(), quotationVO);
    }

    public Quotation updateFromVO(Quotation quotation, QuotationVO quotationVO) {
        quotation.setCustomer(customerRepository.findById(quotationVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(quotationVO.getCustomerId())));
        quotation.setSeller(userRepository.findById(quotationVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(quotationVO.getSellerId())));
        quotation.setDateTime(quotationVO.getDateTime() != null ? quotationVO.getDateTime() : LocalDateTime.now());
        quotation.setQuotationStatus(quotationVO.getQuotationStatus() != null ? quotationVO.getQuotationStatus() : QuotationStatus.OPEN);
        quotation.setItems(quotationVO.getItems() != null ? quotationVO.getItems() : new ArrayList<>());
        quotation.setCreatedDate(quotationVO.getCreatedDate() != null ? quotationVO.getCreatedDate() : new Date());
        quotation.setLastModifiedDate(quotationVO.getLastModifiedDate() != null ? quotationVO.getLastModifiedDate() : new Date());

        return quotation;
    }

    public List<QuotationVO> convertToQuotationVOs(List<Quotation> quotations) {
        final List<QuotationVO> vos = new ArrayList<>();
        for (final Quotation quotation : quotations) {
            vos.add(convertToVO(quotation));
        }
        return vos;
    }

    public List<Quotation> convertFromQuotationVOs(List<QuotationVO> quotationVOs) {
        final List<Quotation> quotations = new ArrayList<>();
        for (final QuotationVO quotationVO : quotationVOs) {
            quotations.add(convertFromVO(quotationVO));
        }
        return quotations;
    }
}