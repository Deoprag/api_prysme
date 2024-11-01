package com.deopraglabs.api_prysme.security;

import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Service
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider tokenProvider;

    @Autowired
    JwtTokenFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        final String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && tokenProvider.validateToken(token)) {
                final Authentication auth = tokenProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (CustomRuntimeException.InvalidJwtAuthenticationException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
