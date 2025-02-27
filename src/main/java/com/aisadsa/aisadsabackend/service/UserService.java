package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.request.RegisterRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserResponse;
import com.aisadsa.aisadsabackend.core.exception.BadRequestException;
import com.aisadsa.aisadsabackend.core.exception.UserNotFoundException;
import com.aisadsa.aisadsabackend.core.mapper.UserMapper;
import com.aisadsa.aisadsabackend.entity.User;
import com.aisadsa.aisadsabackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserResponse> getUserByEmail(String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        UserResponse userResponse = UserMapper.INSTANCE.getUserResponseFromUser(user);
        return ResponseEntity.ok(userResponse);
    }

    public ResponseEntity<String> save(RegisterRequest registerRequest) {
        userRepository.findByEmail(registerRequest.getEmail()).ifPresent(u -> { throw new BadRequestException("Email already exists!");});

        isValidPassword(registerRequest.getPassword());
        User user = UserMapper.INSTANCE.getUserFromRegisterUserRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
    }

/*
    public ResponseEntity<UserResponse> login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmailAndPassword(loginUserRequest.getEmail(), loginUserRequest.getPassword()).orElseThrow(() -> new UserNotFoundException("User not found!"));

        UserResponse userResponse = UserMapper.INSTANCE.getUserResponseFromUser(user);
        return ResponseEntity.ok(userResponse);
    }
*/

    public ResponseEntity<String> update(String email, RegisterRequest registerRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
        return ResponseEntity.ok("User successfully updated.");
    }

    public ResponseEntity<String> delete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        userRepository.delete(user);
        return ResponseEntity.ok("User successfully deleted.");
    }

    public void isValidPassword(String password){
        if (password.length() < 5) {
            throw new BadRequestException("Password must be at least 5 characters long!");
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            }
            if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            }
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }
        if (!hasUpperCase || !hasLowerCase || !hasDigit) {
            throw new BadRequestException("Password must contain at least one uppercase letter, one lowercase letter, and one number!");
        }
    }

    /**
     * Used in UserDataService, AuthController to return userId
     */
    public UUID getUserId(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        return user.getId();

    }
}
