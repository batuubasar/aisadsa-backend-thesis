package com.aisadsa.aisadsabackend.entity;

import com.aisadsa.aisadsabackend.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
@NoArgsConstructor
@Data
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id", nullable = false)
    private int recommendation_id;

    @Column(name = "recommendation")
    private String recommendation;

    @Column(name = "recommendation_message")
    private String recommendation_message;

    @ElementCollection
    @CollectionTable(name = "recommendation_scores", joinColumns = @JoinColumn(name = "recommendation_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "score")
    private Map<String, Integer> recommendationScores = new HashMap<>(Map.of(
            "DWH", 0,
            "modernDataWarehouse", 0,
            "dataLake", 0,
            "dataLakehouse", 0,
            "dataFabric", 0,
            "dataMesh", 0
    ));

    @ElementCollection
    @CollectionTable(name = "recommendation_messages", joinColumns = @JoinColumn(name = "recommendation_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "message")
    private Map<String, String> recommendationMessages = new HashMap<>(Map.of(
            "streamingType", "",
            "cloudUsage", ""
    ));

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Maksimum değeri hesaplayıp, key'i döndüren metod
    public String getMaxRecommendation() {
        String bestRecommendation = "";
        int maxValue = -1;

        for (Map.Entry<String, Integer> entry : recommendationScores.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                bestRecommendation = entry.getKey();
            }
        }

        return bestRecommendation;
    }

    // Bütün mesajları birleştiren metod
    public String getAllMessage() {
        StringBuilder combinedMessage = new StringBuilder();
        for (String message : recommendationMessages.values()) {
            if (message != null && !message.isBlank()) {
                combinedMessage.append(message).append(" ");
            }
        }
        return combinedMessage.toString().trim();
    }
}