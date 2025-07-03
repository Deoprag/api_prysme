package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.dto.GoalRequestDTO;
import com.deopraglabs.api_prysme.data.dto.GoalResponseDTO;
import com.deopraglabs.api_prysme.mapper.impl.GoalMapperImpl;
import com.deopraglabs.api_prysme.repository.GoalRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
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
public class GoalService {

    private final Logger logger = Logger.getLogger(GoalService.class.getName());

    private final GoalMapperImpl goalMapper;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository, GoalMapperImpl goalMapper, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
        this.userRepository = userRepository;
    }

    public GoalResponseDTO save(GoalRequestDTO goalRequestDTO) {
        logger.info("Saving goal: " + goalRequestDTO);
        final List<String> validations = validateGoalInfo(goalRequestDTO, null);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        final var entity = goalMapper.fromRequestDTO(goalRequestDTO);
        final var savedEntity = goalRepository.save(entity);
        return goalMapper.toResponseDTO(savedEntity);
    }

    public GoalResponseDTO update(UUID id, GoalRequestDTO goalRequestDTO) {
        logger.info("Updating goal with id: " + id);
        final var existingEntity = goalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found"));
                
        final List<String> validations = validateGoalInfo(goalRequestDTO, id);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }
        
        final var updatedEntity = goalMapper.fromRequestDTO(goalRequestDTO);
        updatedEntity.setId(id);
        updatedEntity.setCreatedDate(existingEntity.getCreatedDate());
        updatedEntity.setCreatedBy(existingEntity.getCreatedBy());
        
        final var savedEntity = goalRepository.save(updatedEntity);
        return goalMapper.toResponseDTO(savedEntity);
    }

    public List<GoalResponseDTO> findAll() {
        logger.info("Finding all goals");
        return goalMapper.toResponseDTOList(goalRepository.findAll());
    }

    public GoalResponseDTO findById(UUID id) {
        logger.info("Finding goal by id: " + id);
        final var entity = goalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found"));
        return goalMapper.toResponseDTO(entity);
    }

    public GoalResponseDTO findCurrentGoalByUsername(String username) {
        logger.info("Finding goal by username: " + username);
        final var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        
        var latestGoal = goalRepository.findTopBySellerIdOrderByCreatedDateDesc(user.getId());
        if (latestGoal != null) {
            return goalMapper.toResponseDTO(latestGoal);
        } else {
            throw new NoSuchElementException("No goal found for user");
        }
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting goal: " + id);
        if (goalRepository.existsById(id)) {
            goalRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Business Rules - Restored and adapted for DTOs
    private List<String> validateGoalInfo(GoalRequestDTO goalRequestDTO, UUID existingGoalId) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(goalRequestDTO, validations);
        validateUniqueFields(goalRequestDTO, validations, existingGoalId);

        return validations;
    }

    private void validateBasicFields(GoalRequestDTO goalRequestDTO, List<String> validations) {
        Utils.checkField(validations, goalRequestDTO.getGoal() == null, "Goal amount is required");
        Utils.checkField(validations, goalRequestDTO.getGoal() != null && goalRequestDTO.getGoal().signum() <= 0, "Goal amount must be greater than zero");
        Utils.checkField(validations, goalRequestDTO.getSellerId() == null, "Seller is required");
        Utils.checkField(validations, goalRequestDTO.getStartDate() == null, "Start date is required");
        Utils.checkField(validations, goalRequestDTO.getEndDate() == null, "End date is required");
        
        // Validate date range
        if (goalRequestDTO.getStartDate() != null && goalRequestDTO.getEndDate() != null) {
            Utils.checkField(validations, !goalRequestDTO.getEndDate().after(goalRequestDTO.getStartDate()), "End date must be after start date");
        }
    }

    private void validateUniqueFields(GoalRequestDTO goalRequestDTO, List<String> validations, UUID existingGoalId) {
        // Check if there's already an active goal for the same seller in the same period
        if (goalRequestDTO.getSellerId() != null && goalRequestDTO.getStartDate() != null && goalRequestDTO.getEndDate() != null) {
            boolean hasOverlappingGoal = goalRepository.findAll().stream()
                    .anyMatch(goal -> !goal.getId().equals(existingGoalId)
                            && goal.getSeller() != null 
                            && goal.getSeller().getId().equals(goalRequestDTO.getSellerId())
                            && isDateRangeOverlapping(goal.getStartDate(), goal.getEndDate(), 
                                                    goalRequestDTO.getStartDate(), goalRequestDTO.getEndDate()));
            
            if (hasOverlappingGoal) {
                validations.add("There is already a goal for this seller in the specified period");
            }
        }
    }
    
    private boolean isDateRangeOverlapping(java.util.Date existingStart, java.util.Date existingEnd, 
                                         java.util.Date newStart, java.util.Date newEnd) {
        return (newStart.before(existingEnd) || newStart.equals(existingEnd)) 
                && (newEnd.after(existingStart) || newEnd.equals(existingStart));
    }
}