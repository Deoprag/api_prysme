package com.deopraglabs.api_prysme.mapper.impl;

import com.deopraglabs.api_prysme.data.dto.TeamDTO;
import com.deopraglabs.api_prysme.data.dto.TeamRequestDTO;
import com.deopraglabs.api_prysme.data.dto.TeamResponseDTO;
import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.DynamicMapper;
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
    private final DynamicMapper dynamicMapper;

    @Autowired
    public TeamMapperImpl(UserRepository userRepository, DynamicMapper dynamicMapper) {
        this.userRepository = userRepository;
        this.dynamicMapper = dynamicMapper;
    }

    @Override
    public TeamDTO toDTO(Team entity) {
        return dynamicMapper.toDTO(entity, TeamDTO.class);
    }

    @Override
    public Team toEntity(TeamDTO dto) {
        Team entity = dynamicMapper.toEntity(dto, Team.class);
        
        // Resolve entity references from IDs
        if (dto.getLeaderId() != null) {
            userRepository.findById(dto.getLeaderId())
                    .ifPresent(entity::setManager);
        }
        
        return entity;
    }

    @Override
    public List<TeamDTO> toDTOList(List<Team> entities) {
        return dynamicMapper.toDTOList(entities, TeamDTO.class);
    }

    @Override
    public List<Team> toEntityList(List<TeamDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public TeamResponseDTO toResponseDTO(Team entity) {
        return dynamicMapper.toDTO(entity, TeamResponseDTO.class);
    }

    public Team fromRequestDTO(TeamRequestDTO dto) {
        Team entity = dynamicMapper.toEntity(dto, Team.class);
        
        // Resolve entity references from IDs
        if (dto.getManagerId() != null) {
            userRepository.findById(dto.getManagerId())
                    .ifPresent(entity::setManager);
        }
        
        return entity;
    }

    public List<TeamResponseDTO> toResponseDTOList(List<Team> entities) {
        return dynamicMapper.toDTOList(entities, TeamResponseDTO.class);
    }
}
