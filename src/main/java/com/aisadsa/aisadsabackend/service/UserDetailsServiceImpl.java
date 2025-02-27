package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.exception.BadRequestException;
import com.aisadsa.aisadsabackend.core.exception.UserNotFoundException;
import com.aisadsa.aisadsabackend.entity.User;
import com.aisadsa.aisadsabackend.repository.UserRepository;
import com.aisadsa.aisadsabackend.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return JwtUserDetails.create(user);
    }


    public UserDetails loadUserById(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return JwtUserDetails.create(user);
    }
}
