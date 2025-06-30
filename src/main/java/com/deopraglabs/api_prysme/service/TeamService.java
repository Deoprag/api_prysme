package com.deopraglabs.api_prysme.service;


import com.deopraglabs.api_prysme.data.dto.TeamRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TeamResponseDTO;
import com.deopraglabs.api_prysme.mapper.impl.TeamMapperImpl;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        final var entity = teamMapper.fromRequestDTO(teamRequestDTO);
        final var savedEntity = teamRepository.save(entity);
        return teamMapper.toResponseDTO(savedEntity);
    }

    public TeamResponseDTO update(UUID id, TeamRequestDTO teamRequestDTO) {
        logger.info("Updating team with id: " + id);
        final var existingEntity = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team not found"));
        
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
}
