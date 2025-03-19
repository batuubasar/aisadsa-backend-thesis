package com.aisadsa.aisadsabackend.service;

import com.aisadsa.aisadsabackend.core.dto.CreateUserDataDto;
import com.aisadsa.aisadsabackend.core.dto.request.CreateUserDataRequest;
import com.aisadsa.aisadsabackend.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/// kullanılmama sebebi???

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    //@Autowired // Field injection is not recommended diğer kodda
    private final KieContainer kieContainer;

    //////// buradan recommendatiion donduruyoruz fonksiyonla ekstra olarak da user'i da dondurebiliriz
    // !1!! bu sayede drl'da user'a göre bir şey yapacaksak yapabiliriz

    public Recommendation getRecommendation(CreateUserDataRequest createUserDataDto) {
        Recommendation recommendation = new Recommendation();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("recommendation", recommendation);
        kieSession.insert(createUserDataDto);
        kieSession.fireAllRules();
        kieSession.dispose();
        return recommendation;
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
