package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskMapper {

    private final UserRepository userRepository;

    @Autowired
    public TaskMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TaskVO convertToVO(Task task) {
        final TaskVO vo = new TaskVO();

        vo.setKey(task.getId());
        vo.setTitle(task.getTitle());
        vo.setDescription(task.getDescription());
        vo.setDueDate(task.getDueDate());
        vo.setUserId(task.getUser().getId());
        vo.setCreatedDate(task.getCreatedDate());
        vo.setLastModifiedDate(task.getLastModifiedDate());
        vo.setCreatedBy(task.getCreatedBy() != null ? task.getCreatedBy().getUsername() : "");
        vo.setLastModifiedBy(task.getLastModifiedBy() != null ? task.getLastModifiedBy().getUsername() : "");

        return vo;
    }

    public Task convertFromVO(TaskVO taskVO) {
        return updateFromVO(new Task(), taskVO);
    }

    public Task updateFromVO(Task task, TaskVO taskVO) {
        task.setTitle(taskVO.getTitle());
        task.setDescription(taskVO.getDescription());
        task.setDueDate(taskVO.getDueDate());
        task.setUser(userRepository.findById(taskVO.getUserId()).orElse(null));

        return task;
    }

    public List<TaskVO> convertToTaskVOs(List<Task> tasks) {
        final List<TaskVO> listVO = new ArrayList<>();

        for (final Task task : tasks) {
            listVO.add(this.convertToVO(task));
        }

        return listVO;
    }

    public List<Task> convertFromTaskVOs(List<TaskVO> taskVOs) {
        final List<Task> listTask = new ArrayList<>();

        for (final TaskVO taskVO : taskVOs) {
            listTask.add(this.convertFromVO(taskVO));
        }

        return listTask;
    }
}

