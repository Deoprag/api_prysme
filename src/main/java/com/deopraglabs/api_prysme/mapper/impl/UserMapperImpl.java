package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.UserDTO;
import com.deopraglabs.api_prysme.data.model.Permission;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
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

    @Autowired
    public UserMapperImpl(TeamRepository teamRepository, PermissionRepository permissionRepository) {
        this.teamRepository = teamRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        final UserDTO dto = DozerMapper.parseObject(entity, UserDTO.class);
        
        // Set fullName from firstName and lastName
        dto.setFullName(entity.getFirstName() + " " + entity.getLastName());
        
        // Set active status based on enabled field
        dto.setActive(entity.isEnabled());
        
        // Set team ID
        if (entity.getTeam() != null) {
            dto.setTeamId(entity.getTeam().getId());
        }
        
        // Set permission IDs
        if (entity.getPermissions() != null) {
            dto.setPermissionIds(entity.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    @Override
    public User toEntity(UserDTO dto) {
        final User entity = DozerMapper.parseObject(dto, User.class);
        
        // Set enabled field based on active status
        entity.setEnabled(dto.getActive() != null ? dto.getActive() : true);
        
        // Handle team assignment
        if (dto.getTeamId() != null) {
            teamRepository.findById(dto.getTeamId())
                    .ifPresent(entity::setTeam);
        }
        
        // Handle permissions
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            final List<Permission> permissions = permissionRepository.findAllById(dto.getPermissionIds());
            entity.setPermissions(permissions);
        }
        
        // Parse fullName into firstName and lastName if needed
        if (entity.getFirstName() == null && entity.getLastName() == null && dto.getFullName() != null) {
            final String[] names = dto.getFullName().split(" ");
            if (names.length > 1) {
                entity.setFirstName(names[0]);
                entity.setLastName(names[names.length - 1]);
            } else {
                entity.setFirstName(dto.getFullName());
                entity.setLastName("");
            }
        }
        
        return entity;
    }

    @Override
    public List<UserDTO> toDTOList(final List<User> entities) {
        return DozerMapper.parseListObjects(entities, UserDTO.class);
    }
    
    @Override
    public List<User> toEntityList(final List<UserDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
