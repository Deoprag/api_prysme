package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.CustomerDTO;
import com.deopraglabs.api_prysme.data.enums.CustomerStatus;
import com.deopraglabs.api_prysme.data.model.Address;
import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.AddressRepository;
import com.deopraglabs.api_prysme.repository.PhoneNumberRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerMapperImpl implements Mapper<Customer, CustomerDTO> {

    private final AddressRepository addressRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerMapperImpl(AddressRepository addressRepository, PhoneNumberRepository phoneNumberRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CustomerDTO toDTO(Customer entity) {
        final CustomerDTO dto = DozerMapper.parseObject(entity, CustomerDTO.class);
        
        if (entity.getAddress() != null) {
            final List<Long> addressIds = new ArrayList<>();
            addressIds.add(entity.getAddress().getId());
            dto.setAddressIds(addressIds);
        }
        
        dto.setDocument(entity.getCpfCnpj());
        dto.setBirthDate(entity.getBirthFoundationDate());
        dto.setActive(entity.getCustomerStatus() != CustomerStatus.DELETED);

        if (entity.getPhoneNumbers() != null && !entity.getPhoneNumbers().isEmpty()) {
            final List<Long> phoneNumberIds = entity.getPhoneNumbers().stream()
                .map(PhoneNumber::getId)
                .collect(Collectors.toList());
            dto.setContactIds(phoneNumberIds);
        }
        
        return dto;
    }
    
    @Override
    public Customer toEntity(CustomerDTO dto) {
        final Customer entity = DozerMapper.parseObject(dto, Customer.class);
        
        // Map seller
        if (dto.getId() != null) {
            final Customer existingCustomer = userRepository.findById(dto.getId())
                    .map(user -> entity)
                    .orElse(entity);
            
            // Set address
            if (dto.getAddressIds() != null && !dto.getAddressIds().isEmpty()) {
                final Address address = addressRepository.findById(dto.getAddressIds().get(0))
                        .orElse(new Address());
                address.setCustomer(existingCustomer);
                existingCustomer.setAddress(address);
            }
            
            // Set other fields that don't map directly
            existingCustomer.setCpfCnpj(dto.getDocument());
            existingCustomer.setBirthFoundationDate(dto.getBirthDate());
            
            return existingCustomer;
        }
        
        return entity;
    }

    @Override
    public List<CustomerDTO> toDTOList(final List<Customer> entities) {
        return DozerMapper.parseListObjects(entities, CustomerDTO.class);
    }
    
    @Override
    public List<Customer> toEntityList(final List<CustomerDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}