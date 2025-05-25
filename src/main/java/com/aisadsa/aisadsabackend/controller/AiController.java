package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.auth.service.JwtService;
import com.aisadsa.aisadsabackend.core.dto.request.ChatRequest;
import com.aisadsa.aisadsabackend.service.AiService;
import com.aisadsa.aisadsabackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final JwtService jwtService;

    @PostMapping("/qa")
    public String chat(@RequestHeader("Authorization") String authHeader, @RequestBody String message) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        ChatRequest chatRequest = new ChatRequest(message, username);
        return aiService.chat(chatRequest);
    }

    @GetMapping("/document-generation")
    public String generateDocument(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        ChatRequest chatRequest = new ChatRequest(username);
        return aiService.generateDocument(chatRequest);
    }
}
