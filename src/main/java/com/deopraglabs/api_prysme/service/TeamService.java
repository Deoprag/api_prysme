package com.deopraglabs.api_prysme.service;


import com.deopraglabs.api_prysme.controller.TeamController;
import com.deopraglabs.api_prysme.data.vo.TeamVO;
import com.deopraglabs.api_prysme.mapper.custom.TeamMapper;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class TeamService {

    private final Logger logger = Logger.getLogger(TeamService.class.getName());

    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public TeamVO save(TeamVO teamVO) {
        logger.info("Saving team: " + teamVO);
        final List<String> validations = validateTeamInfo(teamVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (teamVO.getKey() > 0) {
            return teamMapper.convertToVO(teamRepository.save(teamMapper.updateFromVO(
                    teamRepository.findById(teamVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.TeamNotFoundException(teamVO.getKey())),
                    teamVO
            ))).add(linkTo(methodOn(TeamController.class).findById(teamVO.getKey())).withSelfRel());
        } else {
            final var team = teamRepository.save(teamMapper.convertFromVO(teamVO));
            return teamMapper.convertToVO(teamRepository.save(team))
                    .add(linkTo(methodOn(TeamController.class).findById(team.getId())).withSelfRel());
        }
    }

    public List<TeamVO> findAll() {
        logger.info("Finding all teams");
        final var teams = teamMapper.convertToTeamVOs(teamRepository.findAll());
        teams.forEach(team -> team.add(linkTo(methodOn(TeamController.class).findById(team.getKey())).withSelfRel()));

        return teams;
    }

    public TeamVO findById(long id) {
        logger.info("Finding team by id: " + id);
        return teamMapper.convertToVO(teamRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.TeamNotFoundException(id)))
                .add(linkTo(methodOn(TeamController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting team: " + id);
        return teamRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateTeamInfo(TeamVO teamVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(teamVO, validations);
        validateUniqueFields(teamVO, validations);

        return validations;
    }

    private void validateBasicFields(TeamVO teamVO, List<String> validations) {

    }

    private void validateUniqueFields(TeamVO teamVO, List<String> validations) {

    }
}
