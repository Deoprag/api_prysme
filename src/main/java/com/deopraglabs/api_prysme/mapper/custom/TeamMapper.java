package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TeamVO;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return updateFromVO(new Team(), teamVO);
    }

    public Team updateFromVO(Team team, TeamVO teamVO) {

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

    public List<TeamVO> convertToTeamVOs(List<Team> teams) {
        final List<TeamVO> listVO = new ArrayList<>();

        for (final Team team : teams) {
            listVO.add(this.convertToVO(team));
        }

        return listVO;
    }

    public List<Team> convertFromTeamVOs(List<TeamVO> teamVOs) {
        final List<Team> listTeam = new ArrayList<>();

        for (final TeamVO teamVO : teamVOs) {
            listTeam.add(this.convertFromVO(teamVO));
        }

        return listTeam;
    }
}
