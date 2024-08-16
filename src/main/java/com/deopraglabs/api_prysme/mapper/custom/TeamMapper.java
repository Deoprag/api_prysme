package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TeamVO;
import org.springframework.stereotype.Service;

@Service
public class TeamMapper {

    public static TeamVO convertToVO(Team team) {
        final TeamVO teamVO = new TeamVO();

        teamVO.setId(team.getId());
        teamVO.setName(team.getName());
        teamVO.setManagerId(team.getManager().getId());
        teamVO.setManager(team.getManager().getFullName());
        for(final User seller : team.getSellers()) {
            teamVO.getSellersIds().add(seller.getId());
            teamVO.getSellers().add(seller.getFullName());
        }

        return teamVO;
    }
    public static Team convertFromVO(TeamVO teamVO) {
        final Team team = new Team();

        team.setId(teamVO.getId());
        team.setName(teamVO.getName());
        // Precisa dos repositories pra finalizar

        return team;
    }

}
