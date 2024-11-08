package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.NF;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.NFVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NFMapper {

    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final SalesOrderMapper salesOrderMapper;
    private final UserRepository userRepository;

    @Autowired
    public NFMapper(UserMapper userMapper, CustomerMapper customerMapper, SalesOrderMapper salesOrderMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
        this.salesOrderMapper = salesOrderMapper;
        this.userRepository = userRepository;
    }

    public NFVO convertToVO(NF nF) {
        final NFVO vo = new NFVO();

        vo.setKey(nF.getId());
        vo.setIssueDate(nF.getIssueDate());
        vo.setDueDate(nF.getDueDate());
        vo.setCustomer(customerMapper.convertToVO(nF.getCustomer()));
        vo.setSeller(userMapper.convertToVO(nF.getSeller()));
        vo.setSalesOrder(salesOrderMapper.convertToVO(nF.getSalesOrder()));
        vo.setItems(Mapper.parseListObjects(nF.getItems(), ItemProductVO.class));
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
        nF.setCustomer(customerMapper.convertFromVO(nFVO.getCustomer()));
        nF.setSeller(userMapper.convertFromVO(nFVO.getSeller()));
        nF.setSalesOrder(salesOrderMapper.convertFromVO(nFVO.getSalesOrder()));
        nF.setItems(Mapper.parseListObjects(nF.getItems(), ItemProduct.class));
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
