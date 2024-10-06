package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.ContactInfo;
import com.deopraglabs.api_prysme.data.vo.ContactInfoVO;
import com.deopraglabs.api_prysme.repository.ContactRepository;
import com.deopraglabs.api_prysme.service.CustomerService;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoMapper {

    private final ContactRepository contactRepository;
    private final CustomerService customerService;

    @Autowired
    public ContactInfoMapper(ContactRepository contactRepository, CustomerService customerService) {
        this.contactRepository = contactRepository;
        this.customerService = customerService;
    }

    public ContactInfoVO convertToVO(ContactInfo contactInfo) {
        final ContactInfoVO vo = new ContactInfoVO();

        vo.setId(contactInfo.getId());
        vo.setContactType(contactInfo.getContactType());
        vo.setValue(contactInfo.getValue());
        vo.setContactId(contactInfo.getContact() != null ? contactInfo.getContact().getId() : 0);
        vo.setContactName(contactInfo.getContact());

        return vo;
    }

    public ContactInfo convertFromVO(ContactInfoVO contactInfoVO) {
        return updateFromVO(new ContactInfo(), contactInfoVO);
    }

    public ContactInfo updateFromVO(ContactInfo contactInfo, ContactInfoVO contactInfoVO) {
        contactInfo.setContactType(contactInfoVO.getContactType());
        contactInfo.setValue(Utils.isEmpty(contactInfoVO.getValue()) ? null : contactInfoVO.getValue());
        contactInfo.setContact(contactRepository.findById(contactInfoVO.getContactId()).orElseThrow(() -> new CustomRuntimeException.ContactNotFoundException(contactInfoVO.getId())));

        return contactInfo;
    }
}
