package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.request.ChatRequest;
import com.aisadsa.aisadsabackend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;


    @GetMapping("/qa")
    public String chat(@RequestBody ChatRequest chatRequest) {
        return aiService.chat(chatRequest.getMessage());
    }


    /*@GetMapping("/document-generation")
    public String generateDocument(@RequestParam String topic) {
        return aiService.generateDocument(topic);
    }*/
}
