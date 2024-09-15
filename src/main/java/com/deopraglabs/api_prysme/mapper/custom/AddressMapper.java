package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Address;
import com.deopraglabs.api_prysme.data.vo.AddressVO;
import com.deopraglabs.api_prysme.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressMapper {

    public AddressVO convertToVO(Address address) {
        final AddressVO vo = new AddressVO();

        vo.setKey(address.getId());
        vo.setStreet(address.getStreet());
        vo.setNumber(address.getNumber());
        vo.setComplement(address.getComplement());
        vo.setNeighborhood(address.getNeighborhood());
        vo.setCity(address.getCity());
        vo.setState(address.getState());
        vo.setPostalCode(address.getPostalCode());
        vo.setCountry(address.getCountry());

        return vo;
    }

    public Address convertFromVO(AddressVO addressVO) {
        return updateFromVO(new Address(), addressVO);
    }

    public Address updateFromVO(Address address, AddressVO addressVO) {
        address.setStreet(addressVO.getStreet());
        address.setNumber(addressVO.getNumber());
        address.setComplement(Utils.isEmpty(addressVO.getComplement()) ? null : addressVO.getComplement());
        address.setNeighborhood(addressVO.getNeighborhood());
        address.setCity(addressVO.getCity());
        address.setState(addressVO.getState());
        address.setPostalCode(addressVO.getPostalCode());
        address.setCountry(addressVO.getCountry());

        return address;
    }

    public List<AddressVO> convertToAddressVOs(List<Address> addresses) {
        final List<AddressVO> listVO = new ArrayList<>();

        for (final Address address : addresses) {
            listVO.add(this.convertToVO(address));
        }

        return listVO;
    }

    public List<Address> convertFromAddressVOs(List<AddressVO> addressVOs) {
        final List<Address> listAddress = new ArrayList<>();

        for (final AddressVO addressVO : addressVOs) {
            listAddress.add(this.convertFromVO(addressVO));
        }

        return listAddress;
    }
}
