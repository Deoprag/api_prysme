package com.deopraglabs.api_prysme.service;


import com.deopraglabs.api_prysme.data.dto.TeamRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TeamResponseDTO;
import com.deopraglabs.api_prysme.mapper.impl.TeamMapperImpl;
import com.deopraglabs.api_prysme.repository.TeamRepository;
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
public class TeamService {

    private final Logger logger = Logger.getLogger(TeamService.class.getName());

    private final TeamMapperImpl teamMapper;
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMapperImpl teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public TeamResponseDTO save(TeamRequestDTO teamRequestDTO) {
        logger.info("Saving team: " + teamRequestDTO);
        final List<String> validations = validateTeamInfo(teamRequestDTO, null);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        final var entity = teamMapper.fromRequestDTO(teamRequestDTO);
        final var savedEntity = teamRepository.save(entity);
        return teamMapper.toResponseDTO(savedEntity);
    }

    public TeamResponseDTO update(UUID id, TeamRequestDTO teamRequestDTO) {
        logger.info("Updating team with id: " + id);
        final var existingEntity = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));
                
        final List<String> validations = validateTeamInfo(teamRequestDTO, id);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }
        
        final var updatedEntity = teamMapper.fromRequestDTO(teamRequestDTO);
        updatedEntity.setId(id);
        
        final var savedEntity = teamRepository.save(updatedEntity);
        return teamMapper.toResponseDTO(savedEntity);
    }

    public List<TeamResponseDTO> findAll() {
        logger.info("Finding all teams");
        return teamMapper.toResponseDTOList(teamRepository.findAll());
    }

    public TeamResponseDTO findById(UUID id) {
        logger.info("Finding team by id: " + id);
        final var entity = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));
        return teamMapper.toResponseDTO(entity);
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting team: " + id);
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Business Rules - Restored and adapted for DTOs
    private List<String> validateTeamInfo(TeamRequestDTO teamRequestDTO, UUID existingTeamId) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(teamRequestDTO, validations);
        validateUniqueFields(teamRequestDTO, validations, existingTeamId);

        return validations;
    }

    private void validateBasicFields(TeamRequestDTO teamRequestDTO, List<String> validations) {
        Utils.checkField(validations, Utils.isEmpty(teamRequestDTO.getName()), "Team name is required");
        Utils.checkField(validations, teamRequestDTO.getManagerId() == null, "Team manager is required");
    }

    private void validateUniqueFields(TeamRequestDTO teamRequestDTO, List<String> validations, UUID existingTeamId) {
        // Check if team name is unique
        if (!Utils.isEmpty(teamRequestDTO.getName())) {
            boolean nameExists = teamRepository.findAll().stream()
                    .anyMatch(team -> team.getName().equalsIgnoreCase(teamRequestDTO.getName()) 
                            && !team.getId().equals(existingTeamId));
            if (nameExists) {
                validations.add("Team name already exists");
            }
        }
        
        // Check if manager is already managing another team
        if (teamRequestDTO.getManagerId() != null) {
            boolean managerAlreadyAssigned = teamRepository.findAll().stream()
                    .anyMatch(team -> team.getManager() != null 
                            && team.getManager().getId().equals(teamRequestDTO.getManagerId())
                            && !team.getId().equals(existingTeamId));
            if (managerAlreadyAssigned) {
                validations.add("This user is already managing another team");
            }
        }
    }
}
