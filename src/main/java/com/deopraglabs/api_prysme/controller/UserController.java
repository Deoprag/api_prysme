package com.deopraglabs.api_prysme.controller;

import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds all users",
            description = "Finds all users",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(array = @ArraySchema(schema = @Schema(implementation = UserVO.class)))
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public List<UserVO> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/findAllByTeamId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds all users",
            description = "Finds all users",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(array = @ArraySchema(schema = @Schema(implementation = UserVO.class)))
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public List<UserVO> findAllByTeamId(@PathVariable(value = "id") long id) {
        return userService.findAllByTeamId(id);
    }

    @GetMapping(value = "/findAllByManagerId/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserVO> findAllByManagerId(@PathVariable(value = "id") long id) {
        return userService.findAllByManagerId(id);
    }

    @PostMapping(value = "/resetPassword/{id}/{password}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> resetPassword(@PathVariable(value = "id") long id, @PathVariable(value = "password") String password) {
        return userService.resetPassword(id, password);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds an user",
            description = "Finds an user",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserVO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content()),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public UserVO findById(@PathVariable(value = "id") long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Adds a new user",
            description = "Adds a new user by passing in a JSON or XML representation of the user",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public UserVO create(@RequestBody UserVO user) {
        return userService.save(user);
    }

    @PutMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Updates an user",
            description = "Updates an user by passing in a JSON or XML representation of the user",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public UserVO update(@RequestBody UserVO user) {
        return userService.save(user);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes an user",
            description = "Deletes an user by passing in a JSON or XML representation of the user",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content()),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content()),
            }
    )
    public ResponseEntity<?> delete(@PathVariable long id) {
        return userService.delete(id);
    }
}
