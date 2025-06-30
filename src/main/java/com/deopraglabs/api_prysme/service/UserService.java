package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.dto.UserRequestDTO;
import com.deopraglabs.api_prysme.data.dto.UserResponseDTO;
import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.impl.UserMapperImpl;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserMapperImpl userMapper;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapperImpl userMapper, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.teamRepository = teamRepository;
    }

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        logger.info("Saving user: " + userRequestDTO);
        final var entity = userMapper.fromRequestDTO(userRequestDTO);
        final var savedEntity = userRepository.save(entity);
        return userMapper.toResponseDTO(savedEntity);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO userRequestDTO) {
        logger.info("Updating user with id: " + id);
        final var existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        final var updatedEntity = userMapper.fromRequestDTO(userRequestDTO);
        updatedEntity.setId(id);
        updatedEntity.setCreatedDate(existingEntity.getCreatedDate());
        
        final var savedEntity = userRepository.save(updatedEntity);
        return userMapper.toResponseDTO(savedEntity);
    }

    public List<UserResponseDTO> findAll() {
        logger.info("Finding all users");
        return userMapper.toResponseDTOList(userRepository.findAll());
    }

    public List<UserResponseDTO> findAllByTeamId(UUID teamId) {
        logger.info("Finding all users by team id: " + teamId);
        return userMapper.toResponseDTOList(userRepository.findAllByTeamId(teamId));
    }

    public List<UserResponseDTO> findAllByManagerId(UUID managerId) {
        logger.info("Finding all users by manager id: " + managerId);
        final User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new NoSuchElementException("Manager not found"));
        
        if (manager.getTeam() != null) {
            return userMapper.toResponseDTOList(userRepository.findAllByTeamId(manager.getTeam().getId()));
        }
        return List.of();
    }

    public UserResponseDTO findById(UUID id) {
        logger.info("Finding user by id: " + id);
        final var entity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return userMapper.toResponseDTO(entity);
    }

    public ResponseEntity<?> delete(UUID id) {
        logger.info("Deleting user: " + id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> resetPassword(UUID id, String password) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setPassword(password); // Assuming password encryption is handled elsewhere
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: " + username);
        final var user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException(username);
        }
    }


}
