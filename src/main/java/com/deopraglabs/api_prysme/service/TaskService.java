package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.TaskController;
import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.mapper.custom.TaskMapper;
import com.deopraglabs.api_prysme.repository.TaskRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class TaskService {

    private final Logger logger = Logger.getLogger(TaskService.class.getName());

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    public TaskVO save(TaskVO taskVO) {
        logger.info("Saving task: " + taskVO);
        final List<String> validations = validateTaskInfo(taskVO);
        taskVO.setDueDate(Utils.resetTime(taskVO.getDueDate()));

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (taskVO.getKey() > 0) {
            return taskMapper.convertToVO(taskRepository.save(taskMapper.updateFromVO(
                    taskRepository.findById(taskVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.TaskNotFoundException(taskVO.getKey())),
                    taskVO
            ))).add(linkTo(methodOn(TaskController.class).findById(taskVO.getKey())).withSelfRel());
        } else {
            final var task = taskRepository.save(taskMapper.convertFromVO(taskVO));
            return taskMapper.convertToVO(taskRepository.save(task))
                    .add(linkTo(methodOn(TaskController.class).findById(task.getId())).withSelfRel());
        }
    }

    public List<TaskVO> findAll() {
        logger.info("Finding all tasks");
        final var tasks = taskMapper.convertToTaskVOs(taskRepository.findAll());
        tasks.forEach(task -> task.add(linkTo(methodOn(TaskController.class).findById(task.getKey())).withSelfRel()));

        return tasks;
    }

    public TaskVO findById(long id) {
        logger.info("Finding task by id: " + id);
        return taskMapper.convertToVO(taskRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.TaskNotFoundException(id)))
                .add(linkTo(methodOn(TaskController.class).findById(id)).withSelfRel());
    }

    public List<TaskVO> findAllByUsernameAndDate(String username, String date) {
        logger.info("Finding all tasks by username: " + username + " and date: " + date);
        var user = userRepository.findByUsername(username);
        return taskMapper.convertToTaskVOs(taskRepository.findAllByUserIdAndDueDate(user.getId(), Utils.formatStringToDate(date)));
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting task: " + id);
        return taskRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateTaskInfo(TaskVO taskVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(taskVO, validations);
        validateUniqueFields(taskVO, validations);

        return validations;
    }

    private void validateBasicFields(TaskVO taskVO, List<String> validations) {

    }

    private void validateUniqueFields(TaskVO taskVO, List<String> validations) {

    }
}
