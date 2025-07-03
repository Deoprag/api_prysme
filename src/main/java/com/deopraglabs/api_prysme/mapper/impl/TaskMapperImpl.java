package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.TaskDTO;
import com.deopraglabs.api_prysme.data.dto.TaskRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TaskResponseDTO;
import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.mapper.DynamicMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskMapperImpl implements Mapper<Task, TaskDTO> {

    private final UserRepository userRepository;
    private final DynamicMapper dynamicMapper;

    @Autowired
    public TaskMapperImpl(UserRepository userRepository, DynamicMapper dynamicMapper) {
        this.userRepository = userRepository;
        this.dynamicMapper = dynamicMapper;
    }

    @Override
    public TaskDTO toDTO(Task entity) {
        return dynamicMapper.toDTO(entity, TaskDTO.class);
    }
    
    @Override
    public Task toEntity(TaskDTO dto) {
        Task entity = dynamicMapper.toEntity(dto, Task.class);
        
        // Resolve entity references from IDs
        if (dto.getAssignedToId() != null) {
            userRepository.findById(dto.getAssignedToId())
                    .ifPresent(entity::setUser);
        }
        
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(entity::setCreatedBy);
        }
        
        return entity;
    }

    @Override
    public List<TaskDTO> toDTOList(List<Task> entities) {
        return dynamicMapper.toDTOList(entities, TaskDTO.class);
    }

    @Override
    public List<Task> toEntityList(List<TaskDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO toResponseDTO(Task entity) {
        return dynamicMapper.toDTO(entity, TaskResponseDTO.class);
    }

    public Task fromRequestDTO(TaskRequestDTO dto) {
        Task entity = dynamicMapper.toEntity(dto, Task.class);
        
        // Resolve entity references from IDs
        if (dto.getAssignedToId() != null) {
            userRepository.findById(dto.getAssignedToId())
                    .ifPresent(entity::setUser);
        }
        
        return entity;
    }

    public List<TaskResponseDTO> toResponseDTOList(List<Task> entities) {
        return dynamicMapper.toDTOList(entities, TaskResponseDTO.class);
    }
}
