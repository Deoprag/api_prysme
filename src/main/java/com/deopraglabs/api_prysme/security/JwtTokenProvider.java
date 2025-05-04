package com.deopraglabs.api_prysme.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.deopraglabs.api_prysme.security.dto.TokenDTO;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private Algorithm algorithm = null;

    @Value("${cors.security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${cors.security.jwt.token.expire-length:3600000}")
    private long msValidity = 3600000;

    @Autowired
    JwtTokenProvider(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + msValidity);
        final var accessToken = getAccessToken(username, roles, now, expiration);
        final var refreshToken = getRefreshToken(username, roles, now);

        return new TokenDTO(username, true, now, expiration, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final DecodedJWT decodedJWT = verifier.verify(refreshToken);
        final String username = decodedJWT.getSubject();
        final List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date expiration) {
        final String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + (msValidity * 8)))
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        final DecodedJWT decodedJWT = decodedToken(token);
        final UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        final var decodeAlgorithm = Algorithm.HMAC256(secretKey.getBytes());
        final JWTVerifier verifier = JWT.require(decodeAlgorithm).build();

        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        return bearerToken != null && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) throws CustomRuntimeException.InvalidJwtAuthenticationException {
        final DecodedJWT decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new CustomRuntimeException.InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
