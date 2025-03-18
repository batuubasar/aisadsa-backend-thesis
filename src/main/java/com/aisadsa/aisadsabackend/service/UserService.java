package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.request.RegisterRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserResponse;
import com.aisadsa.aisadsabackend.core.exception.BadRequestException;
import com.aisadsa.aisadsabackend.core.exception.UserNotFoundException;
import com.aisadsa.aisadsabackend.core.mapper.UserMapper;
import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserResponse> getUserResponseByUsername(String username){

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        UserResponse userResponse = UserMapper.INSTANCE.getUserResponseFromUser(user);
        return ResponseEntity.ok(userResponse);
    }

    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = userList.stream().map(UserMapper.INSTANCE::getUserResponseFromUser).toList();
        return ResponseEntity.ok(userResponseList);
    }

    /*public ResponseEntity<String> update(String email, RegisterRequest registerRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok("User successfully updated.");
    }*/

    public ResponseEntity<String> delete(String email) {
        User user = userRepository.findByUsername(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User successfully deleted.");
    }

    /**
     * Used in UserDataService, AuthController to return userId
     */
    public User getUserByUsername(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));

        return user;

    }
}
