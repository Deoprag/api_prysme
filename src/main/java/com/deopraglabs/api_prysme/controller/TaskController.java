package com.deopraglabs.api_prysme.controller;


import com.deopraglabs.api_prysme.data.vo.TaskVO;
import com.deopraglabs.api_prysme.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Task", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<TaskVO> findAll() {
        return taskService.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TaskVO findById(@PathVariable(value = "id") long id) {
        return taskService.findById(id);
    }

    @GetMapping(value = "findAllByUsername/{username}/{date}",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<TaskVO> findAllByUsernameAndDate(@PathVariable(value = "username") String username, @PathVariable(value = "date") String date) {
        return taskService.findAllByUsernameAndDate(username, date);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TaskVO create(@RequestBody TaskVO task) {
        return taskService.save(task);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TaskVO update(@RequestBody TaskVO task) {
        return taskService.save(task);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return taskService.delete(id);
    }
}