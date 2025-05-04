package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.TeamDTO;
import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMapperImpl implements Mapper<Team, TeamDTO> {

    private final UserRepository userRepository;

    @Autowired
    public TeamMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public TeamDTO toDTO(Team entity) {
        final TeamDTO dto = DozerMapper.parseObject(entity, TeamDTO.class);
        
        // Set leader/manager ID
        if (entity.getManager() != null) {
            dto.setLeaderId(entity.getManager().getId());
        }
        
        // Set member IDs from sellers
        if (entity.getSellers() != null && !entity.getSellers().isEmpty()) {
            dto.setMemberIds(entity.getSellers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        } else {
            dto.setMemberIds(new ArrayList<>());
        }
        
        return dto;
    }
    
    @Override
    public Team toEntity(TeamDTO dto) {
        final Team entity = DozerMapper.parseObject(dto, Team.class);
        
        // Set manager/leader
        if (dto.getLeaderId() != null) {
            userRepository.findById(dto.getLeaderId())
                    .ifPresent(entity::setManager);
        }
        
        // Set sellers/members
        if (dto.getMemberIds() != null && !dto.getMemberIds().isEmpty()) {
            final List<User> members = dto.getMemberIds().stream()
                    .map(id -> userRepository.findById(id).orElse(null))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
            
            entity.setSellers(members);
        }
        
        return entity;
    }

    @Override
    public List<TeamDTO> toDTOList(List<Team> entities) {
        return DozerMapper.parseListObjects(entities, TeamDTO.class);
    }

    @Override
    public List<Team> toEntityList(List<TeamDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
