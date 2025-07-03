package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.GoalDTO;
import com.deopraglabs.api_prysme.data.dto.GoalRequestDTO;
import com.deopraglabs.api_prysme.data.dto.GoalResponseDTO;
import com.deopraglabs.api_prysme.data.model.Goal;
import com.deopraglabs.api_prysme.mapper.DynamicMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalMapperImpl implements Mapper<Goal, GoalDTO> {

    private final UserRepository userRepository;
    private final DynamicMapper dynamicMapper;

    @Autowired
    public GoalMapperImpl(UserRepository userRepository, DynamicMapper dynamicMapper) {
        this.userRepository = userRepository;
        this.dynamicMapper = dynamicMapper;
    }

    @Override
    public GoalDTO toDTO(Goal entity) {
        return dynamicMapper.toDTO(entity, GoalDTO.class);
    }

    @Override
    public Goal toEntity(GoalDTO dto) {
        Goal entity = dynamicMapper.toEntity(dto, Goal.class);
        
        // Resolve entity references from IDs
        if (dto.getSellerId() != null) {
            userRepository.findById(dto.getSellerId())
                    .ifPresent(entity::setSeller);
        }
        
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(entity::setCreatedBy);
        }
        
        if (dto.getLastModifiedById() != null) {
            userRepository.findById(dto.getLastModifiedById())
                    .ifPresent(entity::setLastModifiedBy);
        }
        
        return entity;
    }

    @Override
    public List<GoalDTO> toDTOList(List<Goal> entities) {
        return dynamicMapper.toDTOList(entities, GoalDTO.class);
    }

    @Override
    public List<Goal> toEntityList(List<GoalDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public GoalResponseDTO toResponseDTO(Goal entity) {
        return dynamicMapper.toDTO(entity, GoalResponseDTO.class);
    }

    public Goal fromRequestDTO(GoalRequestDTO dto) {
        Goal entity = dynamicMapper.toEntity(dto, Goal.class);
        
        // Resolve entity references from IDs
        if (dto.getSellerId() != null) {
            userRepository.findById(dto.getSellerId())
                    .ifPresent(entity::setSeller);
        }
        
        return entity;
    }

    public List<GoalResponseDTO> toResponseDTOList(List<Goal> entities) {
        return dynamicMapper.toDTOList(entities, GoalResponseDTO.class);
    }
}
