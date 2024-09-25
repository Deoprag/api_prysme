package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Address;
import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import com.deopraglabs.api_prysme.data.vo.CustomerVO;
import com.deopraglabs.api_prysme.repository.AddressRepository;
import com.deopraglabs.api_prysme.repository.PhoneNumberRepository;
import com.deopraglabs.api_prysme.utils.Utils;
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

    @Autowired
    public CustomerMapper(AddressMapper addressMapper, AddressRepository addressRepository, PhoneNumberRepository phoneNumberRepository) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
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

        return vo;
    }

    public Customer convertFromVO(CustomerVO customerVO) {
        return updateFromVO(new Customer(), customerVO);
    }

    public Customer updateFromVO(Customer customer, CustomerVO customerVO) {
        customer.setCpfCnpj(Utils.isEmpty(customerVO.getCpfCnpj()) ? null : customerVO.getCpfCnpj());
        customer.setName(Utils.isEmpty(customerVO.getName()) ? null : customerVO.getName());
        customer.setTradeName(Utils.isEmpty(customerVO.getTradeName()) ? null : customerVO.getTradeName());
        customer.setEmail(Utils.isEmpty(customerVO.getEmail()) ? null : customerVO.getEmail());
        customer.setBirthFoundationDate(customerVO.getBirthFoundationDate());
        customer.setStateRegistration(Utils.isEmpty(customerVO.getStateRegistration()) ? null : customerVO.getStateRegistration());
        customer.setCustomerStatus(customerVO.getCustomerStatus());
        customer.setAddress(addressMapper.updateFromVO(
                Objects.requireNonNullElseGet(
                        addressRepository.findById(customerVO.getAddress().getKey()).orElseThrow(),
                        () -> Address.builder().customer(customer).build()), customerVO.getAddress())
        );
        customer.getAddress().setCustomer(customer);
        for (final String number : customerVO.getPhoneNumbers()) {
            final var phoneNumber = phoneNumberRepository.findByNumber(number);
            customer.getPhoneNumbers().add(Objects.requireNonNullElseGet(phoneNumber, () -> new PhoneNumber(0, number, customer)));
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
