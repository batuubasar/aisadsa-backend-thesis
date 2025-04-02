package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.auth.service.JwtService;
import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.entity.Recommendation;
import com.aisadsa.aisadsabackend.entity.UserData;
import com.aisadsa.aisadsabackend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
/*
    private final RecommendationService recommendationService;
    private final JwtService jwtService;


    @PostMapping("/set-recommendation")
    public ResponseEntity<String> setRecommendation(@RequestBody UserData userData, Recommendation recommendation) {
        return recommendationService.setRecommendation(userData);
    }


    @PostMapping("/get-recommendation")
    public ResponseEntity<Recommendation> getRecommendation(@RequestBody List<CreateUserDataDTO> createUserDataDTOs) {
        Recommendation recommendation = recommendationService.getRecommendation(createUserDataDTOs);
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }*/
}
