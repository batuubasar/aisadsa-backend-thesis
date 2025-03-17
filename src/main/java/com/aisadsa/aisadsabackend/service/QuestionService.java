package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.request.CreateQuestionRequest;
import com.aisadsa.aisadsabackend.core.dto.response.QuestionResponse;
import com.aisadsa.aisadsabackend.core.exception.BadRequestException;
import com.aisadsa.aisadsabackend.core.exception.QuestionNotFoundException;
import com.aisadsa.aisadsabackend.core.mapper.QuestionMapper;
import com.aisadsa.aisadsabackend.entity.Question;
import com.aisadsa.aisadsabackend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public ResponseEntity<QuestionResponse> getQuestionResponseByKey(String questionKey){

        Question question = questionRepository.findByQuestionKey(questionKey).orElseThrow(() -> new QuestionNotFoundException("Question not found!"));

        QuestionResponse questionResponse = QuestionMapper.INSTANCE.getQuestionResponseFromQuestion(question);
        return ResponseEntity.ok(questionResponse);
    }

    public Question getQuestionByKey(String questionKey) {
        return questionRepository.findByQuestionKey(questionKey).orElseThrow(() -> new QuestionNotFoundException("Question not found!"));
    }

    public Question getQuestionById(UUID questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException("Question not found!"));
    }

    public ResponseEntity<String> save(CreateQuestionRequest createQuestionRequest) {

        questionRepository.findByQuestionKey(createQuestionRequest.getQuestionKey()).ifPresent(q -> {throw new BadRequestException("Question already exists!");});
        Question question = QuestionMapper.INSTANCE.getQuestionFromCreateQuestionRequest(createQuestionRequest);
        questionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body("Question successfully created.");
    }

    public ResponseEntity<String> update(String questionKey, CreateQuestionRequest createQuestionRequest) {
        Question question = questionRepository.findByQuestionKey(questionKey).orElseThrow(() -> new BadRequestException("Question doesn't exist!"));

        question.setQuestionKey(createQuestionRequest.getQuestionKey());
        question.setDescription(createQuestionRequest.getDescription());
        question.setOption1(createQuestionRequest.getOption1());
        question.setOption2(createQuestionRequest.getOption2());
        question.setOption3(createQuestionRequest.getOption3());
        question.setOption4(createQuestionRequest.getOption4());

        questionRepository.save(question);
        return ResponseEntity.ok("Question successfully updated.");
    }

    public ResponseEntity<String> delete(String questionKey) {
        Question question = questionRepository.findByQuestionKey(questionKey).orElseThrow(() -> new BadRequestException("Question doesn't exist!"));

        questionRepository.delete(question);
        return ResponseEntity.ok("Question successfully deleted.");
    }
}
