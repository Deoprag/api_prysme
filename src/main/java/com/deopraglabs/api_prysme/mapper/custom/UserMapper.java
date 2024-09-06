package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Role;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    public static UserVO convertToVO(User user) {
        final UserVO vo = new UserVO();

        vo.setId(user.getId());
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
            vo.setTeam(TeamMapper.convertToVO(user.getTeam()));
        }
        vo.setTasks(user.getTasks());

        return vo;
    }

    public static User convertFromVO(UserVO userVO) {
        final User user = new User();

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
            user.setTeam(TeamMapper.convertFromVO(userVO.getTeam()));
        }
        user.setTasks(userVO.getTasks());

        return user;
    }

    public static User updateFromVO(User user, UserVO userVO) {
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
            user.setTeam(TeamMapper.convertFromVO(userVO.getTeam()));
        }
        user.setTasks(userVO.getTasks());

        return user;
    }

    public static List<UserVO> convertToUserVOs(List<User> users) {
        final List<UserVO> listVO = new ArrayList<>();

        for(final User user : users) {
            listVO.add(UserMapper.convertToVO(user));
        }

        return listVO;
    }

    public static List<User> convertFromUserVOs(List<UserVO> userVOs) {
        final List<User> listUser = new ArrayList<>();

        for(final UserVO userVO : userVOs) {
            listUser.add(UserMapper.convertFromVO(userVO));
        }

        return listUser;
    }
}
