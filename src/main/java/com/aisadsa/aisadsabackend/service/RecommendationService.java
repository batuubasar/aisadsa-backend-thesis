package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.auth.entity.User;
import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.entity.Recommendation;
import com.aisadsa.aisadsabackend.entity.UserData;
import com.aisadsa.aisadsabackend.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
/// kullanılmama sebebi???

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final KieContainer kieContainer;
    private final UserService userService;
    private Recommendation recommendation;

    public Recommendation startSession(String username) {
        User user = userService.getUserByUsername(username);
        recommendation = new Recommendation();
        recommendation.setUser(user);
        return recommendation;
    }

    public Boolean setRecommendation(UserData userData) {
        if (recommendation == null) {
            recommendation = startSession(userData.getUser().getUsername());
        }

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("recommendation", recommendation);
        kieSession.insert(userData);
        kieSession.fireAllRules();
        kieSession.dispose();
        return true;
    }

    public ResponseEntity<String> endSession() {
        recommendationRepository.save(recommendation);
        return ResponseEntity.ok("Recommendation object successfully saved to db.");
    }
    // ustekini kullanabiliriz her soruda tek tek gonderir.
    /*
    public Recommendation getRecommendation(
            List<CreateUserDataDTO> createUserDataDTOs) {
        Recommendation finalRecommendation = new Recommendation();

        for (CreateUserDataDTO createUserDataDTO : createUserDataDTOs) {
            // Her soru için KieSession başlatıyoruz
            KieSession kieSession = kieContainer.newKieSession();
            kieSession.setGlobal("questionRecommendation", finalRecommendation);
            kieSession.insert(createUserDataDTO);
            kieSession.fireAllRules();
            kieSession.dispose();
        }

        // En son, en yüksek öneriyi döndürüyoruz
        return finalRecommendation;
    }*/

}
