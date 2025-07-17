package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.data.dto.UserRequestDTO;
import com.deopraglabs.api_prysme.data.dto.UserResponseDTO;
import com.deopraglabs.api_prysme.data.model.Team;
import com.deopraglabs.api_prysme.data.model.User;
import com.deopraglabs.api_prysme.mapper.impl.UserMapperImpl;
import com.deopraglabs.api_prysme.repository.TeamRepository;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.utils.Utils;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        final List<String> validations = validateUserInfo(userRequestDTO, null);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        final var entity = userMapper.fromRequestDTO(userRequestDTO);
        
        // Handle password encryption
        if (!Utils.isEmpty(userRequestDTO.getPassword())) {
            entity.setPassword(Utils.encryptPassword(userRequestDTO.getPassword()));
        }
        
        final var savedEntity = userRepository.save(entity);

        // Auto-create team for managers
        if (savedEntity.getAuthorities().stream().anyMatch(auth -> "MANAGER".equals(auth.getAuthority()))) {
            final var team = teamRepository.save(new Team(null, savedEntity.getFullName(), savedEntity, new ArrayList<>()));
            savedEntity.setTeam(team);
            userRepository.save(savedEntity);
        }
        
        return userMapper.toResponseDTO(savedEntity);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO userRequestDTO) {
        logger.info("Updating user with id: " + id);
        final var existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
                
        final List<String> validations = validateUserInfo(userRequestDTO, id);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }
        
        final var updatedEntity = userMapper.fromRequestDTO(userRequestDTO);
        updatedEntity.setId(id);
        updatedEntity.setCreatedDate(existingEntity.getCreatedDate());
        
        // Keep existing password if not provided
        if (Utils.isEmpty(userRequestDTO.getPassword())) {
            updatedEntity.setPassword(existingEntity.getPassword());
        } else {
            updatedEntity.setPassword(Utils.encryptPassword(userRequestDTO.getPassword()));
        }
        
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
        user.setPassword(Utils.encryptPassword(password));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // Business Rules - Restored and adapted for DTOs
    private List<String> validateUserInfo(UserRequestDTO userRequestDTO, UUID existingUserId) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(userRequestDTO, validations);
        validateUniqueFields(userRequestDTO, validations, existingUserId);

        return validations;
    }

    private void validateBasicFields(UserRequestDTO userRequestDTO, List<String> validations) {
        Utils.checkField(validations, Utils.isEmpty(userRequestDTO.getFirstName()), "First name is required");
        Utils.checkField(validations, Utils.isEmpty(userRequestDTO.getLastName()), "Last name is required");
        Utils.checkField(validations, Utils.isEmpty(userRequestDTO.getEmail()), "Email is required");
        Utils.checkField(validations, userRequestDTO.getBirthDate() == null, "Birth date is required");
        Utils.checkField(validations, userRequestDTO.getGender() == '\u0000', "Gender is required");
        Utils.checkField(validations, Utils.isEmpty(userRequestDTO.getPhoneNumber()), "Phone number is required");
        // Password is required only for new users (when existingUserId is null)
        Utils.checkField(validations, (Utils.isEmpty(userRequestDTO.getPassword()) && existingUserId == null), "Password is required");
    }

    private void validateUniqueFields(UserRequestDTO userRequestDTO, List<String> validations, UUID existingUserId) {
        if (!Utils.isEmpty(userRequestDTO.getEmail())) {
            User existingUserWithEmail = userRepository.findByEmail(userRequestDTO.getEmail());
            if (existingUserWithEmail != null && !existingUserWithEmail.getId().equals(existingUserId)) {
                validations.add("Email is already associated with another account");
            }
        }
        
        if (!Utils.isEmpty(userRequestDTO.getPhoneNumber())) {
            User existingUserWithPhone = userRepository.findByPhoneNumber(userRequestDTO.getPhoneNumber());
            if (existingUserWithPhone != null && !existingUserWithPhone.getId().equals(existingUserId)) {
                validations.add("Phone number is already associated with another account");
            }
        }
        
        if (!Utils.isEmpty(userRequestDTO.getUsername())) {
            User existingUserWithUsername = userRepository.findByUsername(userRequestDTO.getUsername());
            if (existingUserWithUsername != null && !existingUserWithUsername.getId().equals(existingUserId)) {
                validations.add("Username is already taken");
            }
        }
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
