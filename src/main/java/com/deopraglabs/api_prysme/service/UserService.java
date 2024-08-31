package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.mapper.custom.UserMapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserVO save(UserVO userVO) {
        logger.info("Saving user: " + userVO);
        return UserMapper.convertToVO(userRepository.save(UserMapper.convertFromVO(userVO)));
    }

    public void delete(long id) {
        logger.info("Deleting user: " + id);
        userRepository.softDeleteById(id, DatabaseUtils.generateUniquePhoneNumber(id));
    }

    public List<UserVO> findAll() {
        logger.info("Finding all users");
        return UserMapper.convertToUserVOs(userRepository.findAll());
    }

    public UserVO findById(long id) {
        logger.info("Finding user by id: " + id);
        return UserMapper.convertToVO(userRepository.findById(id).orElseThrow());
    }
}