package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.GoalDTO;
import com.deopraglabs.api_prysme.data.model.Goal;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalMapperImpl implements Mapper<Goal, GoalDTO> {

    private final UserRepository userRepository;

    @Autowired
    public GoalMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GoalDTO toDTO(Goal entity) {
        final GoalDTO dto = DozerMapper.parseObject(entity, GoalDTO.class);
        
        // Map additional fields
        dto.setTitle(entity.getGoal());
        
        // Set user ID
        if (entity.getSeller() != null) {
            dto.setUserId(entity.getSeller().getId());
        }
        
        return dto;
    }
    
    @Override
    public Goal toEntity(GoalDTO dto) {
        final Goal entity = DozerMapper.parseObject(dto, Goal.class);
        
        // Map additional fields
        entity.setGoal(dto.getTitle());
        
        // Set user
        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId())
                    .ifPresent(entity::setSeller);
        }
        
        return entity;
    }

    @Override
    public List<GoalDTO> toDTOList(List<Goal> entities) {
        return DozerMapper.parseListObjects(entities, GoalDTO.class);
    }

    @Override
    public List<Goal> toEntityList(List<GoalDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
