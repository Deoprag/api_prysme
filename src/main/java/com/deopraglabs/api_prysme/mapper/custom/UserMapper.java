package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static UserVO convertToVO(User user) {
        final UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole());
        vo.setBirthDate(user.getBirthDate());
        vo.setGender(user.getGender());
        vo.setPhoneNumber(user.getPhoneNumber());
        vo.setPassword(user.getPassword());
        vo.setActive(user.isActive());
        vo.setTeam(TeamMapper.convertToVO(user.getTeam()));

        return vo;
    }

    public static User convertFromVO(UserVO userVO) {
        final User user = new User();

        return user;
    }
}
