package com.aisadsa.aisadsabackend.service;


import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataCreateResponse;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.core.exception.BadRequestException;
import com.aisadsa.aisadsabackend.core.exception.UserDataNotFoundException;
import com.aisadsa.aisadsabackend.core.mapper.UserDataMapper;
import com.aisadsa.aisadsabackend.entity.Question;
import com.aisadsa.aisadsabackend.entity.UserData;
import com.aisadsa.aisadsabackend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final QuestionService questionService;
    private final RecommendationService recommendationService;


    public ResponseEntity<List<UserDataResponse>> getAllDataOfUser(String username) {
        UUID userId = userService.getUserByUsername(username).getId();
        List<UserData> userDataList = userDataRepository.findByUserId(userId);
        List<UserDataResponse> userDataResponseList = UserDataMapper.INSTANCE.getUserDataResponseListFromUserDataList(userDataList);
        IntStream.range(0, userDataList.size())
                .forEach(i -> userDataResponseList.get(i).setQuestionKey(userDataList.get(i).getQuestion().getQuestionKey()));
        return ResponseEntity.ok(userDataResponseList);
    }

    public ResponseEntity<List<UserDataResponse>> getDataOfUserByRecommendationId(String username, int recommendationId) {
        return null;
    }

    public ResponseEntity<UserDataCreateResponse> save(String username, CreateUserDataRequest createUserDataRequest) {
        User user = userService.getUserByUsername(username);
        Question question = questionService.getQuestionByKey(createUserDataRequest.getQuestionKey());

        CreateUserDataDto createUserDataDto = new CreateUserDataDto(user, question, createUserDataRequest.getUserData());
        UserData userData = UserDataMapper.INSTANCE.getUserDataFromCreateUserDataDto(createUserDataDto);

        userDataRepository.save(userData);
        recommendationService.setRecommendation(userData);

        String nextQuestionKey = recommendationService.getNextQuestion();
        int remainingQuestionCount = recommendationService.getRemainingQuestionCount();
        return ResponseEntity.ok(new UserDataCreateResponse(nextQuestionKey, remainingQuestionCount));
    }

    public ResponseEntity<String> update(String username, CreateUserDataRequest createUserDataRequest) {
        User user = userService.getUserByUsername(username);
        Question question = questionService.getQuestionByKey(createUserDataRequest.getQuestionKey());

        UserData userData = userDataRepository.findByUserIdAndQuestionId(user.getId(), question.getId()).orElseThrow(() -> new UserDataNotFoundException("UserData not found!"));

        userData.setUserData(createUserDataRequest.getUserData());
        userData.setUser(user);
        userData.setQuestion(question);

        userDataRepository.save(userData);
        return ResponseEntity.ok("UserData successfully updated!");
    }

    public ResponseEntity<String> delete(String username, String questionKey) {
        User user = userService.getUserByUsername(username);
        Question question = questionService.getQuestionByKey(questionKey);
        UserData userData = userDataRepository.findByUserIdAndQuestionId(user.getId(), question.getId()).orElseThrow(() -> new UserDataNotFoundException("UserData not found!"));

        userDataRepository.delete(userData);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question successfully deleted.");
    }

    public ResponseEntity<String> submit() {
        return recommendationService.endSession();
    }
}


