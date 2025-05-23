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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final KieContainer kieContainer;
    private final UserService userService;

    // Singleton Pattern
    private Recommendation recommendation;
    private KieSession kieSession;
    private String adviceText;

    public Recommendation startSession(String username) {
        User user = userService.getUserByUsername(username);
        recommendation = new Recommendation();
        recommendation.setUser(user);
        adviceText = null;

        List<String> sequenceQuestionKeyList = List.of("nonRelationalUsage","storageSize","budget","architectureType","engineeringSkills","streaming","dataVolumeRate", "mlUsage", "mdmNeed", "securityRequirement", "selfServiceBI", "slaRequirement", "dataMovementChallenge", "cloudUsage");

        for( int i = sequenceQuestionKeyList.size() -1 ; i >= 0; i-- ){
            recommendation.addQuestionToStack(sequenceQuestionKeyList.get(i));
        }

        if (kieSession != null) {
            kieSession.dispose();
        }
        kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("recommendation", recommendation);

        return recommendation;
    }

    public void setRecommendation(UserData userData) {
        if (recommendation == null) {
            recommendation = startSession(userData.getUser().getUsername());
        }

        recommendation.removeQuestion(userData.getQuestion().getQuestionKey());
        recommendation.addQuestionToAskedQuestions(userData.getQuestion().getQuestionKey());

        kieSession.insert(userData);
        kieSession.fireAllRules();
    }

    public String getNextQuestion(){
        return recommendation.getNextQuestionKey();
    }

    public int getRemainingQuestionCount() { return recommendation.getRemainingQuestionCount(); }

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

        adviceText = "The data architecture most aligned with your needs and requirements is " + result + ".\n\n" +
                recommendationMessage;


        // TODO result tek bir deger dondurmeyecek aslında ama suanda tek bir deger donuyor gibi kurgulandi ilerde uzun bir sonuc yazisi dondururken
        // TODO bu kismi degistiririz
        return ResponseEntity.ok(adviceText);
    }

    public Optional<String> getAdviceText() {
        return Optional.ofNullable(adviceText);
    }
}
