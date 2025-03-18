package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.request.CreateQuestionRequest;
import com.aisadsa.aisadsabackend.core.dto.response.QuestionResponse;
import com.aisadsa.aisadsabackend.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{questionKey}")
    public ResponseEntity<QuestionResponse>  get(@PathVariable String questionKey) { return questionService.getQuestionResponseByKey(questionKey); }

    @PostMapping("/create-question")
    public ResponseEntity<String> create(@Valid @RequestBody CreateQuestionRequest createQuestionRequest) { return questionService.save(createQuestionRequest); }

    @PutMapping("/update-question/{questionKey}")
    public ResponseEntity<String> update(@PathVariable String questionKey, @RequestBody CreateQuestionRequest createQuestionRequest) { return questionService.update(questionKey, createQuestionRequest); }

    @PostMapping("/delete-question/{questionKey}")
    public ResponseEntity<String> delete(@PathVariable String questionKey) { return questionService.delete(questionKey); }
}
