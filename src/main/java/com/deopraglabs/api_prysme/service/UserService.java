package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.mapper.custom.UserMapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.DatabaseUtils;
import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserVO save(UserVO userVO) {
        logger.info("Saving user: " + userVO);
        if (userVO.getId() > 0) {
            return UserMapper.convertToVO(userRepository.save(UserMapper.updateFromVO(
                    userRepository.findById(userVO.getId()).orElseThrow(),
                    userVO
            )));
        } else {
            return UserMapper.convertToVO(userRepository.save(UserMapper.convertFromVO(userVO)));
        }
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting user: " + id);
        return userRepository.isDeleted(id) > 0
                ? ResponseEntity.notFound().build()
                : userRepository.softDeleteById(id, DatabaseUtils.generateUniquePhoneNumber(id)) > 0
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
    }

    public List<UserVO> findAll() {
        logger.info("Finding all users");
        return UserMapper.convertToUserVOs(userRepository.findAllByActive(true));
    }

    public UserVO findById(long id) {
        logger.info("Finding user by id: " + id);
        return UserMapper.convertToVO(userRepository.findById(id).orElseThrow());
    }
}
