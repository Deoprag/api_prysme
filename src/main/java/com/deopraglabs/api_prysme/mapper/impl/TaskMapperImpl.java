package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.TaskDTO;
import com.deopraglabs.api_prysme.data.dto.TaskRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TaskResponseDTO;
import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskMapperImpl implements Mapper<Task, TaskDTO> {

    private final UserRepository userRepository;

    @Autowired
    public TaskMapperImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO toDTO(Task entity) {
        final TaskDTO dto = DozerMapper.parseObject(entity, TaskDTO.class);
        
        // Set assigned user ID
        if (entity.getUser() != null) {
            dto.setAssignedToId(entity.getUser().getId());
        }
        
        // Set created by user ID
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
        }
        
        return dto;
    }
    
    @Override
    public Task toEntity(TaskDTO dto) {
        final Task entity = DozerMapper.parseObject(dto, Task.class);
        
        // Set assigned user
        if (dto.getAssignedToId() != null) {
            userRepository.findById(dto.getAssignedToId())
                    .ifPresent(entity::setUser);
        }
        
        // Set created by user
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(entity::setCreatedBy);
        }
        
        return entity;
    }

    @Override
    public List<TaskDTO> toDTOList(List<Task> entities) {
        return DozerMapper.parseListObjects(entities, TaskDTO.class);
    }

    @Override
    public List<Task> toEntityList(List<TaskDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO toResponseDTO(Task entity) {
        final TaskResponseDTO dto = DozerMapper.parseObject(entity, TaskResponseDTO.class);
        
        // Set assigned user ID
        if (entity.getUser() != null) {
            dto.setAssignedToId(entity.getUser().getId());
        }
        
        // Set created by user ID
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
        }
        
        // Set last modified by user ID
        if (entity.getLastModifiedBy() != null) {
            dto.setLastModifiedById(entity.getLastModifiedBy().getId());
        }
        
        return dto;
    }

    public Task fromRequestDTO(TaskRequestDTO dto) {
        final Task entity = DozerMapper.parseObject(dto, Task.class);
        
        // Set assigned user
        if (dto.getAssignedToId() != null) {
            userRepository.findById(dto.getAssignedToId())
                    .ifPresent(entity::setUser);
        }
        
        return entity;
    }

    public List<TaskResponseDTO> toResponseDTOList(List<Task> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
