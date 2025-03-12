package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.service.UserDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user-data")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;
/*

    @GetMapping("/{userEmail}")
    public List<UserDataResponse> getAllDataOfUser(String userEmail) {
        return userDataService.getAllDataOfUser(userEmail);
    }

    @PostMapping("/create")
    public void createUserData(@Valid @RequestBody CreateUserDataRequest createUserDataRequest) {
        userDataService.save(createUserDataRequest);
    }
*/

}