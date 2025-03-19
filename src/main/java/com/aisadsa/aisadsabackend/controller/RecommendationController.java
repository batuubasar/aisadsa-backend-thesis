package com.aisadsa.aisadsabackend.controller;

import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.entity.Recommendation;
import com.aisadsa.aisadsabackend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    // YANLIS GALIBAA POST DEGİL GEL VE İD OLCAK / DA
    @PostMapping("/get-recommendation")
    public ResponseEntity<Recommendation> getRecommendation(@RequestBody CreateUserDataRequest createUserDataDto) {
        Recommendation recommendation = recommendationService.getRecommendation(createUserDataDto);
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }
    /*  TEK TEK YUKARI 20 SORU ALT ALTA POSTMANDEKİ GİBİ BOYLE
    // AMA TEK TEK OLURSA .DRL DA HER SEFERDE GIREN YERDE SORUN OLUYOR BELKI SORULAR BITTIKTEN SONRA
    // SUBMIT REQUEST GIBI BIR SEY YAPARIZ O YES OLURSA O KOSULU CALISTIRIR !!!

    @PostMapping("/get-recommendation")
    public ResponseEntity<Recommendation> getRecommendation(@RequestBody List<CreateUserDataDTO> createUserDataDTOs) {
        Recommendation recommendation = recommendationService.getRecommendation(createUserDataDTOs);
        return new ResponseEntity<>(recommendation, HttpStatus.OK);
    }*/
}
