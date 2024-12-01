package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.vo.GoalVO;
import com.deopraglabs.api_prysme.service.GoalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goal")
@Tag(name = "Goal", description = "Endpoints for managing Goals")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<GoalVO> findAll() {
        return goalService.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GoalVO findById(@PathVariable(value = "id") long id) {
        return goalService.findById(id);
    }

    @GetMapping(value = "/findCurrentGoalByUsername/{username}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GoalVO findCurrentGoalByUsername(@PathVariable(value = "username") String username) {
        return goalService.findCurrentGoalByUsername(username);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GoalVO create(@RequestBody GoalVO goal) {
        return goalService.save(goal);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public GoalVO update(@RequestBody GoalVO goal) {
        return goalService.save(goal);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return goalService.delete(id);
    }
}
