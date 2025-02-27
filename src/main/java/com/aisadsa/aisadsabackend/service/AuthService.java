package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.request.LoginRequest;
import com.aisadsa.aisadsabackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String authenticate(LoginRequest loginUserRequest, UUID userId) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtTokenProvider.generateJwtToken(auth, userId);
    }
}
