package com.aisadsa.aisadsabackend.core.dto.request;

import lombok.*;

@Getter
@Setter
public class ChatRequest {
    private String sessionId;
    private String message;
    private String username;

    public ChatRequest(String message, String username) {
        this.sessionId = "002";
        this.message = message;
        this.username = username;
    }
}
