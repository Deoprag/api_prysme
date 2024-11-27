package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Permission;
import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.repository.PermissionRepository;
import com.deopraglabs.api_prysme.repository.TaskRepository;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    private final TeamMapper teamMapper;
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public UserMapper(TeamMapper teamMapper, TaskMapper taskMapper, TaskRepository taskRepository, UserRepository userRepository, TeamRepository teamRepository, PermissionRepository permissionRepository) {
        this.teamMapper = teamMapper;
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.permissionRepository = permissionRepository;
    }

    public UserVO convertToVO(User user) {
        final UserVO vo = new UserVO();

        vo.setKey(user.getId());
        vo.setUsername(user.getUsername());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setEmail(user.getEmail());
        vo.setBirthDate(user.getBirthDate());
        vo.setGender(user.getGender());
        vo.setPhoneNumber(user.getPhoneNumber());
        vo.setEnabled(user.isEnabled());
        vo.setTeamId(user.getTeam().getId());
        vo.setTeam(user.getTeam().getName());
        vo.setPermissions(user.getPermissions().stream().map(Permission::getDescription).collect(Collectors.toList()));
        vo.setCreatedDate(user.getCreatedDate());
        vo.setLastModifiedDate(user.getLastModifiedDate());

        return vo;
    }

    public User convertFromVO(UserVO userVO) {
        return updateFromVO(new User(), userVO);
    }

    public User updateFromVO(User user, UserVO userVO) {
        user.setUsername(userVO.getUsername());
        user.setFirstName(Utils.isEmpty(userVO.getFirstName()) ? null : userVO.getFirstName());
        user.setLastName(Utils.isEmpty(userVO.getLastName()) ? null : userVO.getLastName());
        user.setEmail(Utils.isEmpty(userVO.getEmail()) ? null : userVO.getEmail());
        user.setBirthDate(userVO.getBirthDate());
        user.setGender(Utils.isEmpty(String.valueOf(userVO.getGender())) ? 'U' : userVO.getGender());
        user.setPhoneNumber(Utils.isEmpty(userVO.getPhoneNumber()) ? null : Utils.removeSpecialCharacters(userVO.getPhoneNumber()));
        if (!Utils.isEmpty(userVO.getPassword())) user.setPassword(Utils.encryptPassword(userVO.getPassword()));
        if (Utils.isEmpty(userVO.getPassword()) && userVO.getKey() < 1) user.setPassword(Utils.encryptPassword("a123456*"));
        user.setEnabled(userVO.isEnabled());
        user.setTeam(teamRepository.findById(userVO.getTeamId()).orElseThrow(() -> new CustomRuntimeException.TeamNotFoundException(userVO.getTeamId())));
        user.setPermissions(userVO.getPermissions().stream()
                .map(permissionRepository::findByDescription)
                .collect(Collectors.toList()));

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
