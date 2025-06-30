package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.UserDTO;
import com.deopraglabs.api_prysme.data.dto.UserRequestDTO;
import com.deopraglabs.api_prysme.data.dto.UserResponseDTO;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.DynamicMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.PermissionRepository;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapperImpl implements Mapper<User, UserDTO> {

    private final TeamRepository teamRepository;
    private final PermissionRepository permissionRepository;
    private final DynamicMapper dynamicMapper;

    @Autowired
    public UserMapperImpl(TeamRepository teamRepository, PermissionRepository permissionRepository, DynamicMapper dynamicMapper) {
        this.teamRepository = teamRepository;
        this.permissionRepository = permissionRepository;
        this.dynamicMapper = dynamicMapper;
    }

    @Override
    public UserDTO toDTO(User entity) {
        return dynamicMapper.toDTO(entity, UserDTO.class);
    }

    @Override
    public User toEntity(UserDTO dto) {
        User entity = dynamicMapper.toEntity(dto, User.class);
        
        // Resolve entity references from IDs
        if (dto.getTeamId() != null) {
            teamRepository.findById(dto.getTeamId())
                    .ifPresent(entity::setTeam);
        }
        
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<com.deopraglabs.api_prysme.data.model.Permission> permissions = dto.getPermissionIds().stream()
                    .map(permissionRepository::findById)
                    .filter(opt -> opt.isPresent())
                    .map(opt -> opt.get())
                    .collect(Collectors.toList());
            entity.setPermissions(permissions);
        }
        
        return entity;
    }

    @Override
    public List<UserDTO> toDTOList(List<User> entities) {
        return dynamicMapper.toDTOList(entities, UserDTO.class);
    }

    @Override
    public List<User> toEntityList(List<UserDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public UserResponseDTO toResponseDTO(User entity) {
        return dynamicMapper.toDTO(entity, UserResponseDTO.class);
    }

    public User fromRequestDTO(UserRequestDTO dto) {
        User entity = dynamicMapper.toEntity(dto, User.class);
        
        // Resolve entity references from IDs
        if (dto.getTeamId() != null) {
            teamRepository.findById(dto.getTeamId())
                    .ifPresent(entity::setTeam);
        }
        
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<com.deopraglabs.api_prysme.data.model.Permission> permissions = dto.getPermissionIds().stream()
                    .map(permissionRepository::findById)
                    .filter(opt -> opt.isPresent())
                    .map(opt -> opt.get())
                    .collect(Collectors.toList());
            entity.setPermissions(permissions);
        }
        
        return entity;
    }

    public List<UserResponseDTO> toResponseDTOList(List<User> entities) {
        return dynamicMapper.toDTOList(entities, UserResponseDTO.class);
    }
}
