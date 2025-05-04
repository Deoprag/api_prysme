package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.security.dto.AccountCredentialsDTO;
import com.deopraglabs.api_prysme.security.dto.TokenDTO;
import com.deopraglabs.api_prysme.repository.UserRepository;
import com.deopraglabs.api_prysme.security.JwtTokenProvider;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.logging.Logger;

@Service
public class AuthService {

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    @Autowired
    AuthService(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> signIn(AccountCredentialsDTO data) {
        logger.info("Signing In: " + data.getUsername() + " " + data.getPassword());
        try {
            final var username = data.getUsername();
            final var password = data.getPassword();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            final var user = userRepository.findByUsername(username);
            var tokenResponse = new TokenDTO();

            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found");
            }

            final HashMap<String, Object> map = new HashMap<>();
            map.put("token", tokenResponse);
            map.put("userId", user.getId());

            return ResponseEntity.ok(map);
        } catch (Exception e) {
            throw new CustomRuntimeException.InvalidUsernameOrPasswordException("Invalid username or password");
        }
    }

    public ResponseEntity<?> refreshToken(String username, String refreshToken) {
        logger.info("Refreshing token from: " + username);
        final var user = userRepository.findByUsername(username);
        var tokenResponse = new TokenDTO();

        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        final HashMap<String, Object> map = new HashMap<>();
        map.put("token", tokenResponse);
        map.put("userId", user.getId());

        return ResponseEntity.ok(map);
    }
}
