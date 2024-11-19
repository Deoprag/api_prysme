package com.deopraglabs.api_prysme.mapper.custom;


import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.Permission;
import com.deopraglabs.api_prysme.data.vo.ItemProductVO;
import com.deopraglabs.api_prysme.data.vo.PermissionVO;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionMapper {

    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final SalesOrderMapper salesOrderMapper;
    private final UserRepository userRepository;

    @Autowired
    public PermissionMapper(UserMapper userMapper, CustomerMapper customerMapper, SalesOrderMapper salesOrderMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
        this.salesOrderMapper = salesOrderMapper;
        this.userRepository = userRepository;
    }

    public PermissionVO convertToVO(Permission permission) {
        final PermissionVO vo = new PermissionVO();

        vo.setKey(permission.getId());
        vo.setDescription(permission.getDescription());

        return vo;
    }

    public Permission convertFromVO(PermissionVO vo) {
        return updateFromVO(new Permission(), vo);
    }

    public Permission updateFromVO(Permission permission, PermissionVO permissionVO) {
        permission.setDescription(permissionVO.getDescription());

        return permission;
    }

    public List<PermissionVO> convertToPermissionVOs(List<Permission> permissions) {
        final List<PermissionVO> vos = new ArrayList<>();
        for (final Permission permission : permissions) {
            vos.add(convertToVO(permission));
        }
        return vos;
    }

    public List<Permission> convertFromPermissionVOs(List<PermissionVO> vos) {
        final List<Permission> permissions = new ArrayList<>();
        for (final PermissionVO vo : vos) {
            permissions.add(convertFromVO(vo));
        }
        return permissions;
    }
}

