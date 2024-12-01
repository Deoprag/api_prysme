package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Address;
import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import com.deopraglabs.api_prysme.data.vo.CustomerVO;
import com.deopraglabs.api_prysme.repository.AddressRepository;
import com.deopraglabs.api_prysme.repository.PhoneNumberRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerMapper(AddressMapper addressMapper, AddressRepository addressRepository, PhoneNumberRepository phoneNumberRepository, UserRepository userRepository) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.userRepository = userRepository;
    }

    public CustomerVO convertToVO(Customer customer) {
        final CustomerVO vo = new CustomerVO();

        vo.setKey(customer.getId());
        vo.setCpfCnpj(customer.getCpfCnpj());
        vo.setName(customer.getName());
        vo.setTradeName(customer.getTradeName());
        vo.setEmail(customer.getEmail());
        vo.setBirthFoundationDate(customer.getBirthFoundationDate());
        vo.setStateRegistration(customer.getStateRegistration());
        vo.setCustomerStatus(customer.getCustomerStatus());
        vo.setAddress(addressMapper.convertToVO(customer.getAddress()));
        for (final PhoneNumber number : customer.getPhoneNumbers()) {
            if (!vo.getPhoneNumbers().contains(number.getNumber())) vo.getPhoneNumbers().add(number.getNumber());
        }
        vo.setSeller(customer.getSeller().getUsername());
        vo.setSellerId(customer.getSeller().getId());
        vo.setCreatedDate(customer.getCreatedDate());
        vo.setLastModifiedDate(customer.getLastModifiedDate());
        vo.setCreatedBy(customer.getCreatedBy() != null ? customer.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(customer.getLastModifiedBy() != null ? customer.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Customer convertFromVO(CustomerVO customerVO) {
        return updateFromVO(new Customer(), customerVO);
    }

    public Customer updateFromVO(Customer customer, CustomerVO customerVO) {
        customer.setCpfCnpj(Utils.isEmpty(customerVO.getCpfCnpj()) ? null : Utils.removeSpecialCharacters(customerVO.getCpfCnpj()));
        customer.setName(Utils.isEmpty(customerVO.getName()) ? null : customerVO.getName());
        customer.setTradeName(Utils.isEmpty(customerVO.getTradeName()) ? null : customerVO.getTradeName());
        customer.setEmail(Utils.isEmpty(customerVO.getEmail()) ? null : customerVO.getEmail());
        customer.setBirthFoundationDate(customerVO.getBirthFoundationDate());
        customer.setStateRegistration(Utils.isEmpty(customerVO.getStateRegistration()) ? null : Utils.removeSpecialCharacters(customerVO.getStateRegistration()));
        customer.setCustomerStatus(customerVO.getCustomerStatus());
        customer.setSeller(userRepository.findById(customerVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(customerVO.getSellerId())));
        customer.setAddress(addressMapper.updateFromVO(
            addressRepository.findById(
                    customerVO.getAddress().getKey()).orElse(Address.builder().customer(customer).build()), customerVO.getAddress()
            )
        );
        customer.getAddress().setCustomer(customer);
        for (final PhoneNumber phoneNumber : phoneNumberRepository.findAllByCustomerId(customer.getId())) {
            if (!customerVO.getPhoneNumbers().contains(phoneNumber.getNumber())) {
                phoneNumber.setCustomer(null);
            }
        }
        for (final String number : customerVO.getPhoneNumbers()) {
            final var phoneNumber = phoneNumberRepository.findByNumber(Utils.removeSpecialCharacters(number));
            customer.getPhoneNumbers().add(Objects.requireNonNullElseGet(phoneNumber, () -> new PhoneNumber(0, Utils.removeSpecialCharacters(number), customer)));
        }

        return customer;
    }

    public List<CustomerVO> convertToCustomerVOs(List<Customer> customers) {
        final List<CustomerVO> listVO = new ArrayList<>();

        for (final Customer customer : customers) {
            listVO.add(this.convertToVO(customer));
        }

        return listVO;
    }

    public List<Customer> convertFromCustomerVOs(List<CustomerVO> customerVOs) {
        final List<Customer> listCustomer = new ArrayList<>();

        for (final CustomerVO customerVO : customerVOs) {
            listCustomer.add(this.convertFromVO(customerVO));
        }

        return listCustomer;
    }
}
