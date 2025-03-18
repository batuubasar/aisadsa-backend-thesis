package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.request.RegisterRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserResponse;
import com.aisadsa.aisadsabackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;



    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> get(@PathVariable String username) { return userService.getUserResponseByUsername(username); }

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAll() { return userService.getAllUsers(); }

/*
    @PutMapping("/update-user/{email}")
    public ResponseEntity<String> update(@PathVariable String email, @RequestBody RegisterRequest registerRequest) { return userService.update(email, registerRequest); }
*/

    @PostMapping("/delete-user/{email}")
    public ResponseEntity<String> delete(@PathVariable String email) { return userService.delete(email); }
}
