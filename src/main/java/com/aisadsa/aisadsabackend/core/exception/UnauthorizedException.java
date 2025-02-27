package com.aisadsa.aisadsabackend.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }
}
