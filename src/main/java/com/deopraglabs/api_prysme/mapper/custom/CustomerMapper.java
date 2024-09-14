package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Cart;
import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import com.deopraglabs.api_prysme.data.vo.CustomerVO;
import com.deopraglabs.api_prysme.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final CartMapper cartMapper;
    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public CustomerMapper(AddressMapper addressMapper, CartMapper cartMapper, PhoneNumberRepository phoneNumberRepository) {
        this.addressMapper = addressMapper;
        this.cartMapper = cartMapper;
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
        vo.setCart(cartMapper.convertToVO(customer.getCart()));
        for (final PhoneNumber number: customer.getPhoneNumbers()) {
            vo.getPhoneNumbers().add(number.getNumber());
        }

        return vo;
    }

    public Customer convertFromVO(CustomerVO customerVO) { return updateFromVO(new Customer(), customerVO); }

    public Customer updateFromVO(Customer customer, CustomerVO customerVO) {
        customer.setCpfCnpj(customerVO.getCpfCnpj());
        customer.setName(customerVO.getName());
        customer.setTradeName(customerVO.getTradeName());
        customer.setEmail(customerVO.getEmail());
        customer.setBirthFoundationDate(customerVO.getBirthFoundationDate());
        customer.setStateRegistration(customerVO.getStateRegistration());
        customer.setCustomerStatus(customerVO.getCustomerStatus());
        customer.setAddress(addressMapper.convertFromVO(customerVO.getAddress()));
        customer.getAddress().setCustomer(customer);
        for(final String number : customerVO.getPhoneNumbers()) {
            if(customer.getId() > 0) {
                customer.getPhoneNumbers().add(phoneNumberRepository.findByNumber(number));
            } else {
                customer.getPhoneNumbers().add(new PhoneNumber(0, number, customer));
            }
        }
        customer.setCart(new Cart(0, new ArrayList<>(), customer));

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
