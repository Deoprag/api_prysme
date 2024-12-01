package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.GoalController;
import com.deopraglabs.api_prysme.data.vo.GoalVO;
import com.deopraglabs.api_prysme.mapper.custom.GoalMapper;
import com.deopraglabs.api_prysme.repository.GoalRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
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
public class GoalService {

    private final Logger logger = Logger.getLogger(GoalService.class.getName());

    private final GoalMapper goalMapper;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository, GoalMapper goalMapper, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
        this.userRepository = userRepository;
    }

    public GoalVO save(GoalVO goalVO) {
        logger.info("Saving goal: " + goalVO);
        final List<String> validations = validateGoalInfo(goalVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (goalVO.getKey() > 0) {
            return goalMapper.convertToVO(goalRepository.save(goalMapper.updateFromVO(
                    goalRepository.findById(goalVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.GoalNotFoundException(goalVO.getKey())),
                    goalVO
            ))).add(linkTo(methodOn(GoalController.class).findById(goalVO.getKey())).withSelfRel());
        } else {
            final var goal = goalRepository.save(goalMapper.convertFromVO(goalVO));
            return goalMapper.convertToVO(goalRepository.save(goal))
                    .add(linkTo(methodOn(GoalController.class).findById(goal.getId())).withSelfRel());
        }
    }

    public List<GoalVO> findAll() {
        logger.info("Finding all goals");
        final var goals = goalMapper.convertToGoalVOs(goalRepository.findAll());
        goals.forEach(goal -> goal.add(linkTo(methodOn(GoalController.class).findById(goal.getKey())).withSelfRel()));

        return goals;
    }

    public GoalVO findById(long id) {
        logger.info("Finding goal by id: " + id);
        return goalMapper.convertToVO(goalRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.GoalNotFoundException(id)))
                .add(linkTo(methodOn(GoalController.class).findById(id)).withSelfRel());
    }

    public GoalVO findCurrentGoalByUsername(String username) {
        logger.info("Finding goal by username: " + username);
        final var user = userRepository.findByUsername(username);
        var latestGoal = goalRepository.findTopBySellerIdOrderByCreatedDateDesc(user.getId());
        if (latestGoal != null) {
            return goalMapper.convertToVO(latestGoal).add(linkTo(methodOn(GoalController.class).findById(user.getId())).withSelfRel());
        } else {
            throw new CustomRuntimeException.GoalNotFoundException(user.getId());
        }
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting goal: " + id);
        return goalRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateGoalInfo(GoalVO goalVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(goalVO, validations);
        validateUniqueFields(goalVO, validations);

        return validations;
    }

    private void validateBasicFields(GoalVO goalVO, List<String> validations) {
        // Adicione validações específicas para os campos básicos, se necessário
    }

    private void validateUniqueFields(GoalVO goalVO, List<String> validations) {
        // Adicione validações para campos exclusivos, como checar se já existe uma meta com o mesmo nome ou identificador
    }
}