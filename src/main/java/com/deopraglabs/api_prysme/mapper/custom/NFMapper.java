package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.NF;
import com.deopraglabs.api_prysme.data.vo.NFVO;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.SalesOrderRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NFMapper {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final ItemProductMapper itemProductMapper;
    private final SalesOrderRepository salesOrderRepository;

    @Autowired
    public NFMapper(UserRepository userRepository, CustomerRepository customerRepository, SalesOrderMapper salesOrderMapper, ItemProductMapper itemProductMapper, SalesOrderRepository salesOrderRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.salesOrderMapper = salesOrderMapper;
        this.itemProductMapper = itemProductMapper;
        this.salesOrderRepository = salesOrderRepository;
    }

    public NFVO convertToVO(NF nF) {
        final NFVO vo = new NFVO();

        vo.setKey(nF.getId());
        vo.setIssueDate(nF.getIssueDate());
        vo.setDueDate(nF.getDueDate());
        vo.setCustomerId(nF.getCustomer().getId());
        vo.setCustomer(nF.getCustomer().getName());
        vo.setSellerId(nF.getSeller().getId());
        vo.setNfKey(nF.getNfKey());
        vo.setSeller(nF.getSeller().getUsername());
        vo.setSalesOrderId(nF.getSalesOrder().getId());
        vo.setItems(itemProductMapper.convertToItemProductVOs(nF.getItems()));
        vo.setTotalValue(nF.getTotalValue());
        vo.setDiscount(nF.getDiscount());
        vo.setDiscountType(nF.getDiscountType());
        vo.setStatus(nF.getStatus());
        vo.setObservations(nF.getObservations());
        vo.setCreatedDate(nF.getCreatedDate());
        vo.setLastModifiedDate(nF.getLastModifiedDate());
        vo.setCreatedBy(nF.getCreatedBy() != null ? nF.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(nF.getLastModifiedBy() != null ? nF.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public NF convertFromVO(NFVO vo) {
        return updateFromVO(new NF(), vo);
    }

    public NF updateFromVO(NF nF, NFVO nFVO) {
        nF.setIssueDate(nFVO.getIssueDate());
        nF.setDueDate(nFVO.getDueDate());
        nF.setNfKey(nFVO.getNfKey());
        nF.setCustomer(customerRepository.findById(nFVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(nFVO.getCustomerId())));
        nF.setSeller(userRepository.findById(nFVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(nFVO.getSellerId())));
        nF.setSalesOrder(salesOrderRepository.findById(nFVO.getSalesOrderId()).orElseThrow(() -> new CustomRuntimeException.SalesOrderNotFoundException(nFVO.getSalesOrderId())));
        nF.setItems(itemProductMapper.convertFromItemProductVOs(nFVO.getItems()));
        nF.setTotalValue(nFVO.getTotalValue());
        nF.setDiscount(nFVO.getDiscount());
        nF.setDiscountType(nFVO.getDiscountType());
        nF.setStatus(nFVO.getStatus());
        nF.setObservations(nFVO.getObservations());

        return nF;
    }

    public List<NFVO> convertToNFVOs(List<NF> nFs) {
        final List<NFVO> vos = new ArrayList<>();
        for (final NF nF : nFs) {
            vos.add(convertToVO(nF));
        }
        return vos;
    }

    public List<NF> convertFromNFVOs(List<NFVO> vos) {
        final List<NF> nFs = new ArrayList<>();
        for (final NFVO vo : vos) {
            nFs.add(convertFromVO(vo));
        }
        return nFs;
    }
}
