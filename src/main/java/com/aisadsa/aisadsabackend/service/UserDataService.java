package com.aisadsa.aisadsabackend.service;


import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDTO;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.core.dto.response.UserDataResponse;
import com.aisadsa.aisadsabackend.core.exception.UserDataNotFoundException;
import com.aisadsa.aisadsabackend.core.mapper.UserDataMapper;
import com.aisadsa.aisadsabackend.entity.Question;
import com.aisadsa.aisadsabackend.entity.UserData;
import com.aisadsa.aisadsabackend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
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


    public List<UserDataResponse> getAllDataOfUser(String username) {
        UUID userId = userService.getUserByUsername(username).getId();
        List<UserData> userDataList = userDataRepository.findByUserId(userId);
        List<UserDataResponse> userDataResponseList = UserDataMapper.INSTANCE.getUserDataResponseListFromUserDataList(userDataList);
        IntStream.range(0, userDataList.size())
                .forEach(i -> userDataResponseList.get(i).setQuestionKey(userDataList.get(i).getQuestion().getQuestionKey()));
        return userDataResponseList;
    }

    public ResponseEntity<String> save(String username, CreateUserDataRequest createUserDataRequest) {
        User user = userService.getUserByUsername(username);
        Question question = questionService.getQuestionByKey(createUserDataRequest.getQuestionKey());

        CreateUserDataDTO createUserDataDTO = new CreateUserDataDTO(user, question, createUserDataRequest.getUserData());

        UserData userData = UserDataMapper.INSTANCE.getUserDataFromCreateUserDataDTO(createUserDataDTO);

        userDataRepository.save(userData);
        return ResponseEntity.ok("UserData successfully saved!");
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

    public ResponseEntity<String> delete(String username, UUID questionId) {
        User user = userService.getUserByUsername(username);
        UserData userData = userDataRepository.findByUserIdAndQuestionId(user.getId(), questionId).orElseThrow(() -> new UserDataNotFoundException("UserData not found!"));

        userDataRepository.delete(userData);
        return ResponseEntity.ok("Question successfully deleted.");
    }
}


