package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Role;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    private final TeamMapper teamMapper;

    @Autowired
    public UserMapper(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    public UserVO convertToVO(User user) {
        final UserVO vo = new UserVO();

        vo.setKey(user.getId());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole().toString());
        vo.setBirthDate(user.getBirthDate());
        vo.setGender(user.getGender());
        vo.setPhoneNumber(user.getPhoneNumber());
        vo.setPassword(user.getPassword());
        vo.setActive(user.isActive());
        if (user.getTeam() != null) {
            vo.setTeam(teamMapper.convertToVO(user.getTeam()));
        }
//        vo.setTasks(user.getTasks());

        return vo;
    }

    public User convertFromVO(UserVO userVO) {
        return updateFromVO(new User(), userVO);
    }

    public User updateFromVO(User user, UserVO userVO) {
        user.setFirstName(userVO.getFirstName());
        user.setLastName(userVO.getLastName());
        user.setEmail(userVO.getEmail());
        user.setRole(Role.valueOf(userVO.getRole()));
        user.setBirthDate(userVO.getBirthDate());
        user.setGender(userVO.getGender());
        user.setPhoneNumber(userVO.getPhoneNumber());
        user.setPassword(userVO.getPassword());
        user.setActive(userVO.isActive());
        if (userVO.getTeam() != null) {
            user.setTeam(teamMapper.convertFromVO(userVO.getTeam()));
        }
//        user.setTasks(userVO.getTasks());

        return user;
    }

    public List<UserVO> convertToUserVOs(List<User> users) {
        final List<UserVO> listVO = new ArrayList<>();

        for (final User user : users) {
            listVO.add(this.convertToVO(user));
        }

        return listVO;
    }

    public List<User> convertFromUserVOs(List<UserVO> userVOs) {
        final List<User> listUser = new ArrayList<>();

        for (final UserVO userVO : userVOs) {
            listUser.add(this.convertFromVO(userVO));
        }

        return listUser;
    }
}
