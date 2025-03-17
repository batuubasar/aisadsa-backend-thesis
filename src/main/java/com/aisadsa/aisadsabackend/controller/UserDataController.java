package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.auth.service.JwtService;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.service.UserDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user-data")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;
    private final JwtService jwtService;

    @GetMapping("/{username}")
    public List<UserDataResponse> getAllDataOfUser(@PathVariable String username) {
        return userDataService.getAllDataOfUser(username);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CreateUserDataRequest createUserDataRequest) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        return userDataService.save(username ,createUserDataRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CreateUserDataRequest createUserDataRequest) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        return userDataService.update(username ,createUserDataRequest);
    }

    @PostMapping("/delete/{questionId}")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String authHeader, @PathVariable UUID questionId) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        return userDataService.delete(username ,questionId);
    }

}