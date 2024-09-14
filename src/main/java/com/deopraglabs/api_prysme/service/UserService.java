package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.UserController;
import com.deopraglabs.api_prysme.data.model.Role;
import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.mapper.custom.UserMapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserVO save(UserVO userVO) {
        logger.info("Saving user: " + userVO);
        final List<String> validations = validateUserInfo(userVO);

        if (validations.isEmpty()) {
            if (userVO.getKey() > 0) {
                return userMapper.convertToVO(userRepository.save(userMapper.updateFromVO(
                        userRepository.findById(userVO.getKey())
                                .orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(userVO.getKey())),
                        userVO
                ))).add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
            } else {
                return userMapper.convertToVO(userRepository.save(userMapper.convertFromVO(userVO)))
                        .add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
            }
        }

        throw new CustomRuntimeException.UserBRValidationException(validations);
    }

    public List<UserVO> findAll() {
        logger.info("Finding all users");
        final var users = userMapper.convertToUserVOs(userRepository.findAllByActive(true));
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class).findById(user.getKey())).withSelfRel()));

        return users;
    }

    public UserVO findById(long id) {
        logger.info("Finding user by id: " + id);
        return userMapper.convertToVO(userRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.UserNotFoundException(id)))
                .add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting user: " + id);
        return userRepository.isDeleted(id) > 0
                ? ResponseEntity.notFound().build()
                : userRepository.softDeleteById(id, DatabaseUtils.generateRandomValue(id, 11)) > 0
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validateUserInfo(UserVO userVO) {
        final List<String> validations = new ArrayList<>();

        checkField(validations, Utils.isEmpty(userVO.getFirstName()), "First name is required");
        checkField(validations, Utils.isEmpty(userVO.getLastName()), "Last name is required");
        checkField(validations, Utils.isEmpty(userVO.getEmail()), "Email is required");
        checkField(validations, userVO.getRole().isEmpty() || userVO.getRole().isBlank(), "Role is required");
        checkField(validations, userVO.getBirthDate() == null, "Birth date is required");
        checkField(validations, userVO.getGender() == '\u0000', "Gender is required");
        checkField(validations, Utils.isEmpty(userVO.getPhoneNumber()), "Phone number is required");
        checkField(validations, Utils.isEmpty(userVO.getPassword()), "Password is required");
        if(userRepository.findByEmail(userVO.getEmail()) != null && !Utils.isEmpty(userVO.getEmail()))
            validations.add("Email is already associated with another account");
        if(userRepository.findByPhoneNumber(userVO.getPhoneNumber()) != null && !Utils.isEmpty(userVO.getPhoneNumber()))
            validations.add("Phone number is already associated with another account");
        if(!userVO.getRole().isEmpty() && !userVO.getRole().isBlank()
                && userVO.getRole().equals(Role.MANAGER.toString())
                && userVO.getTeam() != null) {
            // IMPLEMENTAR DEPOIS
        }

        return validations;
    }

    private void checkField(List<String> validations, boolean condition, String message) {
        if (condition) validations.add(message);
    }
}
