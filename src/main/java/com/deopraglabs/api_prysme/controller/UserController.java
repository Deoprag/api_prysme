package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserVO> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserVO findById(@PathVariable(value = "id") long id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserVO create(@RequestBody UserVO user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/save",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserVO update(@RequestBody UserVO user) {
        return userService.save(user);
    }
}
