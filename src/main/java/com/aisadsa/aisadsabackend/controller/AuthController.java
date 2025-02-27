package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.request.LoginRequest;
import com.aisadsa.aisadsabackend.core.dto.request.RegisterRequest;
import com.aisadsa.aisadsabackend.core.dto.response.LoginResponse;
import com.aisadsa.aisadsabackend.service.AuthService;
import com.aisadsa.aisadsabackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginUserRequest) {
        UUID userId = userService.getUserId(loginUserRequest.getEmail());
        String jwtToken = authService.authenticate(loginUserRequest, userId); // userId is embedded in jwtToken
        return ResponseEntity.ok(new LoginResponse("Bearer " + jwtToken));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) { return userService.save(registerRequest); }

}
