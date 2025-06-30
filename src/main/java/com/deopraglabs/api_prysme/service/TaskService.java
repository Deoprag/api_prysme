package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.dto.TaskRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TaskResponseDTO;
import com.deopraglabs.api_prysme.mapper.impl.TaskMapperImpl;
import com.deopraglabs.api_prysme.repository.TaskRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Transactional
public class TaskService {

    private final Logger logger = Logger.getLogger(TaskService.class.getName());

    private final TaskMapperImpl taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapperImpl taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    public TaskResponseDTO save(TaskRequestDTO taskRequestDTO) {
        logger.info("Saving task: " + taskRequestDTO);
        final var entity = taskMapper.fromRequestDTO(taskRequestDTO);
        final var savedEntity = taskRepository.save(entity);
        return taskMapper.toResponseDTO(savedEntity);
    }

    public TaskResponseDTO update(UUID id, TaskRequestDTO taskRequestDTO) {
        logger.info("Updating task with id: " + id);
        final var existingEntity = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        
        final var updatedEntity = taskMapper.fromRequestDTO(taskRequestDTO);
        updatedEntity.setId(id);
        updatedEntity.setCreatedDate(existingEntity.getCreatedDate());
        updatedEntity.setCreatedBy(existingEntity.getCreatedBy());
        
        final var savedEntity = taskRepository.save(updatedEntity);
        return taskMapper.toResponseDTO(savedEntity);
    }

    public List<TaskResponseDTO> findAll() {
        logger.info("Finding all tasks");
        return taskMapper.toResponseDTOList(taskRepository.findAll());
    }

    public TaskResponseDTO findById(UUID id) {
        logger.info("Finding task by id: " + id);
        final var entity = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return taskMapper.toResponseDTO(entity);
    }

    public List<TaskResponseDTO> findAllByUserId(UUID userId) {
        logger.info("Finding all tasks by user id: " + userId);
        return taskMapper.toResponseDTOList(taskRepository.findAllByUserId(userId));
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting task: " + id);
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
