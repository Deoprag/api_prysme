package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Goal;
import com.deopraglabs.api_prysme.data.vo.GoalVO;
import com.deopraglabs.api_prysme.repository.GoalRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoalMapper {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    @Autowired
    public GoalMapper(UserRepository userRepository, GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
    }

    public GoalVO convertToVO(Goal goal) {
        final GoalVO vo = new GoalVO();

        vo.setKey(goal.getId());
        vo.setGoal(goal.getGoal());
        vo.setCurrentProgress(goalRepository.getCurrentProgress(goal.getSeller(), goal.getId()));
        vo.setSeller(goal.getSeller().getUsername());
        vo.setSellerId(goal.getSeller().getId());
        vo.setStartDate(goal.getStartDate());
        vo.setEndDate(goal.getEndDate());
        vo.setCreatedDate(goal.getCreatedDate());
        vo.setLastModifiedDate(goal.getLastModifiedDate());
        vo.setCreatedBy(goal.getCreatedBy() != null ? goal.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(goal.getLastModifiedBy() != null ? goal.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Goal convertFromVO(GoalVO vo) {
        return updateFromVO(new Goal(), vo);
    }

    public Goal updateFromVO(Goal goal, GoalVO goalVO) {
        goal.setGoal(goalVO.getGoal());
        goal.setSeller(userRepository.findById(goalVO.getSellerId()).orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(goalVO.getSellerId())));
        goal.setStartDate(goalVO.getStartDate());
        goal.setEndDate(goalVO.getEndDate());
        goal.setCreatedDate(goalVO.getCreatedDate());
        goal.setLastModifiedDate(goalVO.getLastModifiedDate());

        return goal;
    }

    public List<GoalVO> convertToGoalVOs(List<Goal> goals) {
        final List<GoalVO> vos = new ArrayList<>();
        for (final Goal goal : goals) {
            vos.add(convertToVO(goal));
        }
        return vos;
    }

    public List<Goal> convertFromGoalVOs(List<GoalVO> vos) {
        final List<Goal> goals = new ArrayList<>();
        for (final GoalVO vo : vos) {
            goals.add(convertFromVO(vo));
        }
        return goals;
    }
}
