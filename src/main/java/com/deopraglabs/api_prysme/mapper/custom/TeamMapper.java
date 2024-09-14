package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TeamVO;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamMapper {

    private final UserRepository userRepository;

    @Autowired
    public TeamMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TeamVO convertToVO(Team team) {
        final TeamVO teamVO = new TeamVO();

        teamVO.setKey(team.getId());
        teamVO.setName(team.getName());
        teamVO.setManagerId(team.getManager().getId());
        teamVO.setManager(team.getManager().getFullName());
        for (final User seller : team.getSellers()) {
            teamVO.getSellersIds().add(seller.getId());
            teamVO.getSellers().add(seller.getFullName());
        }

        return teamVO;
    }

    public Team convertFromVO(TeamVO teamVO) {
        final Team team = new Team();

        team.setId(teamVO.getKey());
        team.setName(teamVO.getName());
        team.setManager(userRepository.findById(teamVO.getManagerId())
                .orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(teamVO.getManagerId())));
        for (final long id : teamVO.getSellersIds()) {
            team.getSellers().add(userRepository.findById(id)
                    .orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(id)));
        }

        return team;
    }
}
