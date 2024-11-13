package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.repository.TaskRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    private final TeamMapper teamMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserMapper(TeamMapper teamMapper, TaskMapper taskMapper, TaskRepository taskRepository, UserRepository userRepository) {
        this.teamMapper = teamMapper;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public UserVO convertToVO(User user) {
        final UserVO vo = new UserVO();

        vo.setKey(user.getId());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setEmail(user.getEmail());
        vo.setBirthDate(user.getBirthDate());
        vo.setGender(user.getGender());
        vo.setPhoneNumber(user.getPhoneNumber());
        vo.setPassword(user.getPassword());
        vo.setEnabled(user.isEnabled());
        if (user.getTeam() != null) vo.setTeam(teamMapper.convertToVO(user.getTeam()));
        vo.setTasks(taskMapper.convertToTaskVOs(user.getTasks()));
        vo.setCreatedDate(user.getCreatedDate());
        vo.setLastModifiedDate(user.getLastModifiedDate());
//        vo.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getUsername() : "");
//        vo.setLastModifiedBy(user.getLastModifiedBy() != null ? user.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public User convertFromVO(UserVO userVO) {
        return updateFromVO(new User(), userVO);
    }

    public User updateFromVO(User user, UserVO userVO) {
        user.setFirstName(Utils.isEmpty(userVO.getFirstName()) ? null : userVO.getFirstName());
        user.setLastName(Utils.isEmpty(userVO.getLastName()) ? null : userVO.getLastName());
        user.setEmail(Utils.isEmpty(userVO.getEmail()) ? null : userVO.getEmail());
        user.setBirthDate(userVO.getBirthDate());
        user.setGender(Utils.isEmpty(String.valueOf(userVO.getGender())) ? 'U' : userVO.getGender());
        user.setPhoneNumber(Utils.isEmpty(userVO.getPhoneNumber()) ? null : userVO.getPhoneNumber());
        user.setPassword(Utils.isEmpty(userVO.getPassword()) ? null : userVO.getPassword());
        user.setEnabled(userVO.isEnabled());
        if (userVO.getTeam() != null) user.setTeam(teamMapper.convertFromVO(userVO.getTeam()));
        if (userVO.getTasks() != null) for (final TaskVO taskVO : userVO.getTasks()) {
            final var task = taskRepository.findById(taskVO.getKey());
            user.getTasks().add(task.orElse(new Task(0, taskVO.getTitle(), taskVO.getDescription(), taskVO.getDueDate(), user, null, null, null, null)));
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
