package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.UserController;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.mapper.custom.UserMapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
