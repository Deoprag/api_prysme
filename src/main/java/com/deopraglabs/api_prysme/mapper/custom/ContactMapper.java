package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Contact;
import com.deopraglabs.api_prysme.data.vo.ContactVO;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactMapper {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ContactInfoMapper contactInfoMapper;

    @Autowired
    public ContactMapper(UserRepository userRepository, CustomerMapper customerMapper, ContactInfoMapper contactInfoMapper, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerMapper = customerMapper;
        this.contactInfoMapper = contactInfoMapper;
        this.customerRepository = customerRepository;
    }

    public ContactVO convertToVO(Contact contact) {
        final ContactVO vo = new ContactVO();

        vo.setKey(contact.getId());
        vo.setSellerId(contact.getSeller().getId());
        vo.setSeller(contact.getSeller().getFullName());
        vo.setCustomerId(contact.getCustomer().getId());
        vo.setCustomer(contact.getCustomer().getName());
        vo.setInfo(contactInfoMapper.convertToVO(contact.getInfo()));
        vo.setCustomerStatus(contact.getCustomerStatus());
        vo.setNotes(contact.getNotes());
        vo.setContactDate(contact.getContactDate());
        vo.setCreatedDate(contact.getCreatedDate());
        vo.setLastModifiedDate(contact.getLastModifiedDate());
        vo.setCreatedBy(contact.getCreatedBy() != null ? contact.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(contact.getLastModifiedBy() != null ? contact.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Contact convertFromVO(ContactVO contactVO) {
        return updateFromVO(new Contact(), contactVO);
    }

    public Contact updateFromVO(Contact contact, ContactVO contactVO) {
        contact.setSeller(userRepository.findById(contactVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.SellerNotFoundException(contactVO.getSellerId())));
        contact.setCustomer(customerRepository.findById(contactVO.getCustomerId()).orElseThrow(() -> new CustomRuntimeException.SellerNotFoundException(contactVO.getSellerId())));
        contact.setInfo(contactInfoMapper.convertFromVO(contactVO.getInfo()));
        contact.setCustomerStatus(contactVO.getCustomerStatus());
        contact.setNotes(Utils.isEmpty(contactVO.getNotes()) ? null : contactVO.getNotes());
        contact.setContactDate(contactVO.getContactDate());
        contact.setCreatedDate(contactVO.getCreatedDate());
        contact.setLastModifiedDate(contactVO.getLastModifiedDate());
        contact.setCreatedBy(userRepository.findByUsername(contactVO.getCreatedBy()));
        contact.setLastModifiedBy(userRepository.findByUsername(contactVO.getLastModifiedBy()));

        return contact;
    }

    public List<ContactVO> convertToContactVOs(List<Contact> contacts) {
        final List<ContactVO> listVO = new ArrayList<>();

        for (final Contact contact : contacts) {
            listVO.add(this.convertToVO(contact));
        }

        return listVO;
    }

    public List<Contact> convertFromContactVOs(List<ContactVO> contactVOs) {
        final List<Contact> listContact = new ArrayList<>();

        for (final ContactVO contactVO : contactVOs) {
            listContact.add(this.convertFromVO(contactVO));
        }

        return listContact;
    }
}
