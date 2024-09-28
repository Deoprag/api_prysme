package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Task;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskMapper {

    public TaskVO convertToVO(Task task) {
        final TaskVO vo = new TaskVO();

        vo.setKey(task.getId());
        vo.setTitle(task.getTitle());
        vo.setDescription(task.getDescription());
        vo.setCompletedDateTime(task.getCompletedDateTime());
        vo.setCreatedDate(task.getCreatedDate());
        vo.setLastModifiedDate(task.getLastModifiedDate());

        return vo;
    }

    public Task convertFromVO(TaskVO taskVO) {
        return updateFromVO(new Task(), taskVO);
    }

    public Task updateFromVO(Task task, TaskVO taskVO) {
        task.setTitle(taskVO.getTitle());
        task.setDescription(taskVO.getDescription());
        task.setCompletedDateTime(taskVO.getCompletedDateTime());
        task.setCreatedDate(taskVO.getCreatedDate());
        task.setLastModifiedDate(taskVO.getLastModifiedDate());

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

