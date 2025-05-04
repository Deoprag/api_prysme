package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.TaskDTO;
import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.mapper.DozerMapper;
import com.deopraglabs.api_prysme.mapper.Mapper;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskMapperImpl implements Mapper<Task, TaskDTO> {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public TaskMapperImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public TaskDTO toDTO(Task entity) {
        final TaskDTO dto = DozerMapper.parseObject(entity, TaskDTO.class);
        
        // Set assigned user ID
        if (entity.getAssignedTo() != null) {
            dto.setAssignedToId(entity.getAssignedTo().getId());
        }
        
        // Set created by user ID
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
        }
        
        // Set customer ID
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }
        
        return dto;
    }
    
    @Override
    public Task toEntity(TaskDTO dto) {
        final Task entity = DozerMapper.parseObject(dto, Task.class);
        
        // Set assigned user
        if (dto.getAssignedToId() != null) {
            userRepository.findById(dto.getAssignedToId())
                    .ifPresent(entity::setAssignedTo);
        }
        
        // Set created by user
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(entity::setCreatedBy);
        }
        
        // Set customer
        if (dto.getCustomerId() != null) {
            customerRepository.findById(dto.getCustomerId())
                    .ifPresent(entity::setCustomer);
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
}
