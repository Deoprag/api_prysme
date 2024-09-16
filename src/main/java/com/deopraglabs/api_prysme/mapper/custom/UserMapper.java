package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Role;
import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.repository.TaskRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserMapper {

    private final TeamMapper teamMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public UserMapper(TeamMapper teamMapper, TaskMapper taskMapper, TaskRepository taskRepository) {
        this.teamMapper = teamMapper;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
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
        if (user.getTeam() != null) vo.setTeam(teamMapper.convertToVO(user.getTeam()));
        vo.setTasks(taskMapper.convertToTaskVOs(user.getTasks()));

        return vo;
    }

    public User convertFromVO(UserVO userVO) {
        return updateFromVO(new User(), userVO);
    }

    public User updateFromVO(User user, UserVO userVO) {
        user.setFirstName(Utils.isEmpty(userVO.getFirstName()) ? null : userVO.getFirstName());
        user.setLastName(Utils.isEmpty(userVO.getLastName()) ? null : userVO.getLastName());
        user.setEmail(Utils.isEmpty(userVO.getEmail()) ? null : userVO.getEmail());
        user.setRole(Role.valueOf(userVO.getRole()));
        user.setBirthDate(userVO.getBirthDate());
        user.setGender(Utils.isEmpty(String.valueOf(userVO.getGender())) ? 'U' : userVO.getGender());
        user.setPhoneNumber(Utils.isEmpty(userVO.getPhoneNumber()) ? null : userVO.getPhoneNumber());
        user.setPassword(Utils.isEmpty(userVO.getPassword()) ? null : userVO.getPassword());
        user.setActive(userVO.isActive());
        if (userVO.getTeam() != null) user.setTeam(teamMapper.convertFromVO(userVO.getTeam()));
        if (userVO.getTasks() != null) for (final TaskVO taskVO : userVO.getTasks()) {
            final var task = taskRepository.findById(taskVO.getKey());
            user.getTasks().add(task.orElse(new Task(0, taskVO.getTitle(), taskVO.getDescription(), taskVO.getCompletedDateTime(), user)));
        }

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
