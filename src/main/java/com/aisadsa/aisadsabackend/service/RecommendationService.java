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

    // Singleton Pattern
    private Recommendation recommendation;
    private KieSession kieSession;

    public Recommendation startSession(String username) {
        User user = userService.getUserByUsername(username);
        recommendation = new Recommendation();
        recommendation.setUser(user);

        if (kieSession != null) {
            kieSession.dispose();
        }
        kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("recommendation", recommendation);

        return recommendation;
    }

    public Boolean setRecommendation(UserData userData) {
        if (recommendation == null) {
            recommendation = startSession(userData.getUser().getUsername());
        }
        kieSession.insert(userData);
        kieSession.fireAllRules();
        return true;
    }

    public ResponseEntity<String> endSession() {
        String result = recommendation.getMaxRecommendation();
        String recommendationMessage = recommendation.getAllMessage();
        recommendation.setRecommendation(result);
        recommendation.setRecommendation_message(recommendationMessage);
        recommendationRepository.save(recommendation);
        if (kieSession != null) {
            kieSession.dispose();
            kieSession = null;
        }
        recommendation = null;

        // TODO result tek bir deger dondurmeyecek aslında ama suanda tek bir deger donuyor gibi kurgulandi ilerde uzun bir sonuc yazisi dondururken
        // TODO bu kismi degistiririz
        return ResponseEntity.ok("Recommendation object successfully saved to db as the recommendation text: " + result + ". " + recommendationMessage);
    }
}
