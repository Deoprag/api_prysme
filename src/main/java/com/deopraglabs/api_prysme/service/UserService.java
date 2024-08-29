package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.data.vo.UserVO;
import com.deopraglabs.api_prysme.mapper.custom.UserMapper;
import com.deopraglabs.api_prysme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public List<UserVO> findAll() {
        logger.info("Finding all users");

        final List<UserVO> listVO = new ArrayList<>();

        for(final User user : userRepository.findAll()) {
            listVO.add(UserMapper.convertToVO(user));
        }

        return listVO;
    }

    public UserVO findById(long id) {
        logger.info("Finding user by id: " + id);
        return UserMapper.convertToVO(userRepository.findById(id).orElseThrow());
    }
}
